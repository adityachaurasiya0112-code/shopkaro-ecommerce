package com.shopsphere.rmi.server;

import com.shopsphere.rmi.remote.InventoryService;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * RMI Server: Initializes RMI Registry and binds remote InventoryService.
 * Demonstrates RTU syllabus: RMI Registry, Naming/Registry binding, and Remote Server.
 */
public class RmiServer {

    public static final int RMI_PORT = 1099;
    public static final String SERVICE_NAME = "InventoryService";

    public static void main(String[] args) {
        System.out.println("====================================================================");
        System.out.println("     SHOPSPHERE REMOTE INVENTORY SERVICE (JAVA RMI SERVER)");
        System.out.println("====================================================================");

        try {
            // 1. Create or obtain RMI Registry on port 1099
            Registry registry;
            try {
                registry = LocateRegistry.createRegistry(RMI_PORT);
                System.out.println("[+] RMI Registry successfully started on port " + RMI_PORT);
            } catch (Exception e) {
                registry = LocateRegistry.getRegistry(RMI_PORT);
                System.out.println("[i] Attached to existing RMI Registry on port " + RMI_PORT);
            }

            // 2. Instantiate Remote Object
            InventoryService service = new InventoryServiceImpl();

            // 3. Bind Remote Object to Registry
            registry.rebind(SERVICE_NAME, service);
            System.out.println("[+] Remote service bound to name: \"" + SERVICE_NAME + "\"");
            System.out.println("[+] URL: rmi://localhost:" + RMI_PORT + "/" + SERVICE_NAME);
            System.out.println("--------------------------------------------------------------------");
            System.out.println("RMI Server is ready and awaiting client invocations...");
            System.out.println("Press Ctrl+C to stop.");
            System.out.println("====================================================================");
        } catch (Exception e) {
            System.err.println("RMI Server Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
