package model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Form {
    private int id;
    private List<QuestionInstance> questions;
    private Set<QuestionInstance> mostRecentQuestions;
    
    public Form(int id) {
        this.id = id;
        this.questions = new ArrayList<>();
        this.mostRecentQuestions = new HashSet<>();
    }
    
    public Form(int id, FormTemplate formTemplate) {
        this(id);
        // Create QuestionInstances from QuestionTemplates
        // Add all questions to mostRecentQuestions (they're all new on creation)
        for (QuestionTemplate template : formTemplate.getQuestions()) {
            int questionId = questions.size() + 1;
            QuestionInstance question = new QuestionInstance(questionId, template.getLabel(), template.isRequired());
            questions.add(question);
            mostRecentQuestions.add(question);
        }
    }
    
    public int getId() {
        return id;
    }
    
    public QuestionInstance addQuestion(String label, boolean required) {
        int questionId = questions.size() + 1; // Simple ID generation
        QuestionInstance question = new QuestionInstance(questionId, label, required);
        questions.add(question);
        mostRecentQuestions.clear();
        mostRecentQuestions.add(question);
        return question;
    }
    
    public QuestionInstance findQuestionById(int questionId) {
        for (QuestionInstance question : questions) {
            if (question.getId() == questionId) {
                return question;
            }
        }
        return null;
    }
    
    public List<QuestionInstance> getQuestions() {
        return questions;
    }
    
    public Set<QuestionInstance> getMostRecentQuestions() {
        return mostRecentQuestions;
    }
    
    public void addMultipleQuestions(List<QuestionInstance> newQuestions) {
        // Clear most recent questions
        mostRecentQuestions.clear();
        // Add all new questions to the form and mark them as most recent
        for (QuestionInstance question : newQuestions) {
            questions.add(question);
            mostRecentQuestions.add(question);
        }
    }
}

