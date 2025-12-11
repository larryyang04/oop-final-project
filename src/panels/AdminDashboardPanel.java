package panels;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import app.AppController;
import model.Case;
import model.CaseStatus;
import model.Form;
import model.FormTemplate;
import model.QuestionTemplate;
import model.QuestionInstance;

public class AdminDashboardPanel extends JPanel {
    private AppController controller;
    private CardLayout cardLayout;
    private JPanel cardPanel;
    private DefaultListModel<String> templateListModel;
    private List<FormTemplate> templatesList;
    private DefaultListModel<String> casesListModel;
    private List<Case> casesList;
    private JList<String> casesJList;
    private JPanel templateDetailPanel;
    private JPanel templateDetailContentPanel;
    private JPanel caseDetailPanel;
    private JPanel caseDetailContentPanel;
    private Case currentViewingCase;

    public AdminDashboardPanel(AppController controller) {
        this.controller = controller;
        setLayout(new BorderLayout());

        // Card layout to switch between dashboard and template management views
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        JPanel dashboardView = buildDashboardView();
        JPanel templatesView = buildTemplatesView();
        templateDetailPanel = buildTemplateDetailView();
        caseDetailPanel = buildCaseDetailView();

        cardPanel.add(dashboardView, "dashboard");
        cardPanel.add(templatesView, "templates");
        cardPanel.add(templateDetailPanel, "templateDetail");
        cardPanel.add(caseDetailPanel, "caseDetail");

        add(cardPanel, BorderLayout.CENTER);
    }

    private JPanel buildDashboardView() {
        JPanel wrapper = new JPanel(new BorderLayout());

        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Admin Dashboard");
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 24f));
        headerPanel.add(titleLabel);

        JButton createCaseButton = new JButton("Create New Case");
        createCaseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.showCaseBuilder();
            }
        });
        headerPanel.add(createCaseButton);
        
        JButton manageTemplatesButton = new JButton("Manage Templates");
        manageTemplatesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showTemplatesView();
            }
        });
        headerPanel.add(manageTemplatesButton);

        wrapper.add(headerPanel, BorderLayout.NORTH);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JLabel welcomeLabel = new JLabel("Admin Dashboard");
        welcomeLabel.setFont(welcomeLabel.getFont().deriveFont(Font.BOLD, 16f));
        welcomeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(welcomeLabel);
        contentPanel.add(Box.createVerticalStrut(20));

        // Cases list section
        JLabel casesLabel = new JLabel("Cases:");
        casesLabel.setFont(casesLabel.getFont().deriveFont(Font.BOLD, 14f));
        casesLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(casesLabel);
        contentPanel.add(Box.createVerticalStrut(10));

        casesListModel = new DefaultListModel<>();
        casesJList = new JList<>(casesListModel);
        casesJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Add double-click listener to view case details
        casesJList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int selectedIndex = casesJList.getSelectedIndex();
                    if (selectedIndex >= 0 && casesList != null && selectedIndex < casesList.size()) {
                        Case selectedCase = casesList.get(selectedIndex);
                        showCaseDetail(selectedCase);
                    }
                }
            }
        });
        
        JScrollPane casesScrollPane = new JScrollPane(casesJList);
        casesScrollPane.setPreferredSize(new Dimension(400, 200));
        casesScrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(casesScrollPane);
        contentPanel.add(Box.createVerticalStrut(20));

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(null);

        wrapper.add(scrollPane, BorderLayout.CENTER);
        
        return wrapper;
    }

    private JPanel buildTemplatesView() {
        JPanel wrapper = new JPanel(new BorderLayout());

        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JButton backButton = new JButton("← Back to Dashboard");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showDashboardView();
            }
        });
        headerPanel.add(backButton);

        JLabel titleLabel = new JLabel("Manage Templates");
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 20f));
        headerPanel.add(titleLabel);

        wrapper.add(headerPanel, BorderLayout.NORTH);

        // Center content: list of templates and actions
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        templateListModel = new DefaultListModel<>();
        JList<String> templateList = new JList<>(templateListModel);
        templateList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Add double-click listener to view template details
        templateList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int selectedIndex = templateList.getSelectedIndex();
                    if (selectedIndex >= 0 && selectedIndex < templatesList.size()) {
                        FormTemplate selectedTemplate = templatesList.get(selectedIndex);
                        showTemplateDetail(selectedTemplate);
                    }
                }
            }
        });
        
        JScrollPane listScrollPane = new JScrollPane(templateList);
        listScrollPane.setPreferredSize(new Dimension(400, 250));
        listScrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton createTemplateButton = new JButton("Create New Template");
        createTemplateButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        createTemplateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.showFormBuilder();
            }
        });

        JLabel listLabel = new JLabel("Existing Templates:");
        listLabel.setFont(listLabel.getFont().deriveFont(Font.BOLD, 14f));
        listLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        contentPanel.add(listLabel);
        contentPanel.add(Box.createVerticalStrut(8));
        contentPanel.add(listScrollPane);
        contentPanel.add(Box.createVerticalStrut(15));
        contentPanel.add(createTemplateButton);

        wrapper.add(contentPanel, BorderLayout.CENTER);

        loadTemplates();
        return wrapper;
    }

    private void loadCases() {
        if (casesListModel == null) {
            return; // Cases list not initialized yet
        }
        casesListModel.clear();
        if (controller.getDataStore() != null) {
            casesList = controller.getDataStore().getCases();
            if (casesList == null || casesList.isEmpty()) {
                casesListModel.addElement("No cases found.");
            } else {
                for (Case c : casesList) {
                    String displayText = "ID: " + c.getId() + " - " + c.getFirstName() + " " + c.getLastName() + " [" + c.getStatus() + "]";
                    casesListModel.addElement(displayText);
                }
            }
            // Ensure the list UI updates
            if (casesJList != null) {
                casesJList.revalidate();
                casesJList.repaint();
            }
        } else {
            casesListModel.addElement("DataStore not available.");
            casesList = null;
        }
    }
    
    private void loadTemplates() {
        templateListModel.clear();
        if (controller != null && controller.getDataStore() != null) {
            templatesList = controller.getDataStore().getFormTemplates();
            if (templatesList.isEmpty()) {
                templateListModel.addElement("No templates found.");
            } else {
                for (FormTemplate template : templatesList) {
                    templateListModel.addElement(template.getName());
                }
            }
        } else {
            templateListModel.addElement("DataStore not available.");
            templatesList = null;
        }
    }
    
    private JPanel buildTemplateDetailView() {
        JPanel wrapper = new JPanel(new BorderLayout());
        
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Left side: Back button and title
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton backButton = new JButton("← Back to Templates");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showTemplatesView();
            }
        });
        leftPanel.add(backButton);
        
        JLabel titleLabel = new JLabel("Template Details");
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 20f));
        leftPanel.add(titleLabel);
        
        headerPanel.add(leftPanel, BorderLayout.WEST);
        
        // Right side: Action buttons (will be populated when showing details)
        JPanel buttonContainer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonContainer.setName("buttonContainer");
        headerPanel.add(buttonContainer, BorderLayout.EAST);
        
        wrapper.add(headerPanel, BorderLayout.NORTH);
        
        // Content will be populated when showing details
        templateDetailContentPanel = new JPanel();
        templateDetailContentPanel.setLayout(new BoxLayout(templateDetailContentPanel, BoxLayout.Y_AXIS));
        templateDetailContentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JScrollPane scrollPane = new JScrollPane(templateDetailContentPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(null);
        
        wrapper.add(scrollPane, BorderLayout.CENTER);
        return wrapper;
    }
    
    private JPanel buildCaseDetailView() {
        JPanel wrapper = new JPanel(new BorderLayout());
        
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Left side: Back button and title
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton backButton = new JButton("← Back to Dashboard");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showDashboardView();
            }
        });
        leftPanel.add(backButton);
        
        JLabel titleLabel = new JLabel("Case Details");
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 20f));
        leftPanel.add(titleLabel);
        
        headerPanel.add(leftPanel, BorderLayout.WEST);
        
        // Right side: Action buttons (will be populated when showing details)
        JPanel buttonContainer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonContainer.setName("buttonContainer");
        headerPanel.add(buttonContainer, BorderLayout.EAST);
        
        wrapper.add(headerPanel, BorderLayout.NORTH);
        
        // Content will be populated when showing details
        caseDetailContentPanel = new JPanel();
        caseDetailContentPanel.setLayout(new BoxLayout(caseDetailContentPanel, BoxLayout.Y_AXIS));
        caseDetailContentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JScrollPane scrollPane = new JScrollPane(caseDetailContentPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(null);
        
        wrapper.add(scrollPane, BorderLayout.CENTER);
        return wrapper;
    }
    
    private void showTemplateDetail(FormTemplate template) {
        if (templateDetailPanel == null || templateDetailContentPanel == null) {
            return;
        }
        
        // Clear existing content
        templateDetailContentPanel.removeAll();
            
        // Add template name
        JLabel nameLabel = new JLabel("Template: " + template.getName());
        nameLabel.setFont(nameLabel.getFont().deriveFont(Font.BOLD, 18f));
        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        templateDetailContentPanel.add(nameLabel);
        
        JLabel idLabel = new JLabel("ID: " + template.getId());
        idLabel.setFont(idLabel.getFont().deriveFont(Font.PLAIN, 12f));
        idLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        templateDetailContentPanel.add(idLabel);
        
        // Add action buttons to header
        JPanel headerPanel = (JPanel) templateDetailPanel.getComponent(0);
        JPanel buttonContainer = null;
        for (Component comp : headerPanel.getComponents()) {
            if (comp instanceof JPanel && "buttonContainer".equals(comp.getName())) {
                buttonContainer = (JPanel) comp;
                break;
            }
        }
        
        if (buttonContainer != null) {
            buttonContainer.removeAll();
            
            JButton editButton = new JButton("Edit Template");
            editButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    editTemplate(template);
                }
            });
            buttonContainer.add(editButton);
            
            JButton deleteButton = new JButton("Delete Template");
            deleteButton.setForeground(Color.RED);
            deleteButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    deleteTemplate(template);
                }
            });
            buttonContainer.add(deleteButton);
            
            buttonContainer.revalidate();
            buttonContainer.repaint();
        }
        
        templateDetailContentPanel.add(Box.createVerticalStrut(10));
        templateDetailContentPanel.add(new JSeparator(SwingConstants.HORIZONTAL));
        templateDetailContentPanel.add(Box.createVerticalStrut(15));
        
        // Add questions header
        JLabel questionsLabel = new JLabel("Questions (" + template.getQuestions().size() + "):");
        questionsLabel.setFont(questionsLabel.getFont().deriveFont(Font.BOLD, 14f));
        questionsLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        templateDetailContentPanel.add(questionsLabel);
        templateDetailContentPanel.add(Box.createVerticalStrut(10));
        
        // Add each question
        int questionNumber = 1;
        for (QuestionTemplate question : template.getQuestions()) {
            JPanel questionPanel = new JPanel();
            questionPanel.setLayout(new BoxLayout(questionPanel, BoxLayout.Y_AXIS));
            questionPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
            ));
            questionPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
            
            JLabel qNumberLabel = new JLabel("Question " + questionNumber + " (ID: " + question.getId() + ")");
            qNumberLabel.setFont(qNumberLabel.getFont().deriveFont(Font.BOLD, 12f));
            questionPanel.add(qNumberLabel);
            
            JLabel qTextLabel = new JLabel(question.getLabel());
            qTextLabel.setFont(qTextLabel.getFont().deriveFont(Font.PLAIN, 12f));
            questionPanel.add(qTextLabel);
            
            JLabel requiredLabel = new JLabel("Required: " + (question.isRequired() ? "Yes" : "No"));
            requiredLabel.setFont(requiredLabel.getFont().deriveFont(Font.PLAIN, 11f));
            requiredLabel.setForeground(question.isRequired() ? Color.RED : Color.GRAY);
            questionPanel.add(requiredLabel);
            
            templateDetailContentPanel.add(questionPanel);
            templateDetailContentPanel.add(Box.createVerticalStrut(10));
            questionNumber++;
        }
        
        templateDetailContentPanel.revalidate();
        templateDetailContentPanel.repaint();
        
        // Show the detail view
        cardLayout.show(cardPanel, "templateDetail");
    }
    
    private void deleteTemplate(FormTemplate template) {
        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to delete the template \"" + template.getName() + "\"?\nThis action cannot be undone.",
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );
        
        if (confirm == JOptionPane.YES_OPTION) {
            controller.getDataStore().deleteFormTemplate(template.getId());
            JOptionPane.showMessageDialog(this, "Template deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            showTemplatesView();
        }
    }
    
    private void editTemplate(FormTemplate template) {
        controller.showFormBuilderWithTemplate(template);
    }
    
    private void showCaseDetail(Case caseObj) {
        if (caseDetailPanel == null || caseDetailContentPanel == null) {
            return;
        }
        
        // Store current viewing case
        currentViewingCase = caseObj;
        
        // Clear existing content
        caseDetailContentPanel.removeAll();
        
        // Add action buttons to header
        JPanel headerPanel = (JPanel) caseDetailPanel.getComponent(0);
        JPanel buttonContainer = null;
        for (Component comp : headerPanel.getComponents()) {
            if (comp instanceof JPanel && "buttonContainer".equals(comp.getName())) {
                buttonContainer = (JPanel) comp;
                break;
            }
        }
        
        if (buttonContainer != null) {
            buttonContainer.removeAll();
            
            // Approve Case button (only show if status is not ACCEPTED)
            if (caseObj.getStatus() != CaseStatus.ACCEPTED) {
                JButton approveButton = new JButton("Approve Case");
                approveButton.setForeground(new Color(0, 128, 0)); // Green color
                approveButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        approveCase(caseObj);
                    }
                });
                buttonContainer.add(approveButton);
            }
            
            // Add Questions button
            JButton addQuestionsButton = new JButton("Add Questions");
            addQuestionsButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    showAddQuestionsToForm(caseObj);
                }
            });
            buttonContainer.add(addQuestionsButton);
            
            buttonContainer.revalidate();
            buttonContainer.repaint();
        }
        
        // Add case metadata
        JLabel caseLabel = new JLabel("Case ID: " + caseObj.getId());
        caseLabel.setFont(caseLabel.getFont().deriveFont(Font.BOLD, 18f));
        caseLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        caseDetailContentPanel.add(caseLabel);
        
        JLabel codeLabel = new JLabel("Code: " + caseObj.getCode());
        codeLabel.setFont(codeLabel.getFont().deriveFont(Font.PLAIN, 12f));
        codeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        caseDetailContentPanel.add(codeLabel);
        
        JLabel nameLabel = new JLabel("Name: " + caseObj.getFirstName() + " " + caseObj.getLastName());
        nameLabel.setFont(nameLabel.getFont().deriveFont(Font.PLAIN, 12f));
        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        caseDetailContentPanel.add(nameLabel);
        
        JLabel emailLabel = new JLabel("Email: " + caseObj.getEmail());
        emailLabel.setFont(emailLabel.getFont().deriveFont(Font.PLAIN, 12f));
        emailLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        caseDetailContentPanel.add(emailLabel);
        
        JLabel statusLabel = new JLabel("Status: " + caseObj.getStatus());
        statusLabel.setFont(statusLabel.getFont().deriveFont(Font.PLAIN, 12f));
        statusLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        caseDetailContentPanel.add(statusLabel);
        
        caseDetailContentPanel.add(Box.createVerticalStrut(10));
        caseDetailContentPanel.add(new JSeparator(SwingConstants.HORIZONTAL));
        caseDetailContentPanel.add(Box.createVerticalStrut(15));
        
        // Add form questions header
        Form form = caseObj.getForm();
        if (form != null && form.getQuestions() != null) {
            JLabel questionsLabel = new JLabel("Form Questions (" + form.getQuestions().size() + "):");
            questionsLabel.setFont(questionsLabel.getFont().deriveFont(Font.BOLD, 14f));
            questionsLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            caseDetailContentPanel.add(questionsLabel);
            caseDetailContentPanel.add(Box.createVerticalStrut(10));
            
            // Add each question
            int questionNumber = 1;
            for (QuestionInstance question : form.getQuestions()) {
                JPanel questionPanel = new JPanel();
                questionPanel.setLayout(new BoxLayout(questionPanel, BoxLayout.Y_AXIS));
                questionPanel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                    BorderFactory.createEmptyBorder(10, 10, 10, 10)
                ));
                questionPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
                
                JLabel qNumberLabel = new JLabel("Question " + questionNumber);
                qNumberLabel.setFont(qNumberLabel.getFont().deriveFont(Font.BOLD, 12f));
                questionPanel.add(qNumberLabel);
                
                JLabel qTextLabel = new JLabel(question.getLabel());
                qTextLabel.setFont(qTextLabel.getFont().deriveFont(Font.PLAIN, 12f));
                questionPanel.add(qTextLabel);
                
                JLabel requiredLabel = new JLabel("Required: " + (question.isRequired() ? "Yes" : "No"));
                requiredLabel.setFont(requiredLabel.getFont().deriveFont(Font.PLAIN, 11f));
                requiredLabel.setForeground(question.isRequired() ? Color.RED : Color.GRAY);
                questionPanel.add(requiredLabel);
                
                String answerValue = question.getAnswerValue();
                String answerDisplay = (answerValue != null && !answerValue.trim().isEmpty()) 
                    ? answerValue 
                    : "Not answered";
                JLabel answerLabel = new JLabel("Answer: " + answerDisplay);
                answerLabel.setFont(answerLabel.getFont().deriveFont(Font.PLAIN, 11f));
                answerLabel.setForeground((answerValue != null && !answerValue.trim().isEmpty()) 
                    ? Color.BLACK 
                    : Color.GRAY);
                questionPanel.add(answerLabel);
                
                caseDetailContentPanel.add(questionPanel);
                caseDetailContentPanel.add(Box.createVerticalStrut(10));
                questionNumber++;
            }
        } else {
            JLabel noQuestionsLabel = new JLabel("No form questions available.");
            noQuestionsLabel.setFont(noQuestionsLabel.getFont().deriveFont(Font.PLAIN, 12f));
            noQuestionsLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            caseDetailContentPanel.add(noQuestionsLabel);
        }
        
        caseDetailContentPanel.revalidate();
        caseDetailContentPanel.repaint();
        
        // Show the detail view
        cardLayout.show(cardPanel, "caseDetail");
    }
    
    private void approveCase(Case caseObj) {
        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to approve this case?\nCase Code: " + caseObj.getCode(),
            "Confirm Approval",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );
        
        if (confirm == JOptionPane.YES_OPTION) {
            caseObj.setStatus(CaseStatus.ACCEPTED);
            controller.getDataStore().saveCase(caseObj);
            JOptionPane.showMessageDialog(this, "Case approved successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            showDashboardView();
        }
    }
    
    private void showAddQuestionsToForm(Case caseObj) {
        controller.showAddQuestionsToForm(caseObj);
    }

    private void showDashboardView() {
        loadCases();
        cardLayout.show(cardPanel, "dashboard");
    }
    
    // Public method to refresh dashboard when shown
    public void refreshDashboard() {
        loadCases();
    }

    private void showTemplatesView() {
        loadTemplates();
        cardLayout.show(cardPanel, "templates");
    }

    
    // For AppController to get back to template management after creating template
    public void showTemplatesViewAndRefresh() {
        loadTemplates();
        cardLayout.show(cardPanel, "templates");
    }
    
    // Public method to refresh and show case detail view
    public void refreshAndShowCaseDetail(Case caseObj) {
        if (caseObj != null) {
            showCaseDetail(caseObj);
        } else if (currentViewingCase != null) {
            // Refresh the currently viewing case
            showCaseDetail(currentViewingCase);
        }
    }
}

