package panels;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import app.AppController;
import model.FormTemplate;
import model.QuestionTemplate;

public class FormBuilderPanel extends JPanel {
    private AppController controller;
    private JTextField templateNameField;
    private JTextField questionField;
    private JCheckBox requiredCheckBox;
    private DefaultListModel<String> questionListModel;
    private JList<String> questionList;
    private List<QuestionTemplate> questionTemplates;
    private int nextQuestionId;
    private boolean isEditMode;
    private int editingTemplateId;
    private java.util.Set<Integer> originalQuestionIds;
    private JLabel titleLabel;
    private JButton saveButton;
    private JButton deleteQuestionButton;
    
    public FormBuilderPanel(AppController controller) {
        this.controller = controller;
        this.questionTemplates = new ArrayList<>();
        this.nextQuestionId = 1;
        this.isEditMode = false;
        this.editingTemplateId = -1;
        
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
        titleLabel = new JLabel("Create Form Template");
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 20f));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(titleLabel);
        contentPanel.add(Box.createVerticalStrut(20));
        
        // Template name section
        JLabel nameLabel = new JLabel("Template Name:");
        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(nameLabel);
        
        templateNameField = new JTextField(30);
        templateNameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        templateNameField.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(templateNameField);
        contentPanel.add(Box.createVerticalStrut(20));
        
        // Questions preview section
        JLabel previewLabel = new JLabel("Questions Preview:");
        previewLabel.setFont(previewLabel.getFont().deriveFont(Font.BOLD, 14f));
        previewLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(previewLabel);
        contentPanel.add(Box.createVerticalStrut(10));
        
        questionListModel = new DefaultListModel<>();
        questionList = new JList<>(questionListModel);
        questionList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        questionList.setVisibleRowCount(8);
        
        // Add selection listener to show/hide delete button
        questionList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int selectedIndex = questionList.getSelectedIndex();
                    deleteQuestionButton.setEnabled(selectedIndex >= 0);
                }
            }
        });
        
        JScrollPane listScrollPane = new JScrollPane(questionList);
        listScrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
        listScrollPane.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));
        contentPanel.add(listScrollPane);
        contentPanel.add(Box.createVerticalStrut(10));
        
        // Delete question button (initially disabled)
        deleteQuestionButton = new JButton("Delete Selected Question");
        deleteQuestionButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        deleteQuestionButton.setEnabled(false);
        deleteQuestionButton.setForeground(Color.RED);
        deleteQuestionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteSelectedQuestion();
            }
        });
        contentPanel.add(deleteQuestionButton);
        contentPanel.add(Box.createVerticalStrut(20));
        
        // Add question section
        JLabel addQuestionLabel = new JLabel("Add Question:");
        addQuestionLabel.setFont(addQuestionLabel.getFont().deriveFont(Font.BOLD, 14f));
        addQuestionLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(addQuestionLabel);
        contentPanel.add(Box.createVerticalStrut(10));
        
        JLabel questionTextLabel = new JLabel("Question Text:");
        questionTextLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(questionTextLabel);
        
        questionField = new JTextField(30);
        questionField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        questionField.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(questionField);
        contentPanel.add(Box.createVerticalStrut(10));
        
        requiredCheckBox = new JCheckBox("Required");
        requiredCheckBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(requiredCheckBox);
        contentPanel.add(Box.createVerticalStrut(15));
        
        // Add button
        JButton addButton = new JButton("Add Question");
        addButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addQuestion();
            }
        });
        contentPanel.add(addButton);
        contentPanel.add(Box.createVerticalStrut(30));
        
        // Save button
        saveButton = new JButton("Save Template");
        saveButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        saveButton.setFont(saveButton.getFont().deriveFont(Font.BOLD, 14f));
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveTemplate();
            }
        });
        contentPanel.add(saveButton);
        
        // Scrollable wrapper
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(null);
        
        add(backButtonPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }
    
    private void addQuestion() {
        String questionText = questionField.getText().trim();
        if (questionText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a question text.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        boolean required = requiredCheckBox.isSelected();
        QuestionTemplate question = new QuestionTemplate(nextQuestionId++, questionText, required);
        questionTemplates.add(question);
        
        // Update preview list
        String displayText = questionText + (required ? " *" : "");
        questionListModel.addElement(displayText);
        
        // Clear input fields
        questionField.setText("");
        requiredCheckBox.setSelected(false);
        
        // Disable delete button if no selection
        deleteQuestionButton.setEnabled(false);
    }
    
    private void deleteSelectedQuestion() {
        int selectedIndex = questionList.getSelectedIndex();
        if (selectedIndex < 0 || selectedIndex >= questionTemplates.size()) {
            return;
        }
        
        // Get the question to delete
        QuestionTemplate questionToDelete = questionTemplates.get(selectedIndex);
        
        // Remove from originalQuestionIds if it exists (for edit mode)
        if (originalQuestionIds != null) {
            originalQuestionIds.remove(questionToDelete.getId());
        }
        
        // Remove from questionTemplates list
        questionTemplates.remove(selectedIndex);
        
        // Remove from list model
        questionListModel.remove(selectedIndex);
        
        // Clear selection and disable delete button
        questionList.clearSelection();
        deleteQuestionButton.setEnabled(false);
    }
    
    private void saveTemplate() {
        String templateName = templateNameField.getText().trim();
        if (templateName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a template name.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (questionTemplates.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please add at least one question to the template.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        FormTemplate template;
        
        if (isEditMode) {
            // Update existing template
            template = new FormTemplate(editingTemplateId, templateName);
            
            // Add all questions to template (preserve existing question IDs, generate new ones for new questions)
            for (QuestionTemplate question : questionTemplates) {
                int questionId;
                if (originalQuestionIds != null && originalQuestionIds.contains(question.getId())) {
                    // Preserve existing question ID from original template
                    questionId = question.getId();
                } else {
                    // Generate new question ID for newly added questions
                    questionId = controller.getDataStore().getNextQuestionId();
                }
                QuestionTemplate newQuestion = new QuestionTemplate(
                    questionId,
                    question.getLabel(),
                    question.isRequired()
                );
                template.addQuestionTemplate(newQuestion);
            }
            
            // Update existing template in DataStore
            controller.getDataStore().updateFormTemplate(template);
            JOptionPane.showMessageDialog(this, "Template updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            // Create new template
            int templateId = controller.getDataStore().getNextTemplateId();
            template = new FormTemplate(templateId, templateName);
            
            // Add all questions to template (with auto-generated IDs)
            for (QuestionTemplate question : questionTemplates) {
                int questionId = controller.getDataStore().getNextQuestionId();
                QuestionTemplate newQuestion = new QuestionTemplate(
                    questionId,
                    question.getLabel(),
                    question.isRequired()
                );
                template.addQuestionTemplate(newQuestion);
            }
            
            // Save to DataStore (this will also write to JSON)
            controller.getDataStore().addFormTemplate(template);
            JOptionPane.showMessageDialog(this, "Template saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        }
        
        // Reset form
        resetForm();
        
        // Navigate back to templates management view
        controller.showAdminDashboardWithTemplates();
    }
    
    private void resetForm() {
        templateNameField.setText("");
        questionField.setText("");
        requiredCheckBox.setSelected(false);
        questionListModel.clear();
        questionTemplates.clear();
        nextQuestionId = 1;
        isEditMode = false;
        editingTemplateId = -1;
        originalQuestionIds = null;
        titleLabel.setText("Create Form Template");
        saveButton.setText("Save Template");
        questionList.clearSelection();
        deleteQuestionButton.setEnabled(false);
    }
    
    public void loadTemplateForEditing(FormTemplate template) {
        // Reset form first
        resetForm();
        
        // Set edit mode
        isEditMode = true;
        editingTemplateId = template.getId();
        
        // Track original question IDs to preserve them when saving
        originalQuestionIds = new HashSet<>();
        
        // Update UI
        titleLabel.setText("Edit Form Template");
        saveButton.setText("Update Template");
        
        // Load template name
        templateNameField.setText(template.getName());
        
        // Load questions and track their original IDs
        for (QuestionTemplate question : template.getQuestions()) {
            questionTemplates.add(question);
            originalQuestionIds.add(question.getId());
            
            // Update preview list
            String displayText = question.getLabel() + (question.isRequired() ? " *" : "");
            questionListModel.addElement(displayText);
        }
        
        // Update nextQuestionId to be higher than any existing question ID
        int maxId = 0;
        for (QuestionTemplate q : questionTemplates) {
            if (q.getId() > maxId) {
                maxId = q.getId();
            }
        }
        nextQuestionId = maxId + 1;
    }
    
    public void renderFilledForm(model.Form form) {
        // This method is kept for backward compatibility but not used in template builder
        // Could be used for preview functionality later
    }
}

