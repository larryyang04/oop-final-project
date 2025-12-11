package panels;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import app.AppController;

public class CustomerLoginPanel extends JPanel {
    private AppController controller;
    private JTextField firstNameField;
    private JTextField lastNameField;
    private JTextField caseCodeField;
    private JLabel errorLabel;
    
    public CustomerLoginPanel(AppController controller) {
        this.controller = controller;
        setLayout(new BorderLayout());
        
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
        
        JLabel titleLabel = new JLabel("Customer Login");
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 18f));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        formPanel.add(titleLabel);
        formPanel.add(Box.createVerticalStrut(30));
        
        JLabel firstNameLabel = new JLabel("First Name:");
        firstNameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(firstNameLabel);
        
        firstNameField = new JTextField(20);
        firstNameField.setMaximumSize(new Dimension(300, 30));
        firstNameField.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(firstNameField);
        formPanel.add(Box.createVerticalStrut(15));
        
        JLabel lastNameLabel = new JLabel("Last Name:");
        lastNameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(lastNameLabel);
        
        lastNameField = new JTextField(20);
        lastNameField.setMaximumSize(new Dimension(300, 30));
        lastNameField.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(lastNameField);
        formPanel.add(Box.createVerticalStrut(15));
        
        JLabel caseCodeLabel = new JLabel("Case Code:");
        caseCodeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(caseCodeLabel);
        
        caseCodeField = new JTextField(20);
        caseCodeField.setMaximumSize(new Dimension(300, 30));
        caseCodeField.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(caseCodeField);
        formPanel.add(Box.createVerticalStrut(20));
        
        JButton loginButton = new JButton("Login");
        loginButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String firstName = firstNameField.getText().trim();
                String lastName = lastNameField.getText().trim();
                String caseCode = caseCodeField.getText().trim();
                
                if (firstName.isEmpty() || lastName.isEmpty() || caseCode.isEmpty()) {
                    showError("Please enter all fields");
                } else {
                    boolean success = controller.handleCustomerLogin(firstName, lastName, caseCode);
                    if (!success) {
                        showError("Invalid credentials. Please check your information and try again.");
                    }
                }
            }
        });
        formPanel.add(loginButton);
        formPanel.add(Box.createVerticalStrut(10));
        
        errorLabel = new JLabel(" ");
        errorLabel.setForeground(Color.RED);
        errorLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(errorLabel);
        
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.add(formPanel);
        
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
        
        add(backButtonPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
    }
    
    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setForeground(Color.RED);
    }
    
    public void clearError() {
        errorLabel.setText(" ");
    }
}

