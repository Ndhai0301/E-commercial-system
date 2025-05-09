package model;

public class Order {
    private String orderId;
    private String userId;
    private String proId;
    private String orderTime;

    public Order(String orderId, String userId, String proId, String orderTime) {
        this.orderId = orderId;
        this.userId = userId;
        this.proId = proId;
        this.orderTime = orderTime;
    }
    public Order(){
        this.orderId = "o_00000";
        this.userId = "u_0000000000";
        this.proId = "p_00000";
        this.orderTime = "01-01-2000_00:00:00";
    }
    public String getOrderId() {
        return orderId;
    }

    public String getUserId() {
        return userId;
    }

    public String getProId() {
        return proId;
    }

    public String getOrderTime() {
        return orderTime;
    }

    @Override
    public String toString() {
        return "{"
            + "\"order_id\":\"" + orderId + "\", "
            + "\"user_id\":\"" + userId + "\", "
            + "\"pro_id\":\"" + proId + "\", "
            + "\"order_time\":\"" + orderTime + "\""
            + "}";
    }
}

