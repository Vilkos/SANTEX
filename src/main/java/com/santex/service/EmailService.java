package com.santex.service;

public interface EmailService {

    void sendInvoice(int orderId);

    void sendRegistrationInfo(int customerId);
}
