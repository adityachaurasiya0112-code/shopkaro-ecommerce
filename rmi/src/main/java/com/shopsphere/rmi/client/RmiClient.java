package com.shopsphere.rmi.client;

import com.shopsphere.rmi.model.RmiProduct;
import com.shopsphere.rmi.remote.InventoryService;
import com.shopsphere.rmi.server.RmiServer;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * RMI Client: Performs remote lookup in RMI Registry and invokes remote methods.
 * Demonstrates RTU syllabus: Remote method invocation, Registry lookup, and Distributed Computing.
 */
public class RmiClient {

    public static void main(String[] args) {
        System.out.println("====================================================================");
        System.out.println("     SHOPSPHERE REMOTE INVENTORY SERVICE (JAVA RMI CLIENT)");
        System.out.println("====================================================================");
        System.out.println("Looking up service \"" + RmiServer.SERVICE_NAME + "\" on localhost:" + RmiServer.RMI_PORT + "\n");

        try {
            // 1. Locate Registry
            Registry registry = LocateRegistry.getRegistry("localhost", RmiServer.RMI_PORT);

            // 2. Lookup Remote Stub
            InventoryService service = (InventoryService) registry.lookup(RmiServer.SERVICE_NAME);
            System.out.println("[+] Successfully retrieved remote stub: " + service.getClass().getName());

            // 3. Remote Call: getProduct(1)
            System.out.println("\n[INVOCATION 1] Calling remote getProduct(1)...");
            RmiProduct product = service.getProduct(1);
            System.out.println("Result from Remote Server: " + product);

            // 4. Remote Call: getStock(1)
            System.out.println("\n[INVOCATION 2] Calling remote getStock(1)...");
            int stock = service.getStock(1);
            System.out.println("Result: Stock level = " + stock + " units");

            // 5. Remote Call: updateStock(1, stock + 5)
            System.out.println("\n[INVOCATION 3] Calling remote updateStock(1, " + (stock + 5) + ")...");
            boolean updated = service.updateStock(1, stock + 5);
            System.out.println("Result: Update status = " + updated);

            // 6. Verify Updated Stock
            int newStock = service.getStock(1);
            System.out.println("Verified new stock: " + newStock + " units");

            System.out.println("\n====================================================================");
            System.out.println("RMI Remote Invocations completed successfully!");
            System.out.println("====================================================================");
        } catch (Exception e) {
            System.err.println("RMI Client Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
