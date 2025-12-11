package model;

public class QuestionTemplate {
    private int id;
    private String label;
    private boolean required;
    
    public QuestionTemplate(int id, String label, boolean required) {
        this.id = id;
        this.label = label;
        this.required = required;
    }
    
    public int getId() {
        return id;
    }
    
    public String getLabel() {
        return label;
    }
    
    public boolean isRequired() {
        return required;
    }
}

