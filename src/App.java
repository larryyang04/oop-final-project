import app.AppController;

public class App {
    public static void main(String[] args) throws Exception {
        AppController controller = new AppController();
        controller.start();
        controller.showRoleSelection();
    }
}
