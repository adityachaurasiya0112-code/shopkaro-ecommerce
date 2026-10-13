package com.shopsphere.swing.view;

import com.shopsphere.swing.dao.AdminDAO;

import javax.swing.*;
import java.awt.*;

/**
 * Swing Dialog for adding a new product to the catalog.
 */
public class ProductDialog extends JDialog {

    private final JTextField nameField = new JTextField(20);
    private final JTextField brandField = new JTextField(20);
    private final JComboBox<String> categoryComboBox = new JComboBox<>(new String[]{
            "1 - Electronics",
            "2 - Fashion",
            "3 - Books",
            "4 - Home & Kitchen",
            "5 - Fitness"
    });
    private final JTextField priceField = new JTextField(10);
    private final JTextField discountField = new JTextField("0", 10);
    private final JTextField stockField = new JTextField("25", 10);
    private final JTextField descField = new JTextField(20);
    private final AdminDAO adminDAO = new AdminDAO();
    private boolean succeeded = false;

    public ProductDialog(Frame parent) {
        super(parent, "Add New Product", true);
        setSize(460, 420);
        setLocationRelativeTo(parent);
        setResizable(false);

        JPanel panel = new JPanel(new GridLayout(8, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 25, 10, 25));

        panel.add(new JLabel("Product Name:"));
        panel.add(nameField);

        panel.add(new JLabel("Brand:"));
        panel.add(brandField);

        panel.add(new JLabel("Category:"));
        panel.add(categoryComboBox);

        panel.add(new JLabel("Price (₹):"));
        panel.add(priceField);

        panel.add(new JLabel("Discount (%):"));
        panel.add(discountField);

        panel.add(new JLabel("Stock Quantity:"));
        panel.add(stockField);

        panel.add(new JLabel("Description:"));
        panel.add(descField);

        JButton saveBtn = new JButton("Save Product");
        JButton cancelBtn = new JButton("Cancel");

        saveBtn.addActionListener(e -> saveProduct());
        cancelBtn.addActionListener(e -> dispose());

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        btnPanel.add(cancelBtn);
        btnPanel.add(saveBtn);

        setLayout(new BorderLayout());
        add(panel, BorderLayout.CENTER);
        add(btnPanel, BorderLayout.SOUTH);
    }

    private void saveProduct() {
        try {
            String name = nameField.getText().trim();
            String brand = brandField.getText().trim();
            int catId = categoryComboBox.getSelectedIndex() + 1;
            double price = Double.parseDouble(priceField.getText().trim());
            double discount = Double.parseDouble(discountField.getText().trim());
            int stock = Integer.parseInt(stockField.getText().trim());
            String desc = descField.getText().trim();

            if (name.isEmpty() || brand.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Name and brand cannot be empty.", "Validation", JOptionPane.WARNING_MESSAGE);
                return;
            }

            boolean ok = adminDAO.addProduct(name, brand, catId, price, discount, stock, desc);
            if (ok) {
                JOptionPane.showMessageDialog(this, "Product added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                succeeded = true;
                dispose();
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter valid numeric values for price, discount, and stock.", "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public boolean isSucceeded() {
        return succeeded;
    }
}
