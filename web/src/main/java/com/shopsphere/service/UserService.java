package com.shopsphere.service;

import com.shopsphere.dao.AddressDAO;
import com.shopsphere.dao.UserDAO;
import com.shopsphere.model.Address;
import com.shopsphere.model.User;

import java.sql.SQLException;
import java.util.List;

/**
 * Service Layer: Customer management and addresses.
 */
public class UserService {

    private final UserDAO userDAO = new UserDAO();
    private final AddressDAO addressDAO = new AddressDAO();

    public User getUser(int userId) throws SQLException {
        return userDAO.getById(userId);
    }

    public List<User> getAllUsers() throws SQLException {
        return userDAO.getAll();
    }

    public boolean toggleUserStatus(int userId, int newStatus) throws SQLException {
        return userDAO.updateStatus(userId, newStatus);
    }

    public List<Address> getUserAddresses(int userId) throws SQLException {
        return addressDAO.getByUserId(userId);
    }

    public Address getAddress(int addressId) throws SQLException {
        return addressDAO.getById(addressId);
    }

    public int addAddress(int userId, String addressLine, String city, String state, String pincode, String type) throws SQLException {
        if (addressLine == null || addressLine.isBlank() || city == null || city.isBlank() || pincode == null || pincode.isBlank()) {
            throw new IllegalArgumentException("Address line, city and pincode are required.");
        }
        Address addr = new Address();
        addr.setUserId(userId);
        addr.setAddressLine(addressLine.trim());
        addr.setCity(city.trim());
        addr.setState(state != null ? state.trim() : "");
        addr.setPincode(pincode.trim());
        addr.setAddressType(type != null ? type.trim().toUpperCase() : "HOME");
        return addressDAO.insert(addr);
    }

    public boolean deleteAddress(int addressId, int userId) throws SQLException {
        return addressDAO.delete(addressId, userId);
    }
}
