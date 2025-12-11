package model;

public class QuestionInstance {
    private int id;
    private String label;
    private boolean required;
    private String answer;
    
    public QuestionInstance(int id, String label, boolean required) {
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
    
    public String getAnswerValue() {
        return answer;
    }
    
    public void setAnswerValue(String value) {
        this.answer = value;
    }
    
    public void clearAnswer() {
        this.answer = null;
    }
    
    public boolean isAnswered() {
        return answer != null && !answer.trim().isEmpty();
    }
}

