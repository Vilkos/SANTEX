package com.santex.service.implementation;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.zugferd.exceptions.DataIncompleteException;
import com.itextpdf.text.zugferd.exceptions.InvalidCodeException;
import com.itextpdf.xmp.XMPException;
import com.santex.dao.CustomerDao;
import com.santex.dto.OrderForm;
import com.santex.entity.Customer;
import com.santex.service.CompanyCredentialsService;
import com.santex.service.EmailService;
import com.santex.service.OrderService;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.tools.generic.NumberTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;
import org.xml.sax.SAXException;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.io.StringWriter;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import static com.santex.service.PriceService.total;

public class EmailServiceImpl implements EmailService {
    private JavaMailSender mailSender;
    private VelocityEngine velocityEngine;
    private final PdfInvoiceBuilder invoiceBuilder;
    private final CompanyCredentialsService cc;
    private final OrderService orderService;
    private final CustomerDao customerDao;

    @Autowired
    public EmailServiceImpl(PdfInvoiceBuilder invoiceBuilder, CompanyCredentialsService cc, OrderService orderService, CustomerDao customerDao) {
        this.invoiceBuilder = invoiceBuilder;
        this.cc = cc;
        this.orderService = orderService;
        this.customerDao = customerDao;
    }

    @Async
    @Override
    public void sendInvoice(int orderId) {
        OrderForm orderForm = orderService.getById(orderId);

        MimeMessage message = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(orderForm.getCustomer().getEmail());
            helper.setSubject(String.format("№ I/%05d", orderForm.getId()));
            helper.setFrom(new InternetAddress(cc.getCredentials().getEmail(), cc.getCredentials().getName(), "UTF-8"));

            Template template = velocityEngine.getTemplate("email/purchase.vm", "UTF-8");
            VelocityContext velocityContext = new VelocityContext();
            velocityContext.put("id", String.format("I/%05d", orderForm.getId()));
            velocityContext.put("entryList", orderForm.getEntryList());
            velocityContext.put("cc", cc.getCredentials());
            velocityContext.put("total", total(orderForm.getEntryList()));
            velocityContext.put("locale", Locale.forLanguageTag("uk-UA"));
            velocityContext.put("number", new NumberTool());

            StringWriter stringWriter = new StringWriter();
            template.merge(velocityContext, stringWriter);
            helper.setText(stringWriter.toString(), true);

            String ldt = LocalDateTime.now(ZoneId.of("Europe/Kiev")).format(DateTimeFormatter.ofPattern("yyyyMMddkkmmss"));

            helper.addAttachment(String.format("%s-%05d", ldt, orderForm.getId()), invoiceBuilder.buildAttachment(orderForm.getEntryList(), orderService.findById(orderForm.getId()), cc.getCredentials()));

        } catch (MessagingException | ParseException | TransformerException | InvalidCodeException | SAXException | DocumentException | ParserConfigurationException | DataIncompleteException | IOException | XMPException e) {
            e.printStackTrace();
        }
        thisMailSender(message);
    }

    @Async
    @Override
    @Transactional(readOnly = true)
    public void sendRegistrationInfo(int customerId) {
        Customer customer = customerDao.findOne(customerId);
        MimeMessage message = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(customer.getEmail());
            helper.setSubject(cc.getCredentials().getName());
            helper.setFrom(new InternetAddress(cc.getCredentials().getEmail(), cc.getCredentials().getName(), "UTF-8"));

            Template template = velocityEngine.getTemplate("email/registration.vm", "UTF-8");
            VelocityContext velocityContext = new VelocityContext();
            velocityContext.put("cc", cc.getCredentials());
            velocityContext.put("customer", customer);

            StringWriter stringWriter = new StringWriter();
            template.merge(velocityContext, stringWriter);
            helper.setText(stringWriter.toString(), true);


        } catch (MessagingException | IOException e) {
            e.printStackTrace();
        }
        thisMailSender(message);
    }

    private void thisMailSender(MimeMessage message) {
        synchronized (this) {
            mailSender.send(message);
        }
    }

    public void setVelocityEngine(VelocityEngine velocityEngine) {
        this.velocityEngine = velocityEngine;
    }

    public void setMailSender(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }
}
