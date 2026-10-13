package com.shopsphere.service;

import com.shopsphere.dao.CartDAO;
import com.shopsphere.dao.WishlistDAO;
import com.shopsphere.model.WishlistItem;

import java.sql.SQLException;
import java.util.List;

/**
 * Service Layer: Wishlist operations.
 */
public class WishlistService {

    private final WishlistDAO wishlistDAO = new WishlistDAO();
    private final CartDAO cartDAO = new CartDAO();

    public List<WishlistItem> getWishlist(int userId) throws SQLException {
        return wishlistDAO.list(userId);
    }

    public boolean addToWishlist(int userId, int productId) throws SQLException {
        return wishlistDAO.add(userId, productId);
    }

    public boolean removeFromWishlist(int userId, int productId) throws SQLException {
        return wishlistDAO.remove(userId, productId);
    }

    public boolean moveToCart(int userId, int productId) throws SQLException {
        cartDAO.add(userId, productId, 1);
        return wishlistDAO.remove(userId, productId);
    }

    public boolean isInWishlist(int userId, int productId) throws SQLException {
        return wishlistDAO.exists(userId, productId);
    }

    public int getWishlistCount(int userId) throws SQLException {
        return wishlistDAO.getCount(userId);
    }
}
