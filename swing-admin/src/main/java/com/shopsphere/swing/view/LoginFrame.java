package com.shopsphere.swing.view;

import com.shopsphere.swing.dao.AdminDAO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Desktop Swing Login Window.
 * Demonstrates RTU syllabus: JFrame, JPanel, JLabel, JTextField, JPasswordField,
 * JButton, JCheckBox, and JOptionPane dialogs.
 */
public class LoginFrame extends JFrame {

    private final JTextField emailField;
    private final JPasswordField passwordField;
    private final JCheckBox showPasswordCheckBox;
    private final JButton loginButton;
    private final JButton cancelButton;
    private final AdminDAO adminDAO = new AdminDAO();

    public LoginFrame() {
        super("ShopSphere - Desktop Admin Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(420, 310);
        setLocationRelativeTo(null);
        setResizable(false);

        // Header Panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(30, 41, 59));
        headerPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 15));
        JLabel titleLabel = new JLabel("ShopSphere Desktop Admin");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);

        // Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 25, 10, 25));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(6, 6, 6, 6);

        // Email row
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.3;
        JLabel emailLabel = new JLabel("Admin Email:");
        emailLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
        formPanel.add(emailLabel, gbc);

        gbc.gridx = 1; gbc.weightx = 0.7;
        emailField = new JTextField("admin@shopsphere.com", 18);
        formPanel.add(emailField, gbc);

        // Password row
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.3;
        JLabel passLabel = new JLabel("Password:");
        passLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
        formPanel.add(passLabel, gbc);

        gbc.gridx = 1; gbc.weightx = 0.7;
        passwordField = new JPasswordField("admin123", 18);
        formPanel.add(passwordField, gbc);

        // Show password checkbox
        gbc.gridx = 1; gbc.gridy = 2;
        showPasswordCheckBox = new JCheckBox("Show Password");
        showPasswordCheckBox.setFont(new Font("SansSerif", Font.PLAIN, 11));
        showPasswordCheckBox.addActionListener(e -> {
            if (showPasswordCheckBox.isSelected()) {
                passwordField.setEchoChar((char) 0);
            } else {
                passwordField.setEchoChar('•');
            }
        });
        formPanel.add(showPasswordCheckBox, gbc);

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));
        loginButton = new JButton("Login");
        loginButton.setBackground(new Color(79, 70, 229));
        loginButton.setForeground(Color.BLACK);
        loginButton.setFont(new Font("SansSerif", Font.BOLD, 12));

        cancelButton = new JButton("Exit");
        buttonPanel.add(cancelButton);
        buttonPanel.add(loginButton);

        // Event listeners
        loginButton.addActionListener(this::handleLogin);
        passwordField.addActionListener(this::handleLogin);
        cancelButton.addActionListener(e -> System.exit(0));

        // Assemble Layout
        setLayout(new BorderLayout());
        add(headerPanel, BorderLayout.NORTH);
        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void handleLogin(ActionEvent e) {
        String email = emailField.getText().trim();
        String pass = new String(passwordField.getPassword());

        if (email.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter both email and password.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            boolean valid = adminDAO.authenticateAdmin(email, pass);
            if (valid) {
                JOptionPane.showMessageDialog(this, "Welcome Administrator!", "Authentication Success", JOptionPane.INFORMATION_MESSAGE);
                dispose();
                SwingUtilities.invokeLater(() -> new AdminMainFrame().setVisible(true));
            } else {
                JOptionPane.showMessageDialog(this, "Invalid credentials or non-admin role.", "Access Denied", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage(), "Connection Failure", JOptionPane.ERROR_MESSAGE);
        }
    }
}
