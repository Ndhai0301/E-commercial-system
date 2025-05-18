package com.example.operation;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.json.JSONObject;

public class ProductOperation {
    public static ProductOperation instance;
    private final String PRODUCT_FILE = "demo/data/products.txt";
    public ProductOperation(){}

    public static  ProductOperation getInstance(){
        if(instance == null){
            instance = new ProductOperation();
        }
        return instance;
    }

    public void extractProductsFromFiles(){
        String rawFile = "demo/data/product.txt"; 

        try (BufferedReader reader = new BufferedReader(new FileReader(rawFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(PRODUCT_FILE))) {

            String line;
            while ((line = reader.readLine()) != null) {
                try {
                    JSONObject json = new JSONObject(line);
                    writer.write(json.toString());
                    writer.newLine();
                } catch (Exception e) {
                    
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ProductListResult getProductList(int pageNumber) {
        List<JSONObject> allProducts = readAllProducts();
        int totalPages = (int) Math.ceil(allProducts.size() / 10.0);
        pageNumber = Math.max(1, Math.min(pageNumber, totalPages));

        int start = (pageNumber - 1) * 10;
        int end = Math.min(start + 10, allProducts.size());
        List<JSONObject> page = allProducts.subList(start, end);

        return new ProductListResult(page, pageNumber, totalPages);
    }

    public boolean deleteProduct(String productId) {
        List<JSONObject> products = readAllProducts();
        boolean removed = products.removeIf(p -> productId.equals(p.optString("pro_id")));
        if (removed) {
            writeAllProducts(products);
        }
        return removed;
    }

    public List<JSONObject> getProductListByKeyword(String keyword) {
        return readAllProducts().stream()
                .filter(p -> p.optString("pro_name").toLowerCase().contains(keyword.toLowerCase()))
                .collect(Collectors.toList());
    }

    public JSONObject getProductById(String productId) {
        return readAllProducts().stream()
                .filter(p -> productId.equals(p.optString("pro_id")))
                .findFirst().orElse(null);
    }


    public void deleteAllProducts() {
        try {
            new PrintWriter(PRODUCT_FILE).close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private List<JSONObject> readAllProducts() {
        List<JSONObject> list = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(PRODUCT_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                try {
                    list.add(new JSONObject(line));
                } catch (Exception e) {
                    System.err.println("Dòng lỗi JSON: " + line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    private void writeAllProducts(List<JSONObject> products) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(PRODUCT_FILE))) {
            for (JSONObject obj : products) {
                writer.write(obj.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
