package app;

import panels.*;

public class AppController {
    private DataStore dataStore;
    private MainWindow mainWindow;
    
    public AppController() {
        this.mainWindow = new MainWindow(this);
    }
    
    public void start() {
        // Initialize DataStore singleton
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
}

