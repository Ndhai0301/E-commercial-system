package util;

import java.util.List;

import model.Product;

public class ProductListResult {
    private List<Product> productList;
    private int currentPage;
    private int totalPages;

    // Constructor
    public ProductListResult(List<Product> productList, int currentPage, int totalPages) {
        this.productList = productList;
        this.currentPage = currentPage;
        this.totalPages = totalPages;
    }

    // Getters
    public List<Product> getProductList() {
        return productList;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public int getTotalPages() {
        return totalPages;
    }

    // Optional: toString() for debugging
    @Override
    public String toString() {
        return "Page " + currentPage + " of " + totalPages + ": " + productList.size() + " items";
    }
}
