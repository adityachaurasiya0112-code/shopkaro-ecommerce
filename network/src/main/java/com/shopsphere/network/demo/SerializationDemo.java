package com.shopsphere.network.demo;

import com.shopsphere.network.model.NetworkPacket;

import java.io.*;

/**
 * Standalone Demonstration of Java Object Serialization and Deserialization.
 * Demonstrates RTU syllabus: java.io.Serializable, ObjectOutputStream, ObjectInputStream,
 * and byte stream transfer.
 */
public class SerializationDemo {

    public static void main(String[] args) {
        System.out.println("====================================================================");
        System.out.println("     JAVA OBJECT SERIALIZATION & DESERIALIZATION DEMONSTRATION");
        System.out.println("====================================================================");

        // 1. Create instance of serializable class
        NetworkPacket.SerializedProduct originalProduct = new NetworkPacket.SerializedProduct(
                101, "Boat Airdopes 141 ANC", "Boat", 1499.00, 45
        );
        System.out.println("[ORIGINAL OBJECT]: " + originalProduct);

        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();

        // 2. Serialize object into byte stream
        try (ObjectOutputStream out = new ObjectOutputStream(byteOut)) {
            out.writeObject(originalProduct);
            out.flush();
            System.out.println("\n[+] Object successfully serialized into byte stream.");
            System.out.println("[+] Serialized byte payload size: " + byteOut.size() + " bytes.");
        } catch (IOException e) {
            System.err.println("Serialization failed: " + e.getMessage());
            return;
        }

        // 3. Deserialize object from byte stream
        byte[] bytes = byteOut.toByteArray();
        try (ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(bytes))) {
            Object deserializedObj = in.readObject();
            if (deserializedObj instanceof NetworkPacket.SerializedProduct) {
                NetworkPacket.SerializedProduct restoredProduct = (NetworkPacket.SerializedProduct) deserializedObj;
                System.out.println("\n[+] Object successfully deserialized back from byte stream!");
                System.out.println("[RESTORED OBJECT]: " + restoredProduct);
                System.out.println("Verification (ID matches): " + (restoredProduct.getId() == originalProduct.getId()));
            }
        } catch (Exception e) {
            System.err.println("Deserialization failed: " + e.getMessage());
        }

        System.out.println("====================================================================");
    }
}
