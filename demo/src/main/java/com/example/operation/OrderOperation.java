package com.example.operation;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;
import org.json.JSONObject;

import com.example.model.Order;

public class OrderOperation {
    public static OrderOperation instance;
    private static final String ORDER_FILE = "demo/data/orders.txt";
    private static final int PAGE_SIZE = 10;
    public OrderOperation(){};

    public static OrderOperation getInstance(){
        if(instance == null){
            instance = new OrderOperation();
        }
        return instance;
    }


    public String generateUniqueOrderId() {
        Set<String> existingIds = loadExistingOrderIds();
        int maxId = 0;

        for (String id : existingIds) {
            try {
                int number = Integer.parseInt(id.substring(2)); // "o_00025" → 25
                if (number > maxId) {
                    maxId = number;
                }
            } catch (NumberFormatException ignored) {}
        }

        int newIdNumber = maxId + 1;

        if (newIdNumber > 99999) {
            throw new RuntimeException("Order ID limit reached.");
        }

        return String.format("o_%05d", newIdNumber); 
    }

    private Set<String> loadExistingOrderIds() {
        Set<String> ids = new HashSet<>();
        File file = new File(ORDER_FILE);

        if (!file.exists()) return ids;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                int keyIndex = line.indexOf("\"order_id\"");
                if (keyIndex != -1) {
                    int colonIndex = line.indexOf(":", keyIndex);
                    int quoteStart = line.indexOf("\"", colonIndex + 1);
                    int quoteEnd = line.indexOf("\"", quoteStart + 1);
                    if (quoteStart != -1 && quoteEnd != -1) {
                        String orderId = line.substring(quoteStart + 1, quoteEnd);
                        ids.add(orderId);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ids;
    }

    public boolean createAnOrder(String customerId,String productId,String createTime){
        
        try {
            if (createTime == null || createTime.trim().isEmpty()) {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy_HH:mm:ss");
                    createTime = sdf.format(new Date());
            }
            File file = new File(ORDER_FILE);

            JSONObject newOrder = new JSONObject();
            newOrder.put("order_id",generateUniqueOrderId());
            newOrder.put("user_id",customerId);
            newOrder.put("pro_id",productId);
            newOrder.put("order_time",createTime);

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(ORDER_FILE, true))) {
                writer.write(newOrder.toString());
                writer.newLine();
            }
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }

    public boolean deleteOrder(String orderId) {
        try {
            List<String> lines = Files.readAllLines(Paths.get(ORDER_FILE));
            List<String> updatedLines = lines.stream()
                .filter(line -> {
                    try {
                        return !new JSONObject(line).getString("order_id").equals(orderId);
                    } catch (Exception e) {
                        return true; 
                    }
                })
                .collect(Collectors.toList());
    
            boolean deleted = lines.size() != updatedLines.size();
    
            if (deleted) {
                Files.write(Paths.get(ORDER_FILE), updatedLines);
            }
    
            return deleted;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    

    
    public OrderListResult getOrderList(String customerId, int pageNumber) {
        List<Order> allOrders = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(ORDER_FILE))) {
            String line;

            while ((line = reader.readLine()) != null) {

                String orderId = extractJsonValue(line, "order_id");
                String userId = extractJsonValue(line, "user_id");
                String productId = extractJsonValue(line, "pro_id");
                String orderTime = extractJsonValue(line, "order_time");

                if (userId != null && userId.equals(customerId)) {
                    allOrders.add(new Order(orderId, userId, productId, orderTime));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        
        int totalOrders = allOrders.size();
        int totalPages = (int) Math.ceil((double) totalOrders / PAGE_SIZE);

        if (pageNumber < 1) pageNumber = 1;
        if (pageNumber > totalPages) pageNumber = totalPages;

        int startIndex = (pageNumber - 1) * PAGE_SIZE;
        int endIndex = Math.min(startIndex + PAGE_SIZE, totalOrders);
        List<Order> pageOrders = (startIndex < totalOrders) ? allOrders.subList(startIndex, endIndex) : new ArrayList<>();

        return new OrderListResult(pageOrders, pageNumber, totalPages);
    }

    private String extractJsonValue(String jsonLine, String key) {
        int keyIndex = jsonLine.indexOf("\"" + key + "\"");
        if (keyIndex == -1) return null;
        int colonIndex = jsonLine.indexOf(":", keyIndex);
        int quoteStart = jsonLine.indexOf("\"", colonIndex + 1);
        int quoteEnd = jsonLine.indexOf("\"", quoteStart + 1);
        if (quoteStart != -1 && quoteEnd != -1) {
            return jsonLine.substring(quoteStart + 1, quoteEnd);
        }
        return null;
    }

    public void generateTestCustomers() {
        File userFile = new File(ORDER_FILE);
        int nextId = getNextCustomerId(userFile);
        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(userFile, true))) {
            for (int i = 0; i < 10; i++) {
                String userId = String.format("u_%010d", nextId++);
                String username = "test_user_" + i;
                String password = "^^TestPass" + i + "$$";
                String email = username + "@example.com";
                String mobile = String.format("049%07d", i);
                String registerTime = new SimpleDateFormat("dd-MM-yyyy_HH:mm:ss").format(new Date());
    
                JSONObject obj = new JSONObject();
                obj.put("user_id", userId);
                obj.put("user_name", username);
                obj.put("user_password", password);
                obj.put("user_register_time", registerTime);
                obj.put("user_role", "customer");
                obj.put("user_email", email);
                obj.put("user_mobile", mobile);
    
                writer.write(obj.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private int getNextCustomerId(File userFile) {
        int max = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader("demo/data/users.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("\"user_id\":\"u_")) {
                    int start = line.indexOf("u_") + 2;
                    int end = line.indexOf("\"", start);
                    String idStr = line.substring(start, end);
                    try {
                        int id = Integer.parseInt(idStr);
                        if (id > max) max = id;
                    } catch (NumberFormatException ignored) {}
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return max + 1;
    }
    public void generateSingleCustomerConsumptionFigure(String customerId) {
        double[] monthTotal = new double[12];
        Map<String, Double> priceMap = loadProductPrices();

        try (BufferedReader reader = new BufferedReader(new FileReader(ORDER_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.contains(customerId)) continue;

                String proId = extractJsonValue(line, "pro_id");
                String orderTime = extractJsonValue(line, "order_time");
                int month = Integer.parseInt(orderTime.substring(3, 5)) - 1;

                Double price = priceMap.get(proId);
                if (price != null) monthTotal[month] += price;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        drawBarChart(monthTotal, "Monthly Consumption for Customer " + customerId, "Month", "Total ($)");
    }

    private Map<String, Double> loadProductPrices() {
        Map<String, Double> map = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("demo/data/products.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String id = extractJsonValue(line, "pro_id");
                String priceStr = extractJsonValue(line, "pro_current_price");
                if (id != null && priceStr != null) {
                    map.put(id, Double.parseDouble(priceStr));
            }
            }
        } catch (IOException e) {
        e.printStackTrace();
        }
    return map;
    }
    public void generateAllTop10BestSellersFigure() {
        Map<String, Integer> proCount = new HashMap<>();
    
        try (BufferedReader reader = new BufferedReader(new FileReader(ORDER_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String proId = extractJsonValue(line, "pro_id");
                if (proId != null) {
                    proCount.put(proId, proCount.getOrDefault(proId, 0) + 1);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    
        List<Map.Entry<String, Integer>> top = new ArrayList<>(proCount.entrySet());
        top.sort((a, b) -> b.getValue() - a.getValue());
        int limit = Math.min(10, top.size());
    
        String[] labels = new String[limit];
        double[] values = new double[limit];
        int index = 0;
        for (int i = 0; i < top.size() && index < limit; i++) {
            String id = top.get(i).getKey();
            Integer count = top.get(i).getValue();
            if (id != null) {
                labels[index] = id;
                values[index] = count;
                index++;
            }
        }
    
        drawBarChart(labels, values, "Top 10 Best-Selling Products", "Product ID", "Units Sold");
    }
    
    public void deleteAllOrders() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ORDER_FILE))) {
            writer.write(""); 
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void drawBarChart(double[] values, String title, String xAxisLabel, String yAxisLabel) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (int i = 0; i < values.length; i++) {
            String monthLabel = String.format("%02d", i + 1); // "01", "02", ...
            dataset.addValue(values[i], "Total", monthLabel);
        }

        JFreeChart chart = ChartFactory.createBarChart(
            title, xAxisLabel, yAxisLabel, dataset
        );

        ChartFrame frame = new ChartFrame(title, chart);
        frame.pack();
        frame.setVisible(true);
    }
    private void drawBarChart(String[] labels, double[] values, String title, String xAxisLabel, String yAxisLabel) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (int i = 0; i < labels.length; i++) {
            dataset.addValue(values[i], "Units", labels[i]);
        }
    
        JFreeChart chart = ChartFactory.createBarChart(
                title, xAxisLabel, yAxisLabel, dataset
        );
    
        ChartFrame frame = new ChartFrame(title, chart);
        frame.pack();
        frame.setVisible(true);
    }
    
}

