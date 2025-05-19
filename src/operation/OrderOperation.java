package operation;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import data.databaseWork;
import model.Customer;
import model.Order;
import util.CustomerListResult;
import util.OrderListResult;

public class OrderOperation {
    private static OrderOperation instance;

    private static String orderFilePath = "data/orders.txt";

    private OrderOperation(){}

    public static OrderOperation getInstance() {
        if (instance == null){
            instance = new OrderOperation();
        }        
        return instance;
    }

    /**
    * Generates and returns a 5-digit unique order id starting with "o_".
    *@return A string such as o_12345
    */
    public String generateUniqueOrderId(){
        Random rand = new Random();
        int number = 10000 + rand.nextInt(90000);
        return "o_" + number;
    }

    

    /**
    *Creates a new order with a unique order id and saves it to the
    *data/orders.txt file.
    *@paramcustomerId The ID of the customer making the order
    *@paramproductIdThe ID of the product being ordered
    *@paramcreateTime The time of order creation (null for current time)
    *@return true if successful,false otherwise
    */
    public boolean createAnOrder(String customerId,String productId, String createTime){
        String orderId = generateUniqueOrderId();
        // Nếu createTime là null → dùng thời gian hiện tại
        if (createTime == null || createTime.isEmpty()) {
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy_HH:mm:ss");
            createTime = now.format(formatter);
        }

        // Tạo chuỗi JSON-like để ghi vào file
        String orderLine = String.format(
                "{\"order_id\":\"%s\",\"user_id\":\"%s\",\"pro_id\":\"%s\",\"order_time\":\"%s\"}",
                orderId, customerId, productId, createTime
        );

        return databaseWork.addOneLine(orderLine.toString(), orderFilePath);
    }

    /**
    *Deletes the order from the data/orders.txt file based on order_id.
    *@paramorderId The ID of the order to delete
    *@return true if successful,false otherwise
    */
    public boolean deleteOrder(String orderId){
        return databaseWork.deleteOneLine(orderId, orderFilePath);
    }

    /**
     *Retrieves one page of orders from the database belonging to the
    *given customer. One page contains a maximum of 10items.
    *@paramcustomerId The ID of the customer
    *@parampageNumber The page number to retrieve
    *@return A list of Order objects,current page number,and total pages
    */
    public OrderListResult getOrderList(String customerId, int pageNumber) {
        List<Order> matchedOrders = new ArrayList<>();
        int pageSize = 10;

        try (BufferedReader reader = new BufferedReader(new FileReader("data/orders.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String userId = databaseWork.extractField(line, "user_id");
                if (userId.equals(customerId)) {
                    matchedOrders.add(parseOrderFromLine(line));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        int totalOrders = matchedOrders.size();
        int totalPages = (int) Math.ceil((double) totalOrders / pageSize);
        pageNumber = Math.max(1, Math.min(pageNumber, totalPages)); // giới hạn trong khoảng hợp lệ

        int fromIndex = (pageNumber - 1) * pageSize;
        int toIndex = Math.min(fromIndex + pageSize, totalOrders);
        List<Order> pageOrders = matchedOrders.subList(fromIndex, toIndex);

        return new OrderListResult(pageOrders, pageNumber, totalPages);
    }
    private Order parseOrderFromLine(String line) {

        try {
            String orderId = databaseWork.extractField(line, "order_id");
            String userId = databaseWork.extractField(line, "user_id");
            String proId = databaseWork.extractField(line, "pro_id");
            String orderTime = databaseWork.extractField(line, "order_time");

            return new Order(orderId, userId, proId, orderTime);
        } catch (Exception e) {
            return null;
        }
    }

    /**
    *Automatically generates test data including customers and orders.
    *Creates 10customers and randomly generates 50-200orders for each.
    *Order times should be scattered across different months of the year.
    */
    public void generateTestOrderData() {
        // Implementation
    }

    /**
    * Generates a chart showing the consumption (sum of order prices)
    * across 12 different months for the given customer.
    * @param customerId The ID of the customer
    */
    public void generateSingleCustomerConsumptionFigure(String customerId) {
    // Implementation using Java charting library
    }

    /**
     * Generates a chart showing the consumption (sum of order prices)
     * across 12 different months for all customers.
     */
    public void generateAllCustomersConsumptionFigure() {
    // Implementation using Java charting library
    }

     /**
     * Generates a graph showing the top 10 best-selling products
     * sorted in descending order.
     */
    public void generateAllTop10BestSellersFigure() {
    // Implementation using Java charting library
    }

    /**
    * Removes all data in the data/orders.txt file.
    */
    public void deleteAllOrders() {
        File orderFile = new File("data/orders.txt");
    
        try {
            if (!orderFile.exists()) {
                System.out.println("File products does not exist");
                return;
            }           
            
            FileWriter writer = new FileWriter(orderFile);
            writer.write(""); 
            writer.close();
            
            System.out.println("    deleted successfully.");
        } catch (IOException e) {
            System.err.println("Error deleting products: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
