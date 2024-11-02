package com.santex.entity;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.*;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Entity(name = "orders")
public class Order {
    private int id;
    private String street;
    private String city;
    private String postcode;
    private String message;
    private Status status;
    private LocalDateTime timeOfOrder;
    private Customer customer;
    private List<OrderEntry> entryList = new ArrayList<>();

    public Order() {
        timeOfOrder = LocalDateTime.now(ZoneId.of("Europe/Kiev"));
        status = Status.Нове;
        message = "";
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

    @Size(min = 3, max = 50, message = "Назва вулиці має містити від 3 до 50 символів.")
    @Column(nullable = false, length = 50)
    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    @Size(min = 3, max = 50, message = "Назва міста має містити від 3 до 50 символів.")
    @Column(nullable = false, length = 50)
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Size(min = 3, max = 10, message = "Поштовий індекс має містити від 3 до 10 символів.")
    @Column(nullable = false, length = 10)
    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    @Size(max = 255, message = "Повідомлення не має перевищувати 255 символів.")
    @Column(nullable = false)
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Basic
    @Convert(converter = StatusConverter.class)
    @Column
    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Column
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    public LocalDateTime getTimeOfOrder() {
        return timeOfOrder;
    }

    public void setTimeOfOrder(LocalDateTime timeOfOrder) {
        this.timeOfOrder = timeOfOrder;
    }

    @Transient
    public String showTimeOfOrder() {
        return timeOfOrder.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.LONG, FormatStyle.SHORT).withLocale(Locale.forLanguageTag("uk-UA")).withZone(ZoneId.of("Europe/Kiev")));
    }

    @Transient
    public boolean isCurrentDate() {
        return timeOfOrder.toLocalDate().isEqual(LocalDate.now());
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer user) {
        this.customer = user;
    }

    @OneToMany(mappedBy = "orders", fetch = FetchType.LAZY)
    public List<OrderEntry> getEntryList() {
        return entryList;
    }

    public void setEntryList(List<OrderEntry> entryList) {
        this.entryList = entryList;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", street='" + street + '\'' +
                ", city='" + city + '\'' +
                ", postcode='" + postcode + '\'' +
                ", message='" + message + '\'' +
                ", timeOfOrder=" + timeOfOrder +
                '}';
    }
}

