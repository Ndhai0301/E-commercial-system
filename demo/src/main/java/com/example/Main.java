package com.example;
import java.util.List;

import org.json.JSONObject;

import com.example.model.Order;
import com.example.operation.AdminOperation;
import com.example.operation.OrderListResult;
import com.example.operation.OrderOperation;
import com.example.operation.ProductListResult;
import com.example.operation.ProductOperation;

public class Main {
    public static void main(String[] args) {
        /** test UserOperation
        UserOperation op = UserOperation.getInstance();
        String newUserId = op.generateUniqueUserId();
        System.out.println("Generated User ID: " + newUserId);

        String password = "pass123";
        String encrypted = op.encryptPassword(password);
        String decrypted = op.decryptPassword(encrypted);
        System.out.println("Original Password: " + password);
        System.out.println("Encrypted Password: " + encrypted);
        System.out.println("Decrypted Password: " + decrypted);
        System.out.println("Password matches: " + password.equals(decrypted));

        String testUsername = "john_doe";
        boolean exists = op.checkUsernameExist(testUsername);
        System.out.println("Username '" + testUsername + "' exists: " + exists);

        String validUsername = "user_test";
        
        String validPassword = "abc123";
        

        System.out.println("Valid username '" + validUsername + "': " + op.validateUsername(validUsername));
        
        System.out.println("Valid password '" + validPassword + "': " + op.validatePassword(validPassword));
       
        String loginUsername = "admin"; // chỉnh theo dữ liệu thật trong users.txt
        String loginPassword = "XtIa"; // chỉnh đúng với password gốc trước khi mã hoá

        User user = op.login(loginUsername, loginPassword);
        if (user != null) {
            System.out.println("Login success. User: " + user.getUserName() + ", Role: " + user.getUserRole());
        } else {
            System.out.println("Login failed for username: " + loginUsername);
        }
        /* */
        // Test CustomerOperation
        /*CustomerOperation customerOp = CustomerOperation.getInstance();
        UserOperation userOp = UserOperation.getInstance();
        String email = "duchai301@example.com";
        System.out.println("Valid email:" + customerOp.validateEmail(email));
        String mobile = "0904784715";
        System.out.println("Valid mobile: " + customerOp.validateMobile(mobile));
        String userName = "duc_hai";
        String userPassword = "duc123";

        customerOp.registerCustomer(userName, userPassword, email, mobile);
        System.out.println("Username exists: " + userOp.checkUsernameExist(userName));

        int pageToFetch = 1;
        CustomerListResult result = customerOp.getCustomerList(pageToFetch);
        System.out.println(" Trang hiện tại: " + result.getCurrentPage());
        System.out.println(" Tổng số trang: " + result.getTotalPages());

        List<Customer> customers = result.getCustomers();
        if (customers.isEmpty()) {
            System.out.println(" Không có khách hàng nào trong trang này.");
        } else {
            System.out.println(" Danh sách khách hàng:");
            for (Customer customer : customers) {
                System.out.println(" ID: " + customer.getUserId());
                System.out.println("    Tên: " + customer.getUserName());
                System.out.println("    Email: " + customer.getUserEmail());
                System.out.println("    SĐT: " + customer.getUserMobile());
                System.out.println("--------------");
            }
        }
        customerOp.deleteAllCustomers();/* */
        /**ProductOperation po = ProductOperation.getInstance();

        // 2.9.2. extractProductsFromFiles()
        System.out.println("=== extractProductsFromFiles() ===");
        po.extractProductsFromFiles();
        System.out.println("Products extracted.\n");

        // 2.9.3. getProductList()
        System.out.println("=== getProductList(page 1) ===");
        ProductListResult page1 = po.getProductList(1);
        for (JSONObject product : page1.getProducts()) {
            System.out.println(product.toString(2)); // pretty print
        }
        System.out.printf("Current page: %d / Total pages: %d\n\n", page1.getCurrentPage(), page1.getTotalPages());

        // 2.9.4. deleteProduct()
        System.out.println("=== deleteProduct(\"p020\") ===");
        boolean deleted = po.deleteProduct("p020");
        System.out.println("Deleted p020? " + deleted + "\n");

        // 2.9.5. getProductListByKeyword()
        System.out.println("=== getProductListByKeyword(\"sony\") ===");
        List<JSONObject> sonyProducts = po.getProductListByKeyword("sony");
        for (JSONObject product : sonyProducts) {
            System.out.println(product.toString(2));
        }
        System.out.println("Found " + sonyProducts.size() + " product(s).\n");

        // 2.9.6. getProductById()
        System.out.println("=== getProductById(\"p001\") ===");
        JSONObject p1 = po.getProductById("p001");
        System.out.println(p1 != null ? p1.toString(2) : "Not found");
        System.out.println();

        // 2.9.11. deleteAllProducts()
        System.out.println("=== deleteAllProducts() ===");
        po.deleteAllProducts();
        System.out.println("All products deleted.");

        // Kiểm tra lại sau khi xóa
        System.out.println("\n=== getProductList(1) after deleteAllProducts ===");
        ProductListResult afterDelete = po.getProductList(1);
        System.out.println("Product count after delete: " + afterDelete.getProducts().size());
    }/* */
    OrderOperation op = OrderOperation.getInstance();

        System.out.println("==== TaO NGuoI DuNG THu ====");
        op.generateTestCustomers();

        // Tạo đơn hàng
        System.out.println("\n==== TaO ĐoN HaNG ====");
        for (int i = 0; i < 5; i++) {
            String userId = String.format("u_%010d", i + 1);
            String productId = String.format("p_%05d", (i % 3) + 1); // giả lập product ID p_00001, p_00002, ...
            boolean success = op.createAnOrder(userId, productId, null);
            System.out.println("Tạo đơn hàng cho user " + userId + " với sản phẩm " + productId + ": " + (success ? "✅" : "❌"));
        }

        // In danh sách đơn hàng theo trang
        System.out.println("\n==== HIỂN THỊ ĐƠN HÀNG (Phân trang) ====");
        String testUserId = "u_0000000001";
        OrderListResult result = op.getOrderList(testUserId, 1);
        System.out.println("Trang: " + result.getCurrentPage() + "/" + result.getTotalPages());
        for (Order order : result.getOrders()) {
            System.out.println(order);
        }

        // Biểu đồ tiêu dùng 1 khách hàng
        System.out.println("\n==== VẼ BIỂU ĐỒ TIÊU DÙNG THEO THÁNG ====");
        op.generateSingleCustomerConsumptionFigure(testUserId);

        // Biểu đồ 10 sản phẩm bán chạy nhất
        System.out.println("\n==== VẼ BIỂU ĐỒ TOP 10 SẢN PHẨM BÁN CHẠY ====");
        op.generateAllTop10BestSellersFigure();

        // Xoá 1 đơn hàng
        System.out.println("\n==== XÓA 1 ĐƠN HÀNG ====");
        if (!result.getOrders().isEmpty()) {
            String orderIdToDelete = result.getOrders().get(0).getUserId(); // dùng userId do lỗi trong hàm deleteOrder
            boolean deleted = op.deleteOrder(orderIdToDelete);
            System.out.println("Xóa đơn hàng của user " + orderIdToDelete + ": " + (deleted ? "✅" : "❌"));
        }

        // Xoá toàn bộ đơn hàng
        System.out.println("\n==== XÓA TẤT CẢ ĐƠN HÀNG ====");
        op.deleteAllOrders();
        System.out.println("Tất cả đơn hàng đã được xóa.");
    }
}




