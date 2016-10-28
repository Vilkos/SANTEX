package com.santex.entity;

import javax.persistence.*;

@Embeddable
public class Address {
    private String street;
    private String number;
    private String city;
    private String postcode;

    public Address() {
        street = "Street name";
        number = "Number";
        city = "City";
        postcode = "Postcode";
    }

    @Column(length = 50)
    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    @Column(length = 10)
    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    @Column(length = 20)
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Column(length = 10)
    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }
}
