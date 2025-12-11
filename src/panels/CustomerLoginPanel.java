package panels;

import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.FlowLayout;
import app.AppController;

public class CustomerLoginPanel extends JPanel {
    private AppController controller;
    
    public CustomerLoginPanel(AppController controller) {
        this.controller = controller;
        setLayout(new FlowLayout());
        add(new JLabel("Customer Login Panel - Routing works!"));
    }
}

