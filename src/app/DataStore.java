package app;

public class DataStore {
    private static DataStore instance;
    
    private DataStore() {
        // Private constructor to enforce singleton pattern
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
}

