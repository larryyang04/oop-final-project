package panels;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import app.AppController;
import model.Case;
import model.Form;
import model.QuestionInstance;
import model.CaseStatus;

public class CustomerFormPanel extends JPanel {
    private AppController controller;
    private JPanel questionsPanel;
    private JButton saveButton;
    private JLabel caseInfoLabel;
    private Map<QuestionInstance, JTextField> answerFields;
    
    public CustomerFormPanel(AppController controller) {
        this.controller = controller;
        this.answerFields = new HashMap<>();
        setLayout(new BorderLayout());
        
        // Back button panel
        JPanel backButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        backButtonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JButton backButton = new JButton("← Back to Login");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.showCustomerLogin();
            }
        });
        backButtonPanel.add(backButton);
        
        // Main content panel
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Header section
        JLabel titleLabel = new JLabel("Customer Form");
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 20f));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(titleLabel);
        contentPanel.add(Box.createVerticalStrut(10));
        
        // Case info will be populated when case is loaded
        caseInfoLabel = new JLabel();
        caseInfoLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(caseInfoLabel);
        contentPanel.add(Box.createVerticalStrut(20));
        
        // Questions section header
        JLabel questionsLabel = new JLabel("Please answer the following questions:");
        questionsLabel.setFont(questionsLabel.getFont().deriveFont(Font.BOLD, 14f));
        questionsLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(questionsLabel);
        contentPanel.add(Box.createVerticalStrut(15));
        
        // Questions panel (will be populated dynamically)
        questionsPanel = new JPanel();
        questionsPanel.setLayout(new BoxLayout(questionsPanel, BoxLayout.Y_AXIS));
        questionsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(questionsPanel);
        contentPanel.add(Box.createVerticalStrut(20));
        
        // Save button
        saveButton = new JButton("Save Answers");
        saveButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        saveButton.setFont(saveButton.getFont().deriveFont(Font.BOLD, 14f));
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveAnswers();
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
    
    public void loadCase() {
        Case currentCase = controller.getCurrentCustomerCase();
        if (currentCase == null) {
            return;
        }
        
        // Clear existing questions
        questionsPanel.removeAll();
        answerFields.clear();
        
        // Update case info
        caseInfoLabel.setText("Case Code: " + currentCase.getCode() + " | Customer: " + 
            currentCase.getFirstName() + " " + currentCase.getLastName());
        
        // Get form from case
        Form form = currentCase.getForm();
        if (form == null || form.getQuestions() == null) {
            JLabel noQuestionsLabel = new JLabel("No questions available for this case.");
            noQuestionsLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            questionsPanel.add(noQuestionsLabel);
            questionsPanel.revalidate();
            questionsPanel.repaint();
            return;
        }
        
        // Get most recent questions for highlighting
        java.util.Set<QuestionInstance> mostRecentQuestions = form.getMostRecentQuestions();
        
        // Display each question
        for (QuestionInstance question : form.getQuestions()) {
            JPanel questionPanel = new JPanel();
            questionPanel.setLayout(new BoxLayout(questionPanel, BoxLayout.Y_AXIS));
            
            // Highlight if it's a new question
            if (mostRecentQuestions != null && mostRecentQuestions.contains(question)) {
                questionPanel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Color.BLUE, 2),
                    BorderFactory.createEmptyBorder(10, 10, 10, 10)
                ));
                questionPanel.setBackground(new Color(230, 240, 255)); // Light blue background
                questionPanel.setOpaque(true);
            } else {
                questionPanel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                    BorderFactory.createEmptyBorder(10, 10, 10, 10)
                ));
            }
            questionPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
            questionPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
            
            // Question label with required indicator
            String questionText = question.getLabel();
            if (question.isRequired()) {
                questionText += " *";
            }
            JLabel questionLabel = new JLabel(questionText);
            questionLabel.setFont(questionLabel.getFont().deriveFont(Font.BOLD, 12f));
            if (question.isRequired()) {
                questionLabel.setForeground(Color.RED);
            }
            questionPanel.add(questionLabel);
            
            // Required indicator text
            if (question.isRequired()) {
                JLabel requiredLabel = new JLabel("(Required)");
                requiredLabel.setFont(requiredLabel.getFont().deriveFont(Font.PLAIN, 10f));
                requiredLabel.setForeground(Color.RED);
                questionPanel.add(requiredLabel);
            }
            
            // Answer input field
            JTextField answerField = new JTextField(30);
            answerField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
            answerField.setAlignmentX(Component.LEFT_ALIGNMENT);
            
            // Pre-fill with existing answer if present
            String existingAnswer = question.getAnswerValue();
            if (existingAnswer != null && !existingAnswer.trim().isEmpty()) {
                answerField.setText(existingAnswer);
            }
            
            questionPanel.add(answerField);
            
            // Store mapping
            answerFields.put(question, answerField);
            
            questionsPanel.add(questionPanel);
            questionsPanel.add(Box.createVerticalStrut(10));
        }
        
        questionsPanel.revalidate();
        questionsPanel.repaint();
    }
    
    private void saveAnswers() {
        Case currentCase = controller.getCurrentCustomerCase();
        if (currentCase == null) {
            JOptionPane.showMessageDialog(this, "No case loaded.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        Form form = currentCase.getForm();
        if (form == null) {
            JOptionPane.showMessageDialog(this, "No form available for this case.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Validate required questions
        boolean hasErrors = false;
        StringBuilder errorMessage = new StringBuilder("Please answer the following required questions:\n");
        
        for (Map.Entry<QuestionInstance, JTextField> entry : answerFields.entrySet()) {
            QuestionInstance question = entry.getKey();
            JTextField answerField = entry.getValue();
            String answer = answerField.getText().trim();
            
            if (question.isRequired() && answer.isEmpty()) {
                hasErrors = true;
                errorMessage.append("- ").append(question.getLabel()).append("\n");
            }
        }
        
        if (hasErrors) {
            JOptionPane.showMessageDialog(this, errorMessage.toString(), "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Save answers to question instances
        for (Map.Entry<QuestionInstance, JTextField> entry : answerFields.entrySet()) {
            QuestionInstance question = entry.getKey();
            JTextField answerField = entry.getValue();
            String answer = answerField.getText().trim();
            
            if (answer.isEmpty()) {
                question.clearAnswer();
            } else {
                question.setAnswerValue(answer);
            }
        }
        
        // Update case status to NEEDSREVIEW
        currentCase.setStatus(CaseStatus.NEEDSREVIEW);
        
        // Save to DataStore
        controller.getDataStore().saveCase(currentCase);
        
        JOptionPane.showMessageDialog(this, "Answers saved successfully! The Administrator will get back to you about next steps.", 
            "Success", JOptionPane.INFORMATION_MESSAGE);
    }
}

