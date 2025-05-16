package com.example.model;

public class Product {
    private String proId;
    private String proModel;
    private String proCategory;
    private String proName;
    private double  proCurrentPrice;
    private double  proRawPrice;
    private double  proDiscount;
    private int proLikesCount;

    public Product(String proId,String proModel,String proCategory,String proName,double proCurrentPrice,double proRawPrice,double proDiscount,int proLikesCount){
        this.proId = proId;
        this.proModel = proModel;
        this.proCategory = proCategory;
        this.proName = proName;
        this.proCurrentPrice = proCurrentPrice;
        this.proRawPrice = proRawPrice;
        this.proDiscount = proDiscount;
        this.proLikesCount = proLikesCount;
    }

    public Product() {
        this.proId = "p_00000";
        this.proModel = "default_model";
        this.proCategory = "default_category";
        this.proName = "default_name";
        this.proCurrentPrice = 0.0;
        this.proRawPrice = 0.0;
        this.proDiscount = 0.0;
        this.proLikesCount = 0;
    }

    public String getProId() {
        return proId;
    }

    public String getProModel() {
        return proModel;
    }

    public String getProCategory() {
        return proCategory;
    }

    public String getProName() {
        return proName;
    }

    public double getProCurrentPrice() {
        return proCurrentPrice;
    }

    public double getProRawPrice() {
        return proRawPrice;
    }

    public double getProDiscount() {
        return proDiscount;
    }

    public int getProLikesCount() {
        return proLikesCount;
    }

    @Override
    public String toString() {
        return "{"
            + "\"pro_id\":\"" + proId + "\", "
            + "\"pro_model\":\"" + proModel + "\", "
            + "\"pro_category\":\"" + proCategory + "\", "
            + "\"pro_name\":\"" + proName + "\", "
            + "\"pro_current_price\":\"" + String.format("%.2f", proCurrentPrice) + "\", "
            + "\"pro_raw_price\":\"" + String.format("%.2f", proRawPrice) + "\", "
            + "\"pro_discount\":\"" + String.format("%.2f", proDiscount) + "\", "
            + "\"pro_likes_count\":\"" + proLikesCount + "\""
            + "}";
    }

}

