package com.shopsphere.swing.view;

import com.shopsphere.swing.dao.AdminDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Desktop Admin Management Main Frame.
 * Demonstrates RTU syllabus: JFrame, JTabbedPane, JTable, JScrollPane,
 * JComboBox, JButton, and Swing MVC Architecture.
 */
public class AdminMainFrame extends JFrame {

    private final AdminDAO adminDAO = new AdminDAO();
    private DefaultTableModel productsModel;
    private DefaultTableModel ordersModel;
    private DefaultTableModel lowStockModel;

    private JTable productsTable;
    private JTable ordersTable;
    private JTable lowStockTable;

    public AdminMainFrame() {
        super("ShopSphere - Desktop Administration Console (Swing MVC)");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(950, 650);
        setLocationRelativeTo(null);

        // Header Panel
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(15, 23, 42));
        topPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel title = new JLabel("ShopSphere Enterprise Admin Management");
        title.setFont(new Font("SansSerif", Font.BOLD, 18));
        title.setForeground(Color.WHITE);

        JLabel subtitle = new JLabel("Swing MVC Architecture • JDBC Connection • RTU Syllabus Module");
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 12));
        subtitle.setForeground(new Color(148, 163, 184));

        JPanel titlePanel = new JPanel(new GridLayout(2, 1));
        titlePanel.setOpaque(false);
        titlePanel.add(title);
        titlePanel.add(subtitle);

        JButton logoutBtn = new JButton("Logout");
        logoutBtn.addActionListener(e -> {
            dispose();
            new LoginFrame().setVisible(true);
        });

        topPanel.add(titlePanel, BorderLayout.WEST);
        topPanel.add(logoutBtn, BorderLayout.EAST);

        // Tabbed Pane
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("SansSerif", Font.BOLD, 12));

        tabbedPane.addTab("📦 Product Catalog", createProductsPanel());
        tabbedPane.addTab("🛒 Customer Orders", createOrdersPanel());
        tabbedPane.addTab("⚠️ Low-Stock Alerts", createLowStockPanel());
        tabbedPane.addTab("ℹ️ Syllabus & Architecture", createInfoPanel());

        setLayout(new BorderLayout());
        add(topPanel, BorderLayout.NORTH);
        add(tabbedPane, BorderLayout.CENTER);

        // Initial Data Load
        refreshProducts();
        refreshOrders();
        refreshLowStock();
    }

    private JPanel createProductsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        String[] cols = {"ID", "Name", "Brand", "Category", "Price (₹)", "Discount (%)", "Stock", "Status"};
        productsModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        productsTable = new JTable(productsModel);
        productsTable.setRowHeight(24);
        productsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(productsTable);

        // Action Toolbar
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        JButton addBtn = new JButton("Add Product");
        JButton updateStockBtn = new JButton("Set Stock");
        JButton deleteBtn = new JButton("Disable Product");
        JButton refreshBtn = new JButton("Refresh");

        addBtn.addActionListener(e -> {
            ProductDialog dlg = new ProductDialog(this);
            dlg.setVisible(true);
            if (dlg.isSucceeded()) refreshProducts();
        });

        updateStockBtn.addActionListener(e -> {
            int row = productsTable.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Select a product to update stock.", "Info", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            int id = (int) productsModel.getValueAt(row, 0);
            String current = String.valueOf(productsModel.getValueAt(row, 6));
            String input = JOptionPane.showInputDialog(this, "Enter new stock quantity for Product #" + id + ":", current);
            if (input != null && !input.isBlank()) {
                try {
                    int newStock = Integer.parseInt(input.trim());
                    adminDAO.updateStock(id, newStock);
                    refreshProducts();
                    refreshLowStock();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Invalid number: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        deleteBtn.addActionListener(e -> {
            int row = productsTable.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Select a product to disable.", "Info", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            int id = (int) productsModel.getValueAt(row, 0);
            int confirm = JOptionPane.showConfirmDialog(this, "Disable Product #" + id + "?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    adminDAO.deleteProduct(id);
                    refreshProducts();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        refreshBtn.addActionListener(e -> refreshProducts());

        toolbar.add(addBtn);
        toolbar.add(updateStockBtn);
        toolbar.add(deleteBtn);
        toolbar.add(refreshBtn);

        panel.add(toolbar, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createOrdersPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        String[] cols = {"Order ID", "Customer", "Total Amount (₹)", "Payment Method", "Status", "Order Date"};
        ordersModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        ordersTable = new JTable(ordersModel);
        ordersTable.setRowHeight(24);

        JScrollPane scrollPane = new JScrollPane(ordersTable);

        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        JLabel statusLbl = new JLabel("Set Status:");
        JComboBox<String> statusBox = new JComboBox<>(new String[]{"PLACED", "CONFIRMED", "SHIPPED", "DELIVERED", "CANCELLED"});
        JButton updateStatusBtn = new JButton("Update Status");
        JButton refreshBtn = new JButton("Refresh");

        updateStatusBtn.addActionListener(e -> {
            int row = ordersTable.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Select an order to update status.", "Info", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            int orderId = (int) ordersModel.getValueAt(row, 0);
            String newStatus = (String) statusBox.getSelectedItem();
            try {
                adminDAO.updateOrderStatus(orderId, newStatus);
                JOptionPane.showMessageDialog(this, "Order #" + orderId + " marked as " + newStatus, "Success", JOptionPane.INFORMATION_MESSAGE);
                refreshOrders();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error updating order: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        refreshBtn.addActionListener(e -> refreshOrders());

        toolbar.add(statusLbl);
        toolbar.add(statusBox);
        toolbar.add(updateStatusBtn);
        toolbar.add(refreshBtn);

        panel.add(toolbar, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createLowStockPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        String[] cols = {"Product ID", "Name", "Brand", "Category", "Price (₹)", "Stock Units"};
        lowStockModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        lowStockTable = new JTable(lowStockModel);
        lowStockTable.setRowHeight(24);

        JScrollPane scrollPane = new JScrollPane(lowStockTable);

        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        JButton restockBtn = new JButton("Quick Restock (+20 Units)");
        JButton refreshBtn = new JButton("Refresh");

        restockBtn.addActionListener(e -> {
            int row = lowStockTable.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Select a low-stock product to restock.", "Info", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            int id = (int) lowStockModel.getValueAt(row, 0);
            int current = (int) lowStockModel.getValueAt(row, 5);
            try {
                adminDAO.updateStock(id, current + 20);
                JOptionPane.showMessageDialog(this, "Restocked Product #" + id + " to " + (current + 20) + " units.", "Restocked", JOptionPane.INFORMATION_MESSAGE);
                refreshLowStock();
                refreshProducts();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        refreshBtn.addActionListener(e -> refreshLowStock());

        toolbar.add(restockBtn);
        toolbar.add(refreshBtn);

        panel.add(toolbar, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createInfoPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JTextArea infoArea = new JTextArea();
        infoArea.setEditable(false);
        infoArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        infoArea.setText(
                "========================================================================\n" +
                "  SHOPSPHERE DESKTOP ADMINISTRATION MODULE (SWING MVC ARCHITECTURE)\n" +
                "========================================================================\n\n" +
                "1. RTU Syllabus Mapping:\n" +
                "   - Swing Components: JFrame, JPanel, JTabbedPane, JTable, JScrollPane,\n" +
                "                       JTextField, JPasswordField, JButton, JCheckBox,\n" +
                "                       JComboBox, JOptionPane.\n" +
                "   - MVC Architecture:\n" +
                "       * Model:      DefaultTableModel, Product, Order\n" +
                "       * View:       LoginFrame, AdminMainFrame, ProductDialog\n" +
                "       * Controller: AdminDAO and Swing Event Dispatch Thread Handlers\n" +
                "   - Pluggable Look & Feel:\n" +
                "       * Configured via UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName())\n\n" +
                "2. Live Database Connection:\n" +
                "   - Target Database: MySQL 'shopsphere'\n" +
                "   - Tables Queried:  products, categories, orders, users\n" +
                "   - Query Safety:    Strict PreparedStatement parameterization.\n\n" +
                "========================================================================\n"
        );

        panel.add(new JScrollPane(infoArea), BorderLayout.CENTER);
        return panel;
    }

    private void refreshProducts() {
        productsModel.setRowCount(0);
        try {
            List<Object[]> list = adminDAO.getProducts();
            for (Object[] row : list) productsModel.addRow(row);
        } catch (Exception ex) {
            System.err.println("Error loading products: " + ex.getMessage());
        }
    }

    private void refreshOrders() {
        ordersModel.setRowCount(0);
        try {
            List<Object[]> list = adminDAO.getOrders();
            for (Object[] row : list) ordersModel.addRow(row);
        } catch (Exception ex) {
            System.err.println("Error loading orders: " + ex.getMessage());
        }
    }

    private void refreshLowStock() {
        lowStockModel.setRowCount(0);
        try {
            List<Object[]> list = adminDAO.getLowStockProducts(15);
            for (Object[] row : list) lowStockModel.addRow(row);
        } catch (Exception ex) {
            System.err.println("Error loading low stock items: " + ex.getMessage());
        }
    }
}
