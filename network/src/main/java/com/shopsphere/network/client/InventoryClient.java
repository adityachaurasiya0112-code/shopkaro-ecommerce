package com.shopsphere.network.client;

import com.shopsphere.network.model.NetworkPacket;
import com.shopsphere.network.server.InventoryServer;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

/**
 * Socket Client for querying ShopSphere Inventory Server.
 * Demonstrates RTU syllabus: java.net.Socket, ObjectInputStream, and ObjectOutputStream.
 */
public class InventoryClient {

    private static final String HOST = "localhost";
    private static final int PORT = InventoryServer.PORT;

    public static NetworkPacket.Response sendCommand(String command, String argument) {
        try (
            Socket socket = new Socket(HOST, PORT);
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream())
        ) {
            NetworkPacket.Request req = new NetworkPacket.Request(command, argument);
            out.writeObject(req);
            out.flush();

            Object res = in.readObject();
            if (res instanceof NetworkPacket.Response) {
                return (NetworkPacket.Response) res;
            }
        } catch (Exception e) {
            return new NetworkPacket.Response(false, "Connection error: " + e.getMessage(), null);
        }
        return new NetworkPacket.Response(false, "Unknown response type", null);
    }

    public static void main(String[] args) {
        System.out.println("====================================================================");
        System.out.println("      SHOPSPHERE INVENTORY SOCKET CLIENT (java.net.Socket)");
        System.out.println("====================================================================");
        System.out.println("Connecting to server at: " + HOST + ":" + PORT + "\n");

        // 1. Test GET_PRODUCT
        System.out.println("[TEST 1] Sending Command: GET_PRODUCT 1");
        NetworkPacket.Response r1 = sendCommand("GET_PRODUCT", "1");
        System.out.println("Status:  " + (r1.isSuccess() ? "SUCCESS" : "FAILED"));
        System.out.println("Message: " + r1.getMessage());
        System.out.println("Payload: " + r1.getPayload() + "\n");

        // 2. Test GET_STOCK
        System.out.println("[TEST 2] Sending Command: GET_STOCK 1");
        NetworkPacket.Response r2 = sendCommand("GET_STOCK", "1");
        System.out.println("Status:  " + (r2.isSuccess() ? "SUCCESS" : "FAILED"));
        System.out.println("Message: " + r2.getMessage());
        System.out.println("Stock:   " + r2.getPayload() + " units available\n");

        // 3. Test SEARCH_PRODUCT
        System.out.println("[TEST 3] Sending Command: SEARCH_PRODUCT Earbuds");
        NetworkPacket.Response r3 = sendCommand("SEARCH_PRODUCT", "Earbuds");
        System.out.println("Status:  " + (r3.isSuccess() ? "SUCCESS" : "FAILED"));
        System.out.println("Message: " + r3.getMessage());
        if (r3.getPayload() instanceof List) {
            List<?> list = (List<?>) r3.getPayload();
            for (Object item : list) {
                System.out.println("   * " + item);
            }
        }
        System.out.println("====================================================================");
    }
}
