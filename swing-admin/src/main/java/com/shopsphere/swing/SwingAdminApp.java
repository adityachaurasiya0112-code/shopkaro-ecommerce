package com.shopsphere.swing;

import com.shopsphere.swing.view.LoginFrame;

import javax.swing.*;

/**
 * Main Entry Point for ShopSphere Desktop Administration Module.
 * Demonstrates RTU syllabus: Pluggable Look and Feel (UIManager) and Event Dispatch Thread (SwingUtilities.invokeLater).
 */
public class SwingAdminApp {

    public static void main(String[] args) {
        // Set Pluggable Look and Feel per syllabus requirement
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("Could not set system look and feel: " + e.getMessage());
        }

        // Run GUI on Swing Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(() -> {
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
        });
    }
}
