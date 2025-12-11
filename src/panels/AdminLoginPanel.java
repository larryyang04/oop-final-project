package panels;

import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.FlowLayout;
import app.AppController;

public class AdminLoginPanel extends JPanel {
    private AppController controller;
    
    public AdminLoginPanel(AppController controller) {
        this.controller = controller;
        setLayout(new FlowLayout());
        add(new JLabel("Admin Login Panel - Routing works!"));
    }
}

