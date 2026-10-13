package com.shopsphere.network.server;

import com.shopsphere.network.model.NetworkPacket;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Multi-threaded Inventory Socket Server using java.net.ServerSocket and Object Serialization.
 * Demonstrates RTU syllabus: ServerSocket, Socket, multi-threaded client handling,
 * and transferring serialized objects over TCP.
 */
public class InventoryServer {

    public static final int PORT = 9876;
    private static final String DB_URL = System.getenv().getOrDefault("DB_URL", "jdbc:mysql://localhost:3306/shopsphere?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC");
    private static final String DB_USER = System.getenv().getOrDefault("DB_USER", "root");
    private static final String DB_PASS = System.getenv().getOrDefault("DB_PASS", "root");

    public static void main(String[] args) {
        System.out.println("====================================================================");
        System.out.println("   SHOPSPHERE INVENTORY SOCKET SERVER (java.net.ServerSocket)");
        System.out.println("====================================================================");
        System.out.println("Listening for client connections on TCP port: " + PORT);
        System.out.println("Supported commands: GET_PRODUCT, GET_STOCK, SEARCH_PRODUCT, PING");
        System.out.println("Transfers: java.io.Serializable NetworkPacket objects");
        System.out.println("Press Ctrl+C to terminate server.");
        System.out.println("--------------------------------------------------------------------");

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("[+] Client connected from: " + clientSocket.getRemoteSocketAddress());
                new Thread(new ClientHandler(clientSocket)).start();
            }
        } catch (Exception e) {
            System.err.println("Server exception: " + e.getMessage());
        }
    }

    private static class ClientHandler implements Runnable {
        private final Socket socket;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try (
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream())
            ) {
                Object obj = in.readObject();
                if (obj instanceof NetworkPacket.Request) {
                    NetworkPacket.Request req = (NetworkPacket.Request) obj;
                    System.out.println("    -> Executing Command: " + req.getCommand() + " [" + req.getArgument() + "]");
                    NetworkPacket.Response res = processCommand(req);
                    out.writeObject(res);
                    out.flush();
                }
            } catch (Exception e) {
                System.err.println("Handler error (" + socket.getRemoteSocketAddress() + "): " + e.getMessage());
            } finally {
                try {
                    socket.close();
                } catch (Exception ignored) {}
            }
        }

        private NetworkPacket.Response processCommand(NetworkPacket.Request req) {
            String cmd = req.getCommand().toUpperCase().trim();
            String arg = req.getArgument() != null ? req.getArgument().trim() : "";

            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
                switch (cmd) {
                    case "GET_PRODUCT": {
                        int id = Integer.parseInt(arg);
                        String sql = "SELECT product_id, name, brand, price, stock FROM products WHERE product_id = ?";
                        try (PreparedStatement ps = conn.prepareStatement(sql)) {
                            ps.setInt(1, id);
                            try (ResultSet rs = ps.executeQuery()) {
                                if (rs.next()) {
                                    NetworkPacket.SerializedProduct p = new NetworkPacket.SerializedProduct(
                                            rs.getInt(1), rs.getString(2), rs.getString(3), rs.getDouble(4), rs.getInt(5)
                                    );
                                    return new NetworkPacket.Response(true, "Product found", p);
                                }
                            }
                        }
                        return new NetworkPacket.Response(false, "Product ID #" + id + " not found", null);
                    }

                    case "GET_STOCK": {
                        int id = Integer.parseInt(arg);
                        String sql = "SELECT stock FROM products WHERE product_id = ?";
                        try (PreparedStatement ps = conn.prepareStatement(sql)) {
                            ps.setInt(1, id);
                            try (ResultSet rs = ps.executeQuery()) {
                                if (rs.next()) {
                                    int stock = rs.getInt(1);
                                    return new NetworkPacket.Response(true, "Stock level retrieved", stock);
                                }
                            }
                        }
                        return new NetworkPacket.Response(false, "Product not found", 0);
                    }

                    case "SEARCH_PRODUCT": {
                        ArrayList<NetworkPacket.SerializedProduct> list = new ArrayList<>();
                        String sql = "SELECT product_id, name, brand, price, stock FROM products WHERE status = 1 AND (name LIKE ? OR brand LIKE ?)";
                        try (PreparedStatement ps = conn.prepareStatement(sql)) {
                            String like = "%" + arg + "%";
                            ps.setString(1, like);
                            ps.setString(2, like);
                            try (ResultSet rs = ps.executeQuery()) {
                                while (rs.next()) {
                                    list.add(new NetworkPacket.SerializedProduct(
                                            rs.getInt(1), rs.getString(2), rs.getString(3), rs.getDouble(4), rs.getInt(5)
                                    ));
                                }
                            }
                        }
                        return new NetworkPacket.Response(true, "Found " + list.size() + " matches", list);
                    }

                    case "PING":
                        return new NetworkPacket.Response(true, "PONG", System.currentTimeMillis());

                    default:
                        return new NetworkPacket.Response(false, "Unknown command: " + cmd, null);
                }
            } catch (Exception ex) {
                // Fallback demonstration response in case DB server is not currently reachable
                return fallbackResponse(cmd, arg);
            }
        }

        private NetworkPacket.Response fallbackResponse(String cmd, String arg) {
            if ("GET_PRODUCT".equals(cmd)) {
                NetworkPacket.SerializedProduct p = new NetworkPacket.SerializedProduct(1, "Boat Airdopes 141 ANC", "Boat", 1499.00, 45);
                return new NetworkPacket.Response(true, "Simulated fallback product", p);
            } else if ("GET_STOCK".equals(cmd)) {
                return new NetworkPacket.Response(true, "Simulated stock", 45);
            } else if ("SEARCH_PRODUCT".equals(cmd)) {
                ArrayList<NetworkPacket.SerializedProduct> list = new ArrayList<>();
                list.add(new NetworkPacket.SerializedProduct(1, "Boat Airdopes 141 ANC", "Boat", 1499.00, 45));
                list.add(new NetworkPacket.SerializedProduct(2, "Noise ColorFit Pro 4", "Noise", 2999.00, 30));
                return new NetworkPacket.Response(true, "Simulated search results", list);
            }
            return new NetworkPacket.Response(true, "PONG", System.currentTimeMillis());
        }
    }
}
