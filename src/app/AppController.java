package app;

public class AppController {
    private DataStore dataStore;
    private MainWindow mainWindow;
    
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
    
    public DataStore getDataStore() {
        return dataStore;
    }
}

