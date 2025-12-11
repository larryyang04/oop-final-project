package app;

public class AppController {
    private DataStore dataStore;
    private MainWindow mainWindow;
    private model.Case currentCustomerCase;
    
    public AppController() {
        this.mainWindow = new MainWindow(this);
    }

    public void start() {
        this.dataStore = DataStore.getInstance();
        this.dataStore.loadFromDisk();
    }
    
    public void showRoleSelection() {
        mainWindow.showRoleSelection();
    }
    
    public void showAdminLogin() {
        mainWindow.showAdminLogin();
    }
    
    public void showCustomerLogin() {
        mainWindow.showCustomerLogin();
    }
    
    public boolean handleAdminLogin(String username, String password) {
        if (dataStore.checkCredentials(username, password)) {
            mainWindow.showAdminDashboard();
            return true;
        } else {
            return false;
        }
    }
    
    public boolean handleCustomerLogin(String firstName, String lastName, String code) {
        model.Case foundCase = dataStore.findCaseByCredentials(firstName, lastName, code);
        if (foundCase != null) {
            this.currentCustomerCase = foundCase;
            mainWindow.showCustomerForm();
            return true;
        } else {
            return false;
        }
    }
    
    public model.Case getCurrentCustomerCase() {
        return currentCustomerCase;
    }
    
    public DataStore getDataStore() {
        return dataStore;
    }
    
    public void showFormBuilder() {
        mainWindow.showFormBuilder();
    }
    
    public void showFormBuilderWithTemplate(model.FormTemplate template) {
        mainWindow.showFormBuilderWithTemplate(template);
    }
    
    public void showAdminDashboard() {
        mainWindow.showAdminDashboard();
    }
    
    public void showAdminDashboardWithTemplates() {
        mainWindow.showAdminDashboard();
        mainWindow.getAdminDashboardPanel().showTemplatesViewAndRefresh();
    }
    
    public void showAdminDashboardAndRefreshCase(model.Case caseObj) {
        mainWindow.showAdminDashboard();
        mainWindow.getAdminDashboardPanel().refreshAndShowCaseDetail(caseObj);
    }
    
    public void showCaseBuilder() {
        mainWindow.showCaseBuilder();
    }
    
    public void showAddQuestionsToForm(model.Case caseObj) {
        mainWindow.showAddQuestionsToForm(caseObj);
    }
}

