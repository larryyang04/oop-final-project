package panels;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import app.AppController;

public class CustomerLoginPanel extends JPanel {
    private AppController controller;
    
    public CustomerLoginPanel(AppController controller) {
        this.controller = controller;
        setLayout(new BorderLayout());
        
        JPanel backButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        backButtonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JButton backButton = new JButton("← Back");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.showRoleSelection();
            }
        });
        backButtonPanel.add(backButton);
        
        JPanel contentPanel = new JPanel(new FlowLayout());
        contentPanel.add(new JLabel("Customer Login Panel - Routing works!"));
        
        add(backButtonPanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);
    }
}

