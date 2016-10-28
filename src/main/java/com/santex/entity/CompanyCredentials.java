package com.santex.entity;

import org.hibernate.annotations.*;
import org.hibernate.annotations.Cache;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;

@Entity(name = "company_credentials")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "company_credentials")
public class CompanyCredentials implements Serializable {
    private static final long serialVersionUID = -4885142687626717010L;
    private int id;
    private String name;
    private String hostName;
    private Address address;
    private String phone1;
    private String phone2;
    private String phone3;
    private String email;
    private String taxcode;
    private BankDetails bankDetails;
    private String shipmentInfo;
    private String description;

    public CompanyCredentials() {
        id = 1;
        name = "Company name";
        phone1 = "0991234567";
        phone2 = "0991234567";
        phone3 = "0991234567";
        email = "email@email.com";
        taxcode = "XYZ1234567890";
        shipmentInfo = "shipmentInfo";
        description = "shipmentInfo";
    }

    @Transient
    public String getHostName() {
        InetAddress ip;
        try {
            ip = InetAddress.getLocalHost();
            hostName = ip.getHostName();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    @Id
    @Column
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(length = 50)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Embedded
    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    @Column (length = 20)
    public String getPhone1() {
        return phone1;
    }

    public void setPhone1(String phone1) {
        this.phone1 = phone1;
    }

    @Column (length = 20)
    public String getPhone2() {
        return phone2;
    }

    public void setPhone2(String phone2) {
        this.phone2 = phone2;
    }

    @Column (length = 20)
    public String getPhone3() {
        return phone3;
    }

    public void setPhone3(String phone3) {
        this.phone3 = phone3;
    }

    @Column
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Column(length = 20)
    public String getTaxcode() {
        return taxcode;
    }

    public void setTaxcode(String taxcode) {
        this.taxcode = taxcode;
    }

    @Embedded
    public BankDetails getBankDetails() {
        return bankDetails;
    }

    public void setBankDetails(BankDetails bankDetails) {
        this.bankDetails = bankDetails;
    }

    @Column(columnDefinition = "TEXT", length = 5000)
    public String getShipmentInfo() {
        return shipmentInfo;
    }

    public void setShipmentInfo(String shipmentInfo) {
        this.shipmentInfo = shipmentInfo;
    }

    @Column(columnDefinition = "TEXT", length = 5000)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CompanyCredentials that = (CompanyCredentials) o;

        return id == that.id;

    }

    @Override
    public int hashCode() {
        return id;
    }
}
