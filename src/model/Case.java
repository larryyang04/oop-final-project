package model;

public class Case {
    private int id;
    private String code;
    private CaseStatus status;
    private String firstName;
    private String lastName;
    private String email;
    private Form form;
    
    public Case(int id, String code, String firstName, String lastName, String email) {
        this.id = id;
        this.code = code;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.status = CaseStatus.SENT;
        this.form = new Form(id);
    }
    
    public int getId() {
        return id;
    }
    
    public String getCode() {
        return code;
    }
    
    public CaseStatus getStatus() {
        return status;
    }
    
    public void setStatus(CaseStatus status) {
        this.status = status;
    }
    
    public String getFirstName() {
        return firstName;
    }
    
    public String getLastName() {
        return lastName;
    }
    
    public String getEmail() {
        return email;
    }
    
    public String getCustomerDisplayName() {
        return firstName + " " + lastName;
    }
    
    public Form getForm() {
        return form;
    }
    
    public void setForm(Form form) {
        this.form = form;
    }
    
    public boolean isSent() {
        return status == CaseStatus.SENT;
    }
}

