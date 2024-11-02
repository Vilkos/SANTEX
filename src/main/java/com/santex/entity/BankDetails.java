package com.santex.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class BankDetails {
    private String bankName;
    private String IBAN;
    private String SWIFT;

    public BankDetails() {
        bankName = "Name of Bank Institution";
        IBAN = "IBAN1234567890";
        SWIFT = "SWIFT12345";
    }

    @Column(length = 50)
    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    @Column(length = 20)
    public String getIBAN() {
        return IBAN;
    }

    public void setIBAN(String IBAN) {
        this.IBAN = IBAN;
    }

    @Column(length = 10)
    public String getSWIFT() {
        return SWIFT;
    }

    public void setSWIFT(String SWIFT) {
        this.SWIFT = SWIFT;
    }
}
