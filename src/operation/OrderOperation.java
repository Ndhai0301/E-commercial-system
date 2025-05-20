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
import model.Order;
import model.Product;
import util.OrderListResult;

public class OrderOperation {
    private static OrderOperation instance;

    private static String orderFilePath = "src/data/orders.txt";

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

        try (BufferedReader reader = new BufferedReader(new FileReader("src/data/orders.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String userId = databaseWork.extractField(line, "user_id");
                if (userId.contains(customerId)) {
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
        CustomerOperation customerOp = CustomerOperation.getInstance();
        ProductOperation productOp = ProductOperation.getInstance();
        UserOperation userOp = UserOperation.getInstance();

        List<Product> allProducts = productOp.getProductListByKeyword("");
        if (allProducts.isEmpty()) {
            return;
        }

        Random rand = new Random();

        for (int i = 1; i <= 10; i++) {
            String username = "tester" + i;
            String password = "Pass" + i + "123";
            String email = "testuser" + i + "@example.com";
            String mobile = "04" + (10000000 + rand.nextInt(89999999));

            boolean registered = customerOp.registerCustomer(username, password, email, mobile);

            if (!registered) {
                continue;
            }

            int numOrders = 50 + rand.nextInt(151);
            for (int j = 0; j < numOrders; j++) {
                String orderId = generateUniqueOrderId();

                
                Product product = allProducts.get(rand.nextInt(allProducts.size()));
                String proId = product.getProId();


                int year = LocalDateTime.now().getYear();
                int month = 1 + rand.nextInt(12);
                int day = 1 + rand.nextInt(28); 
                int hour = rand.nextInt(24);
                int minute = rand.nextInt(60);
                int second = rand.nextInt(60);
                String time = String.format("%02d-%02d-%d_%02d:%02d:%02d", day, month, year, hour, minute, second);

                Order order = new Order(orderId, userOp.generateUniqueUserId(), proId, time);

                try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/data/orders.txt", true))) {
                    writer.write(order.toString());
                    writer.newLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
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
        File orderFile = new File("src/data/orders.txt");
    
        try {
            if (!orderFile.exists()) {
                return;
            }           
            
            FileWriter writer = new FileWriter(orderFile);
            writer.write(""); 
            writer.close();
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
