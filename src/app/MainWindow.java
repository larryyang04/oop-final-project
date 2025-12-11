package app;

import javax.swing.JFrame;
import java.awt.CardLayout;
import panels.*;

public class MainWindow extends JFrame {
    private AppController controller;
    private CardLayout cardLayout;
    private RoleSelectionPanel roleSelectionPanel;
    private AdminLoginPanel adminLoginPanel;
    private CustomerLoginPanel customerLoginPanel;
    private AdminDashboardPanel adminDashboardPanel;
    private CustomerFormPanel customerFormPanel;
    private FormBuilderPanel formBuilderPanel;
    private CaseBuilderPanel caseBuilderPanel;
    
    public MainWindow(AppController controller) {
        this.controller = controller;
        this.cardLayout = new CardLayout();
        
        setLayout(cardLayout);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        
        this.roleSelectionPanel = new RoleSelectionPanel(controller);
        this.adminLoginPanel = new AdminLoginPanel(controller);
        this.customerLoginPanel = new CustomerLoginPanel(controller);
        this.adminDashboardPanel = new AdminDashboardPanel(controller);
        this.customerFormPanel = new CustomerFormPanel(controller);
        this.formBuilderPanel = new FormBuilderPanel(controller);
        this.caseBuilderPanel = new CaseBuilderPanel(controller);
        
        add(roleSelectionPanel, "RoleSelection");
        add(adminLoginPanel, "AdminLogin");
        add(customerLoginPanel, "CustomerLogin");
        add(adminDashboardPanel, "AdminDashboard");
        add(customerFormPanel, "CustomerForm");
        add(formBuilderPanel, "FormBuilder");
        add(caseBuilderPanel, "CaseBuilder");
        
        setVisible(true);
    }
    
    public void showRoleSelection() {
        cardLayout.show(getContentPane(), "RoleSelection");
    }
    
    public void showAdminLogin() {
        cardLayout.show(getContentPane(), "AdminLogin");
    }
    
    public void showCustomerLogin() {
        cardLayout.show(getContentPane(), "CustomerLogin");
    }
    
    public void showAdminDashboard() {
        adminDashboardPanel.refreshDashboard();
        cardLayout.show(getContentPane(), "AdminDashboard");
    }
    
    public void showFormBuilder() {
        cardLayout.show(getContentPane(), "FormBuilder");
    }
    
    // For editing a template
    public void showFormBuilderWithTemplate(model.FormTemplate template) {
        formBuilderPanel.loadTemplateForEditing(template);
        cardLayout.show(getContentPane(), "FormBuilder");
    }

    public void showCaseBuilder() {
        caseBuilderPanel.refreshTemplates();
        cardLayout.show(getContentPane(), "CaseBuilder");
    }
    
    public AdminDashboardPanel getAdminDashboardPanel() {
        return adminDashboardPanel;
    }
}

