package model;

import java.util.ArrayList;
import java.util.List;

public class FormTemplate {
    private int id;
    private String name;
    private List<QuestionTemplate> questions;
    
    public FormTemplate(int id, String name) {
        this.id = id;
        this.name = name;
        this.questions = new ArrayList<>();
    }
    
    public int getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
    
    public List<QuestionTemplate> getQuestions() {
        return questions;
    }
    
    public void addQuestionTemplate(QuestionTemplate question) {
        questions.add(question);
    }
    
    public QuestionTemplate createQuestionTemplate(int id, String label, boolean required) {
        QuestionTemplate question = new QuestionTemplate(id, label, required);
        addQuestionTemplate(question);
        return question;
    }
}

