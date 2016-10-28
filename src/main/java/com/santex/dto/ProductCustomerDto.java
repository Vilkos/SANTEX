package com.santex.dto;

import com.santex.entity.Brand;
import com.santex.entity.Subcategory;
import com.santex.entity.Unit;

public class ProductCustomerDto {
    private int id;
    private String SKU;
    private double price;
    private double discountPrice;
    private double finalPrice;
    private String stringPrice;
    private String stringDiscountPrice;
    private String stringFinalPrice;
    private boolean priceVisibility;
    private String productName;
    private boolean imageAvailability;
    private Subcategory subcategory;
    private Brand brand;
    private Unit unit;

    public ProductCustomerDto(int id, String SKU, double price, double discountPrice, double finalPrice, String stringPrice, String stringDiscountPrice, String stringFinalPrice, boolean priceVisibility, String productName, boolean imageAvailability, Subcategory subcategory, Brand brand, Unit unit) {
        this.id = id;
        this.SKU = SKU;
        this.price = price;
        this.discountPrice = discountPrice;
        this.finalPrice = finalPrice;
        this.stringPrice = stringPrice;
        this.stringDiscountPrice = stringDiscountPrice;
        this.stringFinalPrice = stringFinalPrice;
        this.priceVisibility = priceVisibility;
        this.productName = productName;
        this.imageAvailability = imageAvailability;
        this.subcategory = subcategory;
        this.brand = brand;
        this.unit = unit;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSKU() {
        return SKU;
    }

    public void setSKU(String SKU) {
        this.SKU = SKU;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(double discountPrice) {
        this.discountPrice = discountPrice;
    }

    public double getFinalPrice() {
        return finalPrice;
    }

    public void setFinalPrice(double finalPrice) {
        this.finalPrice = finalPrice;
    }

    public String getStringPrice() {
        return stringPrice;
    }

    public void setStringPrice(String stringPrice) {
        this.stringPrice = stringPrice;
    }

    public String getStringDiscountPrice() {
        return stringDiscountPrice;
    }

    public void setStringDiscountPrice(String stringDiscountPrice) {
        this.stringDiscountPrice = stringDiscountPrice;
    }

    public String getStringFinalPrice() {
        return stringFinalPrice;
    }

    public void setStringFinalPrice(String stringFinalPrice) {
        this.stringFinalPrice = stringFinalPrice;
    }

    public boolean isPriceVisibility() {
        return priceVisibility;
    }

    public void setPriceVisibility(boolean priceVisibility) {
        this.priceVisibility = priceVisibility;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public boolean isImageAvailability() {
        return imageAvailability;
    }

    public Subcategory getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(Subcategory subcategory) {
        this.subcategory = subcategory;
    }

    public Brand getBrand() {
        return brand;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }
}
