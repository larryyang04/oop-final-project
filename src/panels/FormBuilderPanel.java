package panels;

import javax.swing.*;
import java.awt.*;
import app.AppController;
import model.Form;
import model.QuestionInstance;

public class FormBuilderPanel extends JPanel {
    private AppController controller;
    private JPanel formContentPanel;
    private JScrollPane scrollPane;
    
    public FormBuilderPanel(AppController controller) {
        this.controller = controller;
        setLayout(new BorderLayout());

        formContentPanel = new JPanel();
        formContentPanel.setLayout(new BoxLayout(formContentPanel, BoxLayout.Y_AXIS));
        formContentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        scrollPane = new JScrollPane(formContentPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        
        add(scrollPane, BorderLayout.CENTER);
    }
    
    public void renderFilledForm(Form form) {
        formContentPanel.removeAll();
        
        JLabel formIdLabel = new JLabel("Form ID: " + form.getId());
        formIdLabel.setFont(formIdLabel.getFont().deriveFont(Font.BOLD, 14f));
        formContentPanel.add(formIdLabel);
        
        JLabel revisionLabel = new JLabel("Current Revision: " + form.getCurrentRevision());
        revisionLabel.setFont(revisionLabel.getFont().deriveFont(Font.PLAIN, 12f));
        formContentPanel.add(revisionLabel);
        
        formContentPanel.add(Box.createVerticalStrut(15));
        
        formContentPanel.add(new JSeparator(SwingConstants.HORIZONTAL));
        formContentPanel.add(Box.createVerticalStrut(10));
        
        for (QuestionInstance question : form.getQuestions()) {
            JLabel questionLabel = new JLabel(question.getLabel() + (question.isRequired() ? " *" : ""));
            questionLabel.setFont(questionLabel.getFont().deriveFont(Font.BOLD));
            formContentPanel.add(questionLabel);
            
            JTextArea answerArea = new JTextArea(
                question.getAnswerValue() != null && !question.getAnswerValue().isEmpty() 
                    ? question.getAnswerValue() 
                    : "(not answered)"
            );
            answerArea.setEditable(false);
            answerArea.setLineWrap(true);
            answerArea.setWrapStyleWord(true);
            answerArea.setBackground(question.isAnswered() ? Color.WHITE : new Color(245, 245, 245));
            answerArea.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
            ));
            formContentPanel.add(answerArea);
            
            formContentPanel.add(Box.createVerticalStrut(15));
        }
        
        // Refresh the panel
        formContentPanel.revalidate();
        formContentPanel.repaint();
    }
}

