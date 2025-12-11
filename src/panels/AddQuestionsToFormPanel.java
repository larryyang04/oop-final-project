package panels;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.util.ArrayList;
import java.util.List;
import app.AppController;
import model.Case;
import model.Form;
import model.QuestionInstance;

public class AddQuestionsToFormPanel extends JPanel {
    private AppController controller;
    private Case currentCase;
    private JTextField questionField;
    private JCheckBox requiredCheckBox;
    private DefaultListModel<String> questionListModel;
    private JList<String> questionList;
    private List<QuestionInstance> newQuestions;
    private JButton deleteQuestionButton;
    private JLabel caseInfoLabel;
    
    public AddQuestionsToFormPanel(AppController controller) {
        this.controller = controller;
        this.newQuestions = new ArrayList<>();
        
        setLayout(new BorderLayout());
        
        // Back button panel
        JPanel backButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        backButtonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JButton backButton = new JButton("← Back to Case Details");
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
        JLabel titleLabel = new JLabel("Add Questions to Form");
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 20f));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(titleLabel);
        contentPanel.add(Box.createVerticalStrut(10));
        
        // Case info
        caseInfoLabel = new JLabel();
        caseInfoLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(caseInfoLabel);
        contentPanel.add(Box.createVerticalStrut(20));
        
        // Questions preview section
        JLabel previewLabel = new JLabel("Questions to Add:");
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
        JButton saveButton = new JButton("Save Questions");
        saveButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        saveButton.setFont(saveButton.getFont().deriveFont(Font.BOLD, 14f));
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveQuestions();
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
    
    public void loadCase(Case caseObj) {
        this.currentCase = caseObj;
        if (caseObj != null) {
            caseInfoLabel.setText("Case Code: " + caseObj.getCode() + " | Customer: " + 
                caseObj.getFirstName() + " " + caseObj.getLastName());
        }
        // Reset form
        resetForm();
    }
    
    private void addQuestion() {
        String questionText = questionField.getText().trim();
        if (questionText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a question text.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        boolean required = requiredCheckBox.isSelected();
        QuestionInstance question = new QuestionInstance(0, questionText, required); // ID will be set when saving
        newQuestions.add(question);
        
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
        if (selectedIndex < 0 || selectedIndex >= newQuestions.size()) {
            return;
        }
        
        // Remove from newQuestions list
        newQuestions.remove(selectedIndex);
        
        // Remove from list model
        questionListModel.remove(selectedIndex);
        
        // Clear selection and disable delete button
        questionList.clearSelection();
        deleteQuestionButton.setEnabled(false);
    }
    
    private void saveQuestions() {
        if (currentCase == null) {
            JOptionPane.showMessageDialog(this, "No case loaded.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (newQuestions.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please add at least one question.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        Form form = currentCase.getForm();
        if (form == null) {
            JOptionPane.showMessageDialog(this, "No form available for this case.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Generate IDs for new questions and create QuestionInstances
        List<QuestionInstance> questionsToAdd = new ArrayList<>();
        for (QuestionInstance question : newQuestions) {
            int questionId = controller.getDataStore().getNextQuestionId();
            QuestionInstance newQuestion = new QuestionInstance(questionId, question.getLabel(), question.isRequired());
            questionsToAdd.add(newQuestion);
        }
        
        // Add multiple questions (this clears mostRecentQuestions and adds new ones)
        form.addMultipleQuestions(questionsToAdd);
        
        // Save case to DataStore
        controller.getDataStore().saveCase(currentCase);
        
        JOptionPane.showMessageDialog(this, "Questions added successfully! The customer will see these as new questions.", 
            "Success", JOptionPane.INFORMATION_MESSAGE);
        
        // Navigate back to case detail view
        controller.showAdminDashboardAndRefreshCase(currentCase);
    }
    
    private void resetForm() {
        questionField.setText("");
        requiredCheckBox.setSelected(false);
        questionListModel.clear();
        newQuestions.clear();
        questionList.clearSelection();
        deleteQuestionButton.setEnabled(false);
    }
}

