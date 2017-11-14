package com.example.shopcart.shopcart;

/**
 *
 * 购物车数据实体类
 */
public class ShopcartEntity {
    /**
     * product_id : 53   商品id
     * quantity : 4      购物车选择数量
     * product_name : 哈哈哈222
     * product_price : 0.1
     * product_quantity : 51   库存
     * product_first_cover_url :
     * product_status : selling  销售状态
     */
    private int id;
    private int product_id;
    private int quantity;
    private String product_name;
    private String product_price;
    private int product_quantity;
    private int picRes;
    private String product_status;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public void setProduct_price(String product_price) {
        this.product_price = product_price;
    }

    public void setProduct_quantity(int product_quantity) {
        this.product_quantity = product_quantity;
    }

    public int getPicRes() {
        return picRes;
    }

    public void setPicRes(int picRes) {
        this.picRes = picRes;
    }

    public void setProduct_status(String product_status) {
        this.product_status = product_status;
    }

    public int getProduct_id() {
        return product_id;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getProduct_name() {
        return product_name;
    }

    public String getProduct_price() {
        return product_price;
    }

    public int getProduct_quantity() {
        return product_quantity;
    }

    public String getProduct_status() {
        return product_status;
    }

}
