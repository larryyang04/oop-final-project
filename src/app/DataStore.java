package app;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import model.Case;
import model.FormTemplate;

public class DataStore {
    private static DataStore instance;
    private ArrayList<Case> cases;
    private ArrayList<FormTemplate> formTemplates;
    
    private DataStore() {
        // Private constructor to enforce singleton pattern
        this.cases = new ArrayList<>();
        this.formTemplates = new ArrayList<>();
    }
    
    public static DataStore getInstance() {
        if (instance == null) { 
            instance = new DataStore();
        }
        return instance;
    }
    
    public void loadFromDisk() {
        // TODO: Implement JSON loading
    }
    
    public void saveCase(Case c) {
        // Save case to disk (JSON persistence will be implemented later)
        // For now, just ensure it's in the collection
        if (!cases.contains(c)) {
            cases.add(c);
        }
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
    }
    
    public FormTemplate findFormTemplateById(int id) {
        for (FormTemplate ft : formTemplates) {
            if (ft.getId() == id) {
                return ft;
            }
        }
        return null;
    }
    
    public boolean checkCredentials(String username, String password) {
        try {
            // Get the path to admin.json relative to the project root
            Path projectRoot = Paths.get(System.getProperty("user.dir"));
            Path adminJsonPath = projectRoot.resolve("src/database/admin.json");
            
            // Read the file
            BufferedReader reader = new BufferedReader(new FileReader(adminJsonPath.toFile()));
            StringBuilder jsonContent = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonContent.append(line).append("\n");
            }
            reader.close();
            
            String json = jsonContent.toString();
            
            // Simple JSON parsing for the structure: [{"username":"...","password":"..."}]
            // Extract all username-password pairs
            int index = 0;
            while (index < json.length()) {
                int usernameStart = json.indexOf("\"username\"", index);
                if (usernameStart == -1) break;
                
                int usernameValueStart = json.indexOf("\"", usernameStart + 10) + 1;
                int usernameValueEnd = json.indexOf("\"", usernameValueStart);
                String foundUsername = json.substring(usernameValueStart, usernameValueEnd);
                
                int passwordStart = json.indexOf("\"password\"", usernameValueEnd);
                int passwordValueStart = json.indexOf("\"", passwordStart + 10) + 1;
                int passwordValueEnd = json.indexOf("\"", passwordValueStart);
                String foundPassword = json.substring(passwordValueStart, passwordValueEnd);
                
                if (foundUsername.equals(username) && foundPassword.equals(password)) {
                    return true;
                }
                
                index = passwordValueEnd;
            }
            
            return false;
        } catch (IOException e) {
            System.err.println("Error reading admin.json: " + e.getMessage());
            return false;
        }
    }
}

