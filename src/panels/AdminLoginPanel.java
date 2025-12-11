package panels;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import app.AppController;

public class AdminLoginPanel extends JPanel {
    private AppController controller;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JLabel errorLabel;
    
    public AdminLoginPanel(AppController controller) {
        this.controller = controller;
        setLayout(new BorderLayout());
        
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
        
        JLabel titleLabel = new JLabel("Admin Login");
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 18f));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        formPanel.add(titleLabel);
        formPanel.add(Box.createVerticalStrut(30));
        
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(usernameLabel);
        
        usernameField = new JTextField(20);
        usernameField.setMaximumSize(new Dimension(300, 30));
        usernameField.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(usernameField);
        formPanel.add(Box.createVerticalStrut(15));
        
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(passwordLabel);
        
        passwordField = new JPasswordField(20);
        passwordField.setMaximumSize(new Dimension(300, 30));
        passwordField.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(passwordField);
        formPanel.add(Box.createVerticalStrut(20));
        
        JButton loginButton = new JButton("Login");
        loginButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                
                if (username.isEmpty() || password.isEmpty()) {
                    showError("Please enter both username and password");
                } else {
                    boolean success = controller.handleAdminLogin(username, password);
                    if (!success) {
                        showError("Something is wrong - Invalid username or password");
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

