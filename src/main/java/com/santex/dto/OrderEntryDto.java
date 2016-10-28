package com.santex.dto;

public class OrderEntryDto {
    private int id;
    private int orderId;
    private boolean imageAvailability;
    private String SKU;
    private String productName;
    private double priceUAH;
    private String stringPriceUAH;
    private int quantity;
    private String unit;
    private double subtotal;
    private String stringSubtotal;

    public OrderEntryDto() {
    }

    public OrderEntryDto(int id, int orderId, String SKU, String productName, double priceUAH, String stringPriceUAH, int quantity, String unit, double subtotal, String stringSubtotal) {
        this.id = id;
        this.orderId = orderId;
        this.SKU = SKU;
        this.productName = productName;
        this.priceUAH = priceUAH;
        this.stringPriceUAH = stringPriceUAH;
        this.quantity = quantity;
        this.unit = unit;
        this.subtotal = subtotal;
        this.stringSubtotal = stringSubtotal;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public boolean isImageAvailability() {
        return imageAvailability;
    }

    public void setImageAvailability(boolean imageAvailability) {
        this.imageAvailability = imageAvailability;
    }

    public String getSKU() {
        return SKU;
    }

    public void setSKU(String SKU) {
        this.SKU = SKU;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getPriceUAH() {
        return priceUAH;
    }

    public void setPriceUAH(double priceUAH) {
        this.priceUAH = priceUAH;
    }

    public String getStringPriceUAH() {
        return stringPriceUAH;
    }

    public void setStringPriceUAH(String stringPriceUAH) {
        this.stringPriceUAH = stringPriceUAH;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public String getStringSubtotal() {
        return stringSubtotal;
    }

    public void setStringSubtotal(String stringSubtotal) {
        this.stringSubtotal = stringSubtotal;
    }
}
