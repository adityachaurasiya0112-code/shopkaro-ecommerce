package com.shopsphere.controller;

import com.shopsphere.model.Category;
import com.shopsphere.model.Product;
import com.shopsphere.service.CategoryService;
import com.shopsphere.service.ProductService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Controller for Product catalog, search, filter and sort.
 */
@WebServlet("/products")
public class ProductServlet extends HttpServlet {

    private final ProductService productService = new ProductService();
    private final CategoryService categoryService = new CategoryService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String query = req.getParameter("q");
        String categoryParam = req.getParameter("cat");
        String minPriceParam = req.getParameter("minPrice");
        String maxPriceParam = req.getParameter("maxPrice");
        String sort = req.getParameter("sort");

        Integer categoryId = null;
        if (categoryParam != null && !categoryParam.isBlank()) {
            try { categoryId = Integer.parseInt(categoryParam); } catch (NumberFormatException ignored) {}
        }

        Double minPrice = null;
        if (minPriceParam != null && !minPriceParam.isBlank()) {
            try { minPrice = Double.parseDouble(minPriceParam); } catch (NumberFormatException ignored) {}
        }

        Double maxPrice = null;
        if (maxPriceParam != null && !maxPriceParam.isBlank()) {
            try { maxPrice = Double.parseDouble(maxPriceParam); } catch (NumberFormatException ignored) {}
        }

        try {
            List<Product> products = productService.search(query, categoryId, minPrice, maxPrice, sort);
            List<Category> categories = categoryService.getAllActive();

            req.setAttribute("products", products);
            req.setAttribute("categories", categories);
            req.setAttribute("selectedCategory", categoryId);
            req.setAttribute("selectedSort", sort);

            req.getRequestDispatcher("/products.jsp").forward(req, resp);
        } catch (SQLException e) {
            throw new ServletException("Error querying products", e);
        }
    }
}
