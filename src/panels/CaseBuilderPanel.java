package panels;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import app.AppController;
import model.FormTemplate;

public class CaseBuilderPanel extends JPanel {
    private AppController controller;
    private JTextField firstNameField;
    private JTextField lastNameField;
    private JTextField emailField;
    private JComboBox<String> templateComboBox;
    private DefaultComboBoxModel<String> templateComboBoxModel;
    private List<FormTemplate> availableTemplates;
    
    public CaseBuilderPanel(AppController controller) {
        this.controller = controller;
        
        setLayout(new BorderLayout());
        
        // Back button panel
        JPanel backButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        backButtonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JButton backButton = new JButton("← Back to Dashboard");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.showAdminDashboard();
            }
        });
        backButtonPanel.add(backButton);
        
        // Main content panel
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Title
        JLabel titleLabel = new JLabel("Create New Case");
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 20f));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(titleLabel);
        contentPanel.add(Box.createVerticalStrut(20));
        
        // Customer Information Section
        JLabel customerInfoLabel = new JLabel("Customer Information:");
        customerInfoLabel.setFont(customerInfoLabel.getFont().deriveFont(Font.BOLD, 14f));
        customerInfoLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(customerInfoLabel);
        contentPanel.add(Box.createVerticalStrut(15));
        
        // First Name
        JLabel firstNameLabel = new JLabel("First Name:");
        firstNameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(firstNameLabel);
        
        firstNameField = new JTextField(30);
        firstNameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        firstNameField.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(firstNameField);
        contentPanel.add(Box.createVerticalStrut(15));
        
        // Last Name
        JLabel lastNameLabel = new JLabel("Last Name:");
        lastNameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(lastNameLabel);
        
        lastNameField = new JTextField(30);
        lastNameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        lastNameField.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(lastNameField);
        contentPanel.add(Box.createVerticalStrut(15));
        
        // Email
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(emailLabel);
        
        emailField = new JTextField(30);
        emailField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        emailField.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(emailField);
        contentPanel.add(Box.createVerticalStrut(30));
        
        // Form Template Selection Section
        JLabel templateLabel = new JLabel("Form Template:");
        templateLabel.setFont(templateLabel.getFont().deriveFont(Font.BOLD, 14f));
        templateLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(templateLabel);
        contentPanel.add(Box.createVerticalStrut(10));
        
        templateComboBoxModel = new DefaultComboBoxModel<>();
        templateComboBox = new JComboBox<>(templateComboBoxModel);
        templateComboBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        templateComboBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(templateComboBox);
        contentPanel.add(Box.createVerticalStrut(30));
        
        // Open Case button
        JButton openCaseButton = new JButton("Open Case");
        openCaseButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        openCaseButton.setFont(openCaseButton.getFont().deriveFont(Font.BOLD, 14f));
        openCaseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openCase();
            }
        });
        contentPanel.add(openCaseButton);
        
        // Scrollable wrapper
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(null);
        
        add(backButtonPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        
        // Load templates
        loadTemplates();
    }
    
    private void loadTemplates() {
        templateComboBoxModel.removeAllElements();
        if (controller != null && controller.getDataStore() != null) {
            availableTemplates = controller.getDataStore().getFormTemplates();
            if (availableTemplates.isEmpty()) {
                templateComboBoxModel.addElement("No templates available");
                templateComboBox.setEnabled(false);
            } else {
                for (FormTemplate template : availableTemplates) {
                    templateComboBoxModel.addElement(template.getName());
                }
                templateComboBox.setEnabled(true);
            }
        } else {
            templateComboBoxModel.addElement("DataStore not available");
            templateComboBox.setEnabled(false);
            availableTemplates = null;
        }
    }
    
    public void refreshTemplates() {
        loadTemplates();
    }
    
    private void openCase() {
        // Validate inputs
        String firstName = firstNameField.getText().trim();
        String lastName = lastNameField.getText().trim();
        String email = emailField.getText().trim();
        
        if (firstName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a first name.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (lastName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a last name.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter an email address.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Basic email validation
        if (!email.contains("@") || !email.contains(".")) {
            JOptionPane.showMessageDialog(this, "Please enter a valid email address.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Validate template selection
        int selectedIndex = templateComboBox.getSelectedIndex();
        if (selectedIndex < 0 || availableTemplates == null || selectedIndex >= availableTemplates.size()) {
            JOptionPane.showMessageDialog(this, "Please select a form template.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        FormTemplate selectedTemplate = availableTemplates.get(selectedIndex);
        
        // Generate case ID and code
        int caseId = controller.getDataStore().getNextCaseId();
        String caseCode = controller.getDataStore().generateUniqueCaseCode();
        
        // Create case
        model.Case newCase = new model.Case(caseId, caseCode, firstName, lastName, email);
        
        // Initialize form with the selected template and set it on the case
        model.Form form = new model.Form(caseId, selectedTemplate);
        newCase.setForm(form);
        
        // Save case to DataStore (this will also save to JSON)
        controller.getDataStore().saveCase(newCase);
        
        JOptionPane.showMessageDialog(
            this, 
            "Case created successfully!\nCase Code: " + caseCode + "\nCase ID: " + caseId, 
            "Success", 
            JOptionPane.INFORMATION_MESSAGE
        );
        
        // Reset form
        resetForm();
        
        // Navigate back to dashboard
        controller.showAdminDashboard();
    }
    
    private void resetForm() {
        firstNameField.setText("");
        lastNameField.setText("");
        emailField.setText("");
        templateComboBox.setSelectedIndex(-1);
    }
}

