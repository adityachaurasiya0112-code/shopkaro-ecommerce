package com.shopsphere.network.model;

import java.io.Serializable;
import java.util.List;

/**
 * Serializable data transfer objects for Java Socket Client-Server communication.
 * Demonstrates RTU syllabus: java.io.Serializable and Object serialization over sockets.
 */
public class NetworkPacket implements Serializable {
    private static final long serialVersionUID = 1L;

    public static class Request implements Serializable {
        private static final long serialVersionUID = 1L;
        private final String command; // GET_PRODUCT, GET_STOCK, SEARCH_PRODUCT
        private final String argument;

        public Request(String command, String argument) {
            this.command = command;
            this.argument = argument;
        }

        public String getCommand() { return command; }
        public String getArgument() { return argument; }

        @Override
        public String toString() {
            return command + " " + (argument != null ? argument : "");
        }
    }

    public static class Response implements Serializable {
        private static final long serialVersionUID = 1L;
        private final boolean success;
        private final String message;
        private final Serializable payload; // Product, Integer, or List<Product>

        public Response(boolean success, String message, Serializable payload) {
            this.success = success;
            this.message = message;
            this.payload = payload;
        }

        public boolean isSuccess() { return success; }
        public String getMessage() { return message; }
        public Serializable getPayload() { return payload; }

        @Override
        public String toString() {
            return "Response[success=" + success + ", message=" + message + ", payload=" + payload + "]";
        }
    }

    public static class SerializedProduct implements Serializable {
        private static final long serialVersionUID = 1L;
        private final int id;
        private final String name;
        private final String brand;
        private final double price;
        private final int stock;

        public SerializedProduct(int id, String name, String brand, double price, int stock) {
            this.id = id;
            this.name = name;
            this.brand = brand;
            this.price = price;
            this.stock = stock;
        }

        public int getId() { return id; }
        public String getName() { return name; }
        public String getBrand() { return brand; }
        public double getPrice() { return price; }
        public int getStock() { return stock; }

        @Override
        public String toString() {
            return String.format("#%d: %s (%s) - ₹%.2f [Stock: %d]", id, name, brand, price, stock);
        }
    }
}
