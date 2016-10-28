package com.santex.dto;

import com.santex.entity.Customer;
import com.santex.entity.Status;

import java.util.List;

public class OrderForm {
    private int id;
    private String street;
    private String city;
    private String postcode;
    private String message;
    private Status status;
    private String timeOfOrder;
    private Customer customer;
    private List<OrderEntryDto> entryList;
    private String SKU;
    private double total;

    public OrderForm (int id, String street, String city, String postcode, String message, Status status, String timeOfOrder, Customer customer, List<OrderEntryDto> entryList, double total) {
        this.id = id;
        this.street = street;
        this.city = city;
        this.postcode = postcode;
        this.message = message;
        this.status = status;
        this.timeOfOrder = timeOfOrder;
        this.customer = customer;
        this.entryList = entryList;
        this.total = total;
    }

    public OrderForm() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getTimeOfOrder() {
        return timeOfOrder;
    }

    public void setTimeOfOrder(String timeOfOrder) {
        this.timeOfOrder = timeOfOrder;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public List<OrderEntryDto> getEntryList() {
        return entryList;
    }

    public void setEntryList(List<OrderEntryDto> entryList) {
        this.entryList = entryList;
    }

    public String getSKU() {
        return SKU;
    }

    public void setSKU(String SKU) {
        this.SKU = SKU;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}

