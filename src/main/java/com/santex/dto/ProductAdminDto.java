package com.santex.dto;

import com.santex.entity.Brand;
import com.santex.entity.Currency;
import com.santex.entity.Subcategory;
import com.santex.entity.Unit;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.web.multipart.MultipartFile;

public class ProductAdminDto {
    private int id;
    @NotBlank(message = "Поле не може бути пустим!")
    @Pattern(regexp = "[0-9]+", message = "Артикул має складатись лише з цифр до 11 символів.")
    private String SKU;
    private boolean availability = true;
    private boolean priceVisibility = true;
    private double price;
    private double priceUAH;
    private double discountPrice;
    private Currency currency;
    @Size(min = 5, max = 255, message = "Назва товару має містити від 5 до 255 символів")
    private String productName;
    private int amountOfSales;
    private MultipartFile image;
    private boolean imageAvailability;
    @Valid
    private Subcategory subcategory;
    private Brand brand;
    private Unit unit;

    public ProductAdminDto(int id, String SKU, boolean availability, boolean priceVisibility, double price, double priceUAH, double discountPrice, Currency currency, String productName, int amountOfSales, boolean imageAvailability, Subcategory subcategory, Brand brand, Unit unit) {
        this.id = id;
        this.SKU = SKU;
        this.availability = availability;
        this.priceVisibility = priceVisibility;
        this.price = price;
        this.priceUAH = priceUAH;
        this.discountPrice = discountPrice;
        this.currency = currency;
        this.productName = productName;
        this.amountOfSales = amountOfSales;
        this.imageAvailability = imageAvailability;
        this.subcategory = subcategory;
        this.brand = brand;
        this.unit = unit;
    }

    public ProductAdminDto() {
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

    public boolean isAvailability() {
        return availability;
    }

    public void setAvailability(boolean availability) {
        this.availability = availability;
    }

    public boolean isPriceVisibility() {
        return priceVisibility;
    }

    public void setPriceVisibility(boolean priceVisibility) {
        this.priceVisibility = priceVisibility;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getPriceUAH() {
        return priceUAH;
    }

    public void setPriceUAH(double priceUAH) {
        this.priceUAH = priceUAH;
    }

    public double getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(double discountPrice) {
        this.discountPrice = discountPrice;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getAmountOfSales() {
        return amountOfSales;
    }

    public void setAmountOfSales(int amountOfSales) {
        this.amountOfSales = amountOfSales;
    }

    public MultipartFile getImage() {
        return image;
    }

    public void setImage(MultipartFile image) {
        this.image = image;
    }

    public boolean isImageAvailability() {
        return imageAvailability;
    }

    public void setImageAvailability(boolean imageAvailability) {
        this.imageAvailability = imageAvailability;
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
