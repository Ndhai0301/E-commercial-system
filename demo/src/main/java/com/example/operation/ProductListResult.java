package com.example.operation;

import java.util.List;

import org.json.JSONObject;

public class ProductListResult {
    private List<JSONObject> products;
    private int currentPage;
    private int totalPages;

    public ProductListResult(List<JSONObject> products, int currentPage, int totalPages) {
        this.products = products;
        this.currentPage = currentPage;
        this.totalPages = totalPages;
    }

    public List<JSONObject> getProducts() {
        return products;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public int getTotalPages() {
        return totalPages;
    }
}
