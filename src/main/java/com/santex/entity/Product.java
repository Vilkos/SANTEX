package com.santex.entity;

import org.hibernate.annotations.*;
import org.hibernate.annotations.Cache;

import jakarta.persistence.*;
import jakarta.persistence.Entity;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "product")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "product")
public class Product implements Serializable {
    private static final long serialVersionUID = -2733995223723292834L;
    private int id;
    private String SKU;
    private boolean availability;
    private boolean priceVisibility;
    private int price;
    private int discountPrice;
    private Currency currency;
    private Integer quantityOfSales;
    private String productName;
    private Subcategory subcategory;
    private Brand brand;
    private Unit unit;
    private List<OrderEntry> entryList = new ArrayList<>();

    public Product(String SKU, boolean availability, boolean priceVisibility, int price, int discountPrice, Currency currency, String productName) {
        this.SKU = SKU;
        this.availability = availability;
        this.priceVisibility = priceVisibility;
        this.price = price;
        this.discountPrice = discountPrice;
        this.currency = currency;
        this.productName = productName;
    }

    public Product(String SKU, boolean availability, boolean priceVisibility, int price, int discountPrice, Currency currency, String productName, Subcategory subcategory, Brand brand, Unit unit) {
        this.SKU = SKU;
        this.availability = availability;
        this.priceVisibility = priceVisibility;
        this.price = price;
        this.discountPrice = discountPrice;
        this.currency = currency;
        this.productName = productName;
        this.subcategory = subcategory;
        this.brand = brand;
        this.unit = unit;
    }

    public Product() {
        availability = true;
        priceVisibility = true;
        price = 0;
        discountPrice = 0;
        currency = Currency.UAH;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(nullable = false, unique = true, length = 11)
    public String getSKU() {
        return SKU;
    }

    public void setSKU(String SKU) {
        this.SKU = SKU;
    }

    @Column(nullable = false)
    public boolean isAvailability() {
        return availability;
    }

    public void setAvailability(boolean availability) {
        this.availability = availability;
    }

    @Column(nullable = false)
    public boolean isPriceVisibility() {
        return priceVisibility;
    }

    public void setPriceVisibility(boolean priceVisibility) {
        this.priceVisibility = priceVisibility;
    }

    @Column(nullable = false)
    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    @Column(nullable = false)
    public int getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(int discountPrice) {
        this.discountPrice = discountPrice;
    }

    @Column(length = 3, nullable = false)
    @Enumerated(EnumType.STRING)
    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    @Column(nullable = false)
    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    @Formula("(select sum(oe.quantity) from order_entry oe where oe.PRODUCT_ID = id)")
    public Integer getQuantityOfSales() {
        return quantityOfSales != null ? quantityOfSales : 0;
    }

    public void setQuantityOfSales(Integer quantityOfSales) {
        this.quantityOfSales = quantityOfSales;
    }

    @ManyToOne
    @JoinColumn
    public Subcategory getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(Subcategory subcategory) {
        this.subcategory = subcategory;
    }

    @ManyToOne
    @JoinColumn
    public Brand getBrand() {
        return brand;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
    }

    @ManyToOne
    @JoinColumn
    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    public List<OrderEntry> getEntryList() {
        return entryList;
    }

    public void setEntryList(List<OrderEntry> entryList) {
        this.entryList = entryList;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", SKU='" + SKU + '\'' +
                ", availability=" + availability +
                ", priceVisibility=" + priceVisibility +
                ", price=" + price +
                ", currency=" + currency +
                ", productName='" + productName + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Product product = (Product) o;

        return id == product.id;
    }

    @Override
    public int hashCode() {
        return id;
    }
}
