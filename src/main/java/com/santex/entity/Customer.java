package com.santex.entity;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;
import java.util.Locale;

@Entity(name = "customer")
public class Customer {
    private int id;
    private String email;
    private String password;
    private String firstName;
    private String secondName;
    private String phone;
    private LocalDateTime dateOfRegistration;
    private List<Order> ordersList;

    public Customer() {
    }

    public Customer(String email, String password, String firstName, String secondName, String phone) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.secondName = secondName;
        this.phone = phone;
        this.dateOfRegistration = LocalDateTime.now(ZoneId.of("Europe/Kiev"));
    }

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column (nullable = false, unique = true)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Column (nullable = false)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Column
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Column
    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    @Column (length = 15)
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Transient
    public String showRegistrationDate() {
        return dateOfRegistration.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.LONG, FormatStyle.SHORT).withLocale(Locale.forLanguageTag("uk-UA")).withZone(ZoneId.of("Europe/Kiev")));
    }

    @Column
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    public LocalDateTime getDateOfRegistration() {
        return dateOfRegistration;
    }

    public void setDateOfRegistration(LocalDateTime dateOfRegistration) {
        this.dateOfRegistration = dateOfRegistration;
    }

    @OneToMany (fetch = FetchType.LAZY, mappedBy = "customer")
    public List<Order> getOrdersList() {
        return ordersList;
    }

    public void setOrdersList(List<Order> ordersList) {
        this.ordersList = ordersList;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", firstName='" + firstName + '\'' +
                ", secondName='" + secondName + '\'' +
                ", phone='" + phone + '\'' +
                ", dateOfRegistration=" + dateOfRegistration +
                '}';
    }
}
