package com.shopsphere.rmi.remote;

import com.shopsphere.rmi.model.RmiProduct;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Remote Interface for ShopSphere Remote Inventory Service.
 * Demonstrates RTU syllabus: java.rmi.Remote, RemoteException, and RMI Architecture.
 */
public interface InventoryService extends Remote {

    RmiProduct getProduct(int id) throws RemoteException;

    int getStock(int productId) throws RemoteException;

    boolean updateStock(int productId, int quantity) throws RemoteException;
}
