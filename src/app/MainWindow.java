package app;

import javax.swing.JFrame;
import javax.swing.JPanel;
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
    
    public MainWindow(AppController controller) {
        this.controller = controller;
        this.cardLayout = new CardLayout();
        
        // Set up the frame
        setLayout(cardLayout);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        
        // Create all panels
        this.roleSelectionPanel = new RoleSelectionPanel(controller);
        this.adminLoginPanel = new AdminLoginPanel(controller);
        this.customerLoginPanel = new CustomerLoginPanel(controller);
        this.adminDashboardPanel = new AdminDashboardPanel(controller);
        this.customerFormPanel = new CustomerFormPanel(controller);
        this.formBuilderPanel = new FormBuilderPanel(controller);
        
        // Add panels to the card layout
        add(roleSelectionPanel, "RoleSelection");
        add(adminLoginPanel, "AdminLogin");
        add(customerLoginPanel, "CustomerLogin");
        add(adminDashboardPanel, "AdminDashboard");
        add(customerFormPanel, "CustomerForm");
        add(formBuilderPanel, "FormBuilder");
        
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
}

