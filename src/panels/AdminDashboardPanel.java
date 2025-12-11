package panels;

import javax.swing.*;
import java.awt.*;
import app.AppController;

public class AdminDashboardPanel extends JPanel {
    private AppController controller;
    
    public AdminDashboardPanel(AppController controller) {
        this.controller = controller;
        setLayout(new BorderLayout());
        
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("Admin Dashboard");
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 24f));
        headerPanel.add(titleLabel);
        
        add(headerPanel, BorderLayout.NORTH);
        
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        JLabel welcomeLabel = new JLabel("Welcome to the Admin Dashboard!");
        welcomeLabel.setFont(welcomeLabel.getFont().deriveFont(Font.BOLD, 16f));
        welcomeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(welcomeLabel);
        contentPanel.add(Box.createVerticalStrut(20));
        
        JLabel section1Label = new JLabel("Dashboard Features:");
        section1Label.setFont(section1Label.getFont().deriveFont(Font.BOLD, 14f));
        section1Label.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(section1Label);
        contentPanel.add(Box.createVerticalStrut(10));
        
        JLabel feature1 = new JLabel("• View and manage cases");
        feature1.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(feature1);
        
        JLabel feature2 = new JLabel("• Create and edit form templates");
        feature2.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(feature2);
        
        JLabel feature3 = new JLabel("• Review submitted forms");
        feature3.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(feature3);
        
        contentPanel.add(Box.createVerticalStrut(30));
        
        JLabel statusLabel = new JLabel("System Status:");
        statusLabel.setFont(statusLabel.getFont().deriveFont(Font.BOLD, 14f));
        statusLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(statusLabel);
        contentPanel.add(Box.createVerticalStrut(10));
        
        JLabel status1 = new JLabel("✓ Successfully logged in");
        status1.setForeground(new Color(0, 128, 0));
        status1.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(status1);
        
        JLabel status2 = new JLabel("✓ Dashboard loaded");
        status2.setForeground(new Color(0, 128, 0));
        status2.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(status2);
        
        // Scrollable content
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(null);
        
        add(scrollPane, BorderLayout.CENTER);
    }
}

