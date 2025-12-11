package app;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import model.Case;
import model.CaseStatus;
import model.Form;
import model.FormTemplate;
import model.QuestionInstance;
import model.QuestionTemplate;

public class DataStore {
    private static DataStore instance;
    private ArrayList<Case> cases;
    private ArrayList<FormTemplate> formTemplates;
    private int nextTemplateId;
    private int nextQuestionId;
    private int nextCaseId;
    private Gson gson;
    
    private DataStore() {
        // Private constructor to enforce singleton pattern
        this.cases = new ArrayList<>();
        this.formTemplates = new ArrayList<>();
        // Use GsonBuilder for pretty printing JSON files
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        loadIdCounters();
    }
    
    public static DataStore getInstance() {
        if (instance == null) { 
            instance = new DataStore();
        }
        return instance;
    }
    
    public void loadFromDisk() {
        loadFormTemplatesFromJson();
        loadFormsFromJson();
        loadCasesFromJson();
    }
    
    private void loadFormTemplatesFromJson() {
        try {
            Path projectRoot = Paths.get(System.getProperty("user.dir"));
            Path formTemplatesJsonPath = projectRoot.resolve("src/database/formTemplates.json");
            
            if (!formTemplatesJsonPath.toFile().exists()) {
                return;
            }
            
            BufferedReader reader = new BufferedReader(new FileReader(formTemplatesJsonPath.toFile()));
            JsonArray templatesArray = gson.fromJson(reader, JsonArray.class);
            reader.close();
            
            if (templatesArray == null || templatesArray.size() == 0) {
                return;
            }
            
            int maxTemplateId = 0;
            int maxQuestionId = 0;
            
            for (JsonElement element : templatesArray) {
                JsonObject templateObj = element.getAsJsonObject();
                
                int templateId = templateObj.get("id").getAsInt();
                String templateName = templateObj.get("name").getAsString();
                
                if (templateId > maxTemplateId) maxTemplateId = templateId;
                
                FormTemplate template = new FormTemplate(templateId, templateName);
                JsonArray questionsArray = templateObj.getAsJsonArray("questions");
                if (questionsArray != null) {
                    for (JsonElement questionElement : questionsArray) {
                        JsonObject questionObj = questionElement.getAsJsonObject();
                        
                        int questionId = questionObj.get("id").getAsInt();
                        String label = questionObj.get("label").getAsString();
                        boolean required = questionObj.get("required").getAsBoolean();
                        
                        if (questionId > maxQuestionId) maxQuestionId = questionId;
                        
                        QuestionTemplate question = new QuestionTemplate(questionId, label, required);
                        template.addQuestionTemplate(question);
                    }
                }

                formTemplates.add(template);
            }
            
            if (maxTemplateId >= nextTemplateId) {
                nextTemplateId = maxTemplateId + 1;
            }
            if (maxQuestionId >= nextQuestionId) {
                nextQuestionId = maxQuestionId + 1;
            }
            saveIdCounters();
            
        } catch (IOException e) {
            System.err.println("Error loading formTemplates.json: " + e.getMessage());
        }
    }
    
    private void loadIdCounters() {
        try {
            Path projectRoot = Paths.get(System.getProperty("user.dir"));
            Path idCounterPath = projectRoot.resolve("src/database/idCounter.json");
            
            if (!idCounterPath.toFile().exists()) {
                nextTemplateId = 1;
                nextQuestionId = 1;
                nextCaseId = 1;
                saveIdCounters();
                return;
            }
            
            BufferedReader reader = new BufferedReader(new FileReader(idCounterPath.toFile()));
            JsonObject counterObj = gson.fromJson(reader, JsonObject.class);
            reader.close();
            
            if (counterObj != null) {
                if (counterObj.has("nextTemplateId")) {
                    nextTemplateId = counterObj.get("nextTemplateId").getAsInt();
                } else {
                    nextTemplateId = 1;
                }
                
                if (counterObj.has("nextQuestionId")) {
                    nextQuestionId = counterObj.get("nextQuestionId").getAsInt();
                } else {
                    nextQuestionId = 1;
                }
                
                if (counterObj.has("nextCaseId")) {
                    nextCaseId = counterObj.get("nextCaseId").getAsInt();
                } else {
                    nextCaseId = 1;
                }
            } else {
                nextTemplateId = 1;
                nextQuestionId = 1;
                nextCaseId = 1;
            }
        } catch (IOException e) {
            System.err.println("Error loading idCounter.json: " + e.getMessage());
            nextTemplateId = 1;
            nextQuestionId = 1;
            nextCaseId = 1;
        }
    }
    
    private void saveIdCounters() {
        try {
            Path projectRoot = Paths.get(System.getProperty("user.dir"));
            Path idCounterPath = projectRoot.resolve("src/database/idCounter.json");
            
            JsonObject counterObj = new JsonObject();
            counterObj.addProperty("nextTemplateId", nextTemplateId);
            counterObj.addProperty("nextQuestionId", nextQuestionId);
            counterObj.addProperty("nextCaseId", nextCaseId);
            
            BufferedWriter writer = new BufferedWriter(new FileWriter(idCounterPath.toFile()));
            gson.toJson(counterObj, writer);
            writer.close();
        } catch (IOException e) {
            System.err.println("Error writing idCounter.json: " + e.getMessage());
        }
    }
    
    public int getNextTemplateId() {
        int id = nextTemplateId++;
        saveIdCounters();
        return id;
    }
    
    public int getNextQuestionId() {
        int id = nextQuestionId++;
        saveIdCounters();
        return id;
    }
    
    public int getNextCaseId() {
        int id = nextCaseId++;
        saveIdCounters();
        return id;
    }
    
    public String generateUniqueCaseCode() {
        String code;
        int attempts = 0;
        do {
            // Generate an 8-character uppercase code from UUID
            code = UUID.randomUUID().toString().replace("-", "").substring(0, 8).toUpperCase();
            attempts++;
        } while (findCaseByCode(code) != null && attempts <= 100);
        return code;
    }
    
    public void saveCase(Case c) {
        // Add to collection if not already present
        if (!cases.contains(c)) {
            cases.add(c);
        }
        // Save case and form to JSON files
        saveCasesToJson();
        saveFormsToJson();
    }
    
    public void saveFormTemplate(FormTemplate ft) {
        // Save form template to disk (JSON persistence will be implemented later)
        // For now, just ensure it's in the collection
        if (!formTemplates.contains(ft)) {
            formTemplates.add(ft);
        }
    }
    
    public List<Case> getCases() {
        return new ArrayList<>(cases);
    }
    
    public void addCase(Case c) {
        cases.add(c);
        // Also save to JSON when adding
        saveCasesToJson();
        saveFormsToJson();
    }
    
    public Case findCaseByCode(String code) {
        for (Case c : cases) {
            if (c.getCode().equals(code)) {
                return c;
            }
        }
        return null;
    }
    
    public List<FormTemplate> getFormTemplates() {
        return new ArrayList<>(formTemplates);
    }
    
    public void addFormTemplate(FormTemplate t) {
        formTemplates.add(t);
        saveFormTemplatesToJson();
    }
    
    private void saveFormTemplatesToJson() {
        try {
            Path projectRoot = Paths.get(System.getProperty("user.dir"));
            Path formTemplatesJsonPath = projectRoot.resolve("src/database/formTemplates.json");

            // We need to preserve format of the JSON so we build manually
            JsonArray templatesArray = new JsonArray();
            
            for (FormTemplate template : formTemplates) {
                JsonObject templateObj = new JsonObject();
                templateObj.addProperty("id", template.getId());
                templateObj.addProperty("name", template.getName());
                
                JsonArray questionsArray = new JsonArray();
                for (QuestionTemplate question : template.getQuestions()) {
                    JsonObject questionObj = new JsonObject();
                    questionObj.addProperty("id", question.getId());
                    questionObj.addProperty("label", question.getLabel());
                    questionObj.addProperty("required", question.isRequired());
                    questionsArray.add(questionObj);
                }
                
                templateObj.add("questions", questionsArray);
                templatesArray.add(templateObj);
            }
            
            BufferedWriter writer = new BufferedWriter(new FileWriter(formTemplatesJsonPath.toFile()));
            gson.toJson(templatesArray, writer);
            writer.close();
        } catch (IOException e) {
            System.err.println("Error writing formTemplates.json: " + e.getMessage());
        }
    }
    
    public FormTemplate findFormTemplateById(int id) {
        for (FormTemplate ft : formTemplates) {
            if (ft.getId() == id) {
                return ft;
            }
        }
        return null;
    }
    
    public void deleteFormTemplate(int id) {
        FormTemplate template = findFormTemplateById(id);
        if (template != null) {
            formTemplates.remove(template);
            saveFormTemplatesToJson();
        }
    }
    
    public void updateFormTemplate(FormTemplate updatedTemplate) {
        FormTemplate existingTemplate = findFormTemplateById(updatedTemplate.getId());
        if (existingTemplate != null) {
            formTemplates.remove(existingTemplate);
            formTemplates.add(updatedTemplate);
            saveFormTemplatesToJson();
        }
    }
    
    private void saveCasesToJson() {
        try {
            Path projectRoot = Paths.get(System.getProperty("user.dir"));
            Path casesJsonPath = projectRoot.resolve("src/database/cases.json");

            JsonArray casesArray = new JsonArray();
            
            for (Case c : cases) {
                JsonObject caseObj = new JsonObject();
                caseObj.addProperty("id", c.getId());
                caseObj.addProperty("code", c.getCode());
                caseObj.addProperty("status", c.getStatus().name());
                caseObj.addProperty("firstName", c.getFirstName());
                caseObj.addProperty("lastName", c.getLastName());
                caseObj.addProperty("email", c.getEmail());
                caseObj.addProperty("formId", c.getForm().getId());
                
                casesArray.add(caseObj);
            }
            
            BufferedWriter writer = new BufferedWriter(new FileWriter(casesJsonPath.toFile()));
            gson.toJson(casesArray, writer);
            writer.close();
        } catch (IOException e) {
            System.err.println("Error writing cases.json: " + e.getMessage());
        }
    }
    
    private void saveFormsToJson() {
        try {
            Path projectRoot = Paths.get(System.getProperty("user.dir"));
            Path formsJsonPath = projectRoot.resolve("src/database/forms.json");

            JsonArray formsArray = new JsonArray();
            
            for (Case c : cases) {
                Form form = c.getForm();
                JsonObject formObj = new JsonObject();
                formObj.addProperty("id", form.getId());
                
                // Add all questions
                JsonArray questionsArray = new JsonArray();
                for (QuestionInstance question : form.getQuestions()) {
                    JsonObject questionObj = new JsonObject();
                    questionObj.addProperty("id", question.getId());
                    questionObj.addProperty("label", question.getLabel());
                    questionObj.addProperty("required", question.isRequired());
                    if (question.getAnswerValue() != null) {
                        questionObj.addProperty("answer", question.getAnswerValue());
                    }
                    questionsArray.add(questionObj);
                }
                formObj.add("questions", questionsArray);
                
                // Add most recent questions (by ID reference)
                JsonArray mostRecentQuestionsArray = new JsonArray();
                for (QuestionInstance question : form.getMostRecentQuestions()) {
                    mostRecentQuestionsArray.add(question.getId());
                }
                formObj.add("mostRecentQuestions", mostRecentQuestionsArray);
                
                formsArray.add(formObj);
            }
            
            BufferedWriter writer = new BufferedWriter(new FileWriter(formsJsonPath.toFile()));
            gson.toJson(formsArray, writer);
            writer.close();
        } catch (IOException e) {
            System.err.println("Error writing forms.json: " + e.getMessage());
        }
    }
    
    private void loadCasesFromJson() {
        try {
            Path projectRoot = Paths.get(System.getProperty("user.dir"));
            Path casesJsonPath = projectRoot.resolve("src/database/cases.json");
            
            if (!casesJsonPath.toFile().exists()) {
                return;
            }
            
            BufferedReader reader = new BufferedReader(new FileReader(casesJsonPath.toFile()));
            JsonArray casesArray = gson.fromJson(reader, JsonArray.class);
            reader.close();
            
            if (casesArray == null || casesArray.size() == 0) {
                return;
            }
            
            int maxCaseId = 0;
            
            for (JsonElement element : casesArray) {
                JsonObject caseObj = element.getAsJsonObject();
                
                int caseId = caseObj.get("id").getAsInt();
                String code = caseObj.get("code").getAsString();
                String firstName = caseObj.get("firstName").getAsString();
                String lastName = caseObj.get("lastName").getAsString();
                String email = caseObj.get("email").getAsString();
                CaseStatus status = CaseStatus.valueOf(caseObj.get("status").getAsString());
                // formId is stored in JSON but form will be linked in loadFormsFromJson by matching caseId
                
                if (caseId > maxCaseId) maxCaseId = caseId;
                
                Case c = new Case(caseId, code, firstName, lastName, email);
                c.setStatus(status);
                
                // Form will be loaded separately and linked
                cases.add(c);
            }
            
            if (maxCaseId >= nextCaseId) {
                nextCaseId = maxCaseId + 1;
            }
            saveIdCounters();
            
        } catch (IOException e) {
            System.err.println("Error loading cases.json: " + e.getMessage());
        }
        System.out.println("Successfully loaded cases.");
    }
    
    private void loadFormsFromJson() {
        try {
            Path projectRoot = Paths.get(System.getProperty("user.dir"));
            Path formsJsonPath = projectRoot.resolve("src/database/forms.json");
            
            if (!formsJsonPath.toFile().exists()) {
                return;
            }
            
            BufferedReader reader = new BufferedReader(new FileReader(formsJsonPath.toFile()));
            JsonArray formsArray = gson.fromJson(reader, JsonArray.class);
            reader.close();
            
            if (formsArray == null || formsArray.size() == 0) {
                return;
            }
            
            for (JsonElement element : formsArray) {
                JsonObject formObj = element.getAsJsonObject();
                
                int formId = formObj.get("id").getAsInt();
                
                // Find the case with this form ID
                Case matchingCase = null;
                for (Case c : cases) {
                    if (c.getId() == formId) {
                        matchingCase = c;
                        break;
                    }
                }
                
                if (matchingCase == null) {
                    continue; // Skip if no matching case found
                }
                
                // Create form
                Form form = new Form(formId);
                
                // Load questions
                JsonArray questionsArray = formObj.getAsJsonArray("questions");
                if (questionsArray != null) {
                    for (JsonElement questionElement : questionsArray) {
                        JsonObject questionObj = questionElement.getAsJsonObject();
                        
                        int questionId = questionObj.get("id").getAsInt();
                        String label = questionObj.get("label").getAsString();
                        boolean required = questionObj.get("required").getAsBoolean();
                        String answer = questionObj.has("answer") ? questionObj.get("answer").getAsString() : null;
                        
                        QuestionInstance question = new QuestionInstance(questionId, label, required);
                        if (answer != null) {
                            question.setAnswerValue(answer);
                        }
                        form.getQuestions().add(question);
                    }
                }
                
                // Load most recent questions (by ID reference)
                JsonArray mostRecentQuestionsArray = formObj.getAsJsonArray("mostRecentQuestions");
                if (mostRecentQuestionsArray != null) {
                    for (JsonElement questionIdElement : mostRecentQuestionsArray) {
                        int questionId = questionIdElement.getAsInt();
                        // Find the question by ID
                        for (QuestionInstance q : form.getQuestions()) {
                            if (q.getId() == questionId) {
                                form.getMostRecentQuestions().add(q);
                                break;
                            }
                        }
                    }
                }
                
                // Set the form on the case
                matchingCase.setForm(form);
            }
            
        } catch (IOException e) {
            System.err.println("Error loading forms.json: " + e.getMessage());
        }
    }
    
    public boolean checkCredentials(String username, String password) {
        try {
            Path projectRoot = Paths.get(System.getProperty("user.dir"));
            Path adminJsonPath = projectRoot.resolve("src/database/admin.json");
            
            BufferedReader reader = new BufferedReader(new FileReader(adminJsonPath.toFile()));
            JsonArray adminArray = gson.fromJson(reader, JsonArray.class);
            reader.close();
            
            if (adminArray == null) {
                return false;
            }
            
            for (JsonElement element : adminArray) {
                JsonObject adminObj = element.getAsJsonObject();
                String foundUsername = adminObj.get("username").getAsString();
                String foundPassword = adminObj.get("password").getAsString();
                
                if (foundUsername.equals(username) && foundPassword.equals(password)) {
                    return true;
                }
            }
            
            return false;
        } catch (IOException e) {
            System.err.println("Error reading admin.json: " + e.getMessage());
            return false;
        }
    }
}

