package panels;

import javax.swing.JPanel;
import javax.swing.JButton;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import app.AppController;

public class RoleSelectionPanel extends JPanel {
    private AppController controller;
    private JButton adminButton;
    private JButton customerButton;
    
    public RoleSelectionPanel(AppController controller) {
        this.controller = controller;

        setLayout(new FlowLayout());
        
        this.adminButton = new JButton("Admin");
        adminButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.showAdminLogin();
            }
        });
        
        this.customerButton = new JButton("Customer");
        customerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.showCustomerLogin();
            }
        });
        
        add(adminButton);
        add(customerButton);
    }
}

