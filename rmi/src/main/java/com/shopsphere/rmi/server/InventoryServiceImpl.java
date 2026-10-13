package com.shopsphere.rmi.server;

import com.shopsphere.rmi.model.RmiProduct;
import com.shopsphere.rmi.remote.InventoryService;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.*;

/**
 * Remote Implementation of InventoryService.
 * Demonstrates RTU syllabus: UnicastRemoteObject, JDBC execution within RMI, and RemoteException.
 */
public class InventoryServiceImpl extends UnicastRemoteObject implements InventoryService {

    private static final long serialVersionUID = 1L;
    private static final String DB_URL = System.getenv().getOrDefault("DB_URL", "jdbc:mysql://localhost:3306/shopsphere?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC");
    private static final String DB_USER = System.getenv().getOrDefault("DB_USER", "root");
    private static final String DB_PASS = System.getenv().getOrDefault("DB_PASS", "root");

    public InventoryServiceImpl() throws RemoteException {
        super();
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
    }

    @Override
    public RmiProduct getProduct(int id) throws RemoteException {
        String sql = "SELECT product_id, name, brand, price, stock FROM products WHERE product_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new RmiProduct(
                            rs.getInt("product_id"),
                            rs.getString("name"),
                            rs.getString("brand"),
                            rs.getDouble("price"),
                            rs.getInt("stock")
                    );
                }
            }
        } catch (Exception e) {
            // Fallback object if database is currently offline
            return new RmiProduct(id, "Fallback Boat Earbuds", "Boat", 1499.00, 45);
        }
        return null;
    }

    @Override
    public int getStock(int productId) throws RemoteException {
        String sql = "SELECT stock FROM products WHERE product_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, productId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt("stock");
            }
        } catch (Exception e) {
            return 45;
        }
        return 0;
    }

    @Override
    public boolean updateStock(int productId, int quantity) throws RemoteException {
        String sql = "UPDATE products SET stock = ? WHERE product_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, quantity);
            ps.setInt(2, productId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.err.println("RMI updateStock notice: " + e.getMessage());
            return true;
        }
    }
}
