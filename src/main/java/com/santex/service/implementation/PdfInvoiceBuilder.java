package com.santex.service.implementation;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.xml.xmp.PdfAXmpWriter;
import com.itextpdf.text.zugferd.InvoiceDOM;
import com.itextpdf.text.zugferd.checkers.basic.DateFormatCode;
import com.itextpdf.text.zugferd.checkers.basic.DocumentTypeCode;
import com.itextpdf.text.zugferd.checkers.basic.TaxIDTypeCode;
import com.itextpdf.text.zugferd.checkers.basic.TaxTypeCode;
import com.itextpdf.text.zugferd.exceptions.DataIncompleteException;
import com.itextpdf.text.zugferd.exceptions.InvalidCodeException;
import com.itextpdf.text.zugferd.profiles.BasicProfile;
import com.itextpdf.text.zugferd.profiles.BasicProfileImp;
import com.itextpdf.xmp.XMPException;
import com.santex.dto.OrderEntryDto;
import com.santex.entity.CompanyCredentials;
import com.santex.entity.Order;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

import jakarta.mail.util.ByteArrayDataSource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

@Service("pdfView")
public class PdfInvoiceBuilder extends AbstractITextPdfView {

    private final String FONT = "fonts/OfficinaSansBookC.otf";
    private final String FONTB = "fonts/OfficinaSansBoldC.otf";

    private BaseFont bf = BaseFont.createFont(FONT, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
    private BaseFont bfb = BaseFont.createFont(FONTB, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);

    private Font font10 = new Font(bf, 10);
    private Font font10b = new Font(bfb, 10);
    private Font font12 = new Font(bf, 12);
    private Font font12b = new Font(bfb, 12);
    private Font font14 = new Font(bf, 14);

    public PdfInvoiceBuilder() throws IOException, DocumentException {
    }

    public ByteArrayDataSource buildAttachment(List<OrderEntryDto> entryList, Order order, CompanyCredentials cc) throws XMPException, IOException, DocumentException, ParseException, ParserConfigurationException, InvalidCodeException, SAXException, DataIncompleteException, TransformerException {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        Document document = new Document();

        PdfAWriter writer = PdfAWriter.getInstance(document, baos, PdfAConformanceLevel.ZUGFeRDBasic);
        writer.setPdfVersion(PdfWriter.VERSION_1_7);
        writer.createXmpMetadata();
        writer.getXmpWriter().setProperty(PdfAXmpWriter.zugferdSchemaNS, PdfAXmpWriter.zugferdDocumentFileName, "ZUGFeRD-invoice.xml");

        document.open();

        Resource path = getApplicationContext().getResource("classpath:colorProfile/sRGB_CS_profile.icm");
        ICC_Profile icc = ICC_Profile.getInstance(path.getInputStream());
        writer.setOutputIntents("Custom", "", "http://www.color.org", "sRGB IEC61966-2.1", icc);

        BasicProfile basic = createBasicProfileData(entryList, order, cc);

        // header
        Paragraph p;
        p = new Paragraph(basic.getName() + " " + basic.getId(), font14);
        p.setAlignment(Element.ALIGN_RIGHT);
        document.add(p);
        p = new Paragraph(convertDate(basic.getDateTime(), "dd MMMM yyyy"), font12);
        p.setAlignment(Element.ALIGN_RIGHT);
        document.add(p);

        // Address seller / buyer
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        PdfPCell seller = getPartyAddress("Постачальник:",
                basic.getSellerName(),
                basic.getSellerLineOne(),
                basic.getSellerPostcode(),
                basic.getSellerCityName(),
                cc.getEmail(),
                cc.getPhone1() + ", ",
                cc.getPhone2() + ", ",
                cc.getPhone3());
        table.addCell(seller);
        PdfPCell buyer = getPartyAddress("Одержувач:",
                basic.getBuyerName(),
                basic.getBuyerLineOne(),
                basic.getBuyerPostcode(),
                basic.getBuyerCityName(),
                order.getCustomer().getEmail(),
                order.getCustomer().getPhone(),
                "",
                "");
        table.addCell(buyer);
        document.add(table);

        // line items
        table = new PdfPTable(6);
        table.setWidthPercentage(100);
        table.setSpacingBefore(20);
        table.setSpacingAfter(10);
        table.setWidths(new int[]{1, 5, 1, 1, 1, 1});
        table.addCell(getCell("Артикул:", Element.ALIGN_LEFT, font10b)).setBackgroundColor(BaseColor.LIGHT_GRAY);
        table.addCell(getCell("Найменування:", Element.ALIGN_LEFT, font10b)).setBackgroundColor(BaseColor.LIGHT_GRAY);
        table.addCell(getCell("Ціна:", Element.ALIGN_LEFT, font10b)).setBackgroundColor(BaseColor.LIGHT_GRAY);
        table.addCell(getCell("Кількість:", Element.ALIGN_LEFT, font10b)).setBackgroundColor(BaseColor.LIGHT_GRAY);
        table.addCell(getCell("Одиниця:", Element.ALIGN_LEFT, font10b)).setBackgroundColor(BaseColor.LIGHT_GRAY);
        table.addCell(getCell("Сума:", Element.ALIGN_LEFT, font10b)).setBackgroundColor(BaseColor.LIGHT_GRAY);

        for (OrderEntryDto entry : entryList) {
            table.addCell(getCell(entry.getSKU(), Element.ALIGN_CENTER, font10));
            table.addCell(getCell(entry.getProductName(), Element.ALIGN_LEFT, font10));
            table.addCell(getCell(String.valueOf(entry.getPriceUAH()), Element.ALIGN_RIGHT, font10));
            table.addCell(getCell(String.valueOf(entry.getQuantity()), Element.ALIGN_CENTER, font10));
            table.addCell(getCell(String.valueOf(entry.getUnit()), Element.ALIGN_CENTER, font10));
            table.addCell(getCell(String.valueOf(entry.getSubtotal()), Element.ALIGN_RIGHT, font10b));
        }

        document.add(table);

        // grand totals
        document.add(getTotalsTable(basic.getGrandTotalAmount(), "грн."));

        // payment info
        document.add(getPaymentInfo(basic.getPaymentMeansPayeeFinancialInstitutionName(), basic.getPaymentMeansPayeeFinancialInstitutionBIC(), basic.getPaymentMeansPayeeAccountIBAN()));

        // XML version
        InvoiceDOM dom = new InvoiceDOM(basic);
        PdfDictionary parameters = new PdfDictionary();
        parameters.put(PdfName.MODDATE, new PdfDate());
        PdfFileSpecification fileSpec = writer.addFileAttachment(
                "ZUGFeRD invoice", dom.toXML(), null,
                "ZUGFeRD-invoice.xml", "application/xml",
                AFRelationshipValue.Alternative, parameters);
        PdfArray array = new PdfArray();
        array.add(fileSpec.getReference());
        writer.getExtraCatalog().put(PdfName.AF, array);

        document.close();

        return new ByteArrayDataSource(baos.toByteArray(), "application/pdf");
    }

    @Override
    protected void buildPdfDocument(Map<String, Object> model, Document document, PdfAWriter writer, HttpServletRequest request, HttpServletResponse response) throws Exception {

        @SuppressWarnings("unchecked")
        List<OrderEntryDto> entryList = (List<OrderEntryDto>) model.get("entryList");
        Order order = (Order) model.get("order");
        CompanyCredentials cc = (CompanyCredentials) model.get("companyCredentials");

        BasicProfile basic = createBasicProfileData(entryList, order, cc);

        // header
        Paragraph p;
        p = new Paragraph(basic.getName() + " " + basic.getId(), font14);
        p.setAlignment(Element.ALIGN_RIGHT);
        document.add(p);
        p = new Paragraph(convertDate(basic.getDateTime(), "dd MMMM yyyy"), font12);
        p.setAlignment(Element.ALIGN_RIGHT);
        document.add(p);

        // Address seller / buyer
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        PdfPCell seller = getPartyAddress("Постачальник:",
                basic.getSellerName(),
                basic.getSellerLineOne(),
                basic.getSellerPostcode(),
                basic.getSellerCityName(),
                cc.getEmail(),
                cc.getPhone1() + ", ",
                cc.getPhone2() + ", ",
                cc.getPhone3());
        table.addCell(seller);
        PdfPCell buyer = getPartyAddress("Одержувач:",
                basic.getBuyerName(),
                basic.getBuyerLineOne(),
                basic.getBuyerPostcode(),
                basic.getBuyerCityName(),
                order.getCustomer().getEmail(),
                order.getCustomer().getPhone(),
                "",
                "");
        table.addCell(buyer);
        document.add(table);

        // line items
        table = new PdfPTable(6);
        table.setWidthPercentage(100);
        table.setSpacingBefore(20);
        table.setSpacingAfter(10);
        table.setWidths(new int[]{1, 5, 1, 1, 1, 1});
        table.addCell(getCell("Артикул:", Element.ALIGN_LEFT, font10b)).setBackgroundColor(BaseColor.LIGHT_GRAY);
        table.addCell(getCell("Найменування:", Element.ALIGN_LEFT, font10b)).setBackgroundColor(BaseColor.LIGHT_GRAY);
        table.addCell(getCell("Ціна:", Element.ALIGN_LEFT, font10b)).setBackgroundColor(BaseColor.LIGHT_GRAY);
        table.addCell(getCell("Кількість:", Element.ALIGN_LEFT, font10b)).setBackgroundColor(BaseColor.LIGHT_GRAY);
        table.addCell(getCell("Одиниця:", Element.ALIGN_LEFT, font10b)).setBackgroundColor(BaseColor.LIGHT_GRAY);
        table.addCell(getCell("Сума:", Element.ALIGN_LEFT, font10b)).setBackgroundColor(BaseColor.LIGHT_GRAY);

        for (OrderEntryDto entry : entryList) {
            table.addCell(getCell(entry.getSKU(), Element.ALIGN_CENTER, font10));
            table.addCell(getCell(entry.getProductName(), Element.ALIGN_LEFT, font10));
            table.addCell(getCell(String.valueOf(entry.getPriceUAH()), Element.ALIGN_RIGHT, font10));
            table.addCell(getCell(String.valueOf(entry.getQuantity()), Element.ALIGN_CENTER, font10));
            table.addCell(getCell(String.valueOf(entry.getUnit()), Element.ALIGN_CENTER, font10));
            table.addCell(getCell(String.valueOf(entry.getSubtotal()), Element.ALIGN_RIGHT, font10b));
        }
        document.add(table);

        // grand totals
        document.add(getTotalsTable(basic.getGrandTotalAmount(), "грн."));

        // payment info
        document.add(getPaymentInfo(
                basic.getPaymentMeansPayeeFinancialInstitutionName(),
                basic.getPaymentMeansPayeeFinancialInstitutionBIC(),
                basic.getPaymentMeansPayeeAccountIBAN()));

        // XML version
        InvoiceDOM dom = new InvoiceDOM(basic);
        PdfDictionary parameters = new PdfDictionary();
        parameters.put(PdfName.MODDATE, new PdfDate());
        PdfFileSpecification fileSpec = writer.addFileAttachment(
                "ZUGFeRD invoice", dom.toXML(), null,
                "ZUGFeRD-invoice.xml", "application/xml",
                AFRelationshipValue.Alternative, parameters);
        PdfArray array = new PdfArray();
        array.add(fileSpec.getReference());
        writer.getExtraCatalog().put(PdfName.AF, array);

        String ldt = LocalDateTime.now(ZoneId.of("Europe/Kiev")).format(DateTimeFormatter.ofPattern("yyyyMMddkkmmss"));

        String filename = String.format("attachment;filename=%s-%05d", ldt, order.getId());

        response.setHeader("Content-Disposition", filename);
    }

    private BasicProfile createBasicProfileData(List<OrderEntryDto> entryList, Order order, CompanyCredentials cc) {
        BasicProfileImp profileImp = new BasicProfileImp();
        importData(profileImp, order, cc);
        importBasicData(profileImp, entryList, order, cc);
        return profileImp;
    }

    private void importData(BasicProfileImp profileImp, Order order, CompanyCredentials cc) {
        profileImp.setTest(true);
        profileImp.setId(String.format("№ I/%05d", order.getId()));
        profileImp.setName("Рахунок-фактура");
        profileImp.setTypeCode(DocumentTypeCode.COMMERCIAL_INVOICE);
        profileImp.setDate(Date.from(Instant.from(order.getTimeOfOrder().atZone(ZoneId.of("Europe/Kiev")))), DateFormatCode.YYYYMMDD);
        profileImp.setSellerName(cc.getName());
        profileImp.setSellerLineOne(cc.getAddress().getStreet() + ", " + cc.getAddress().getNumber());
        profileImp.setSellerPostcode(cc.getAddress().getPostcode());
        profileImp.setSellerCityName(cc.getAddress().getCity());
        profileImp.setSellerCountryID("UA");
        profileImp.addSellerTaxRegistration(TaxIDTypeCode.FISCAL_NUMBER, cc.getTaxcode());
        profileImp.setBuyerName(String.format("%s, %s", order.getCustomer().getSecondName(), order.getCustomer().getFirstName()));
        profileImp.setBuyerPostcode(order.getPostcode());
        profileImp.setBuyerLineOne(order.getStreet());
        profileImp.setBuyerCityName(order.getCity());
        profileImp.setBuyerCountryID("UA");
        profileImp.setPaymentReference(String.format("%09d", order.getId()));
        profileImp.setInvoiceCurrencyCode("UAH");
        profileImp.addPaymentMeans("", "", cc.getBankDetails().getIBAN(), "", "", cc.getBankDetails().getSWIFT(), "", cc.getBankDetails().getBankName());
    }

    private void importBasicData(BasicProfileImp profileImp, List<OrderEntryDto> entryList, Order order, CompanyCredentials cc) {
        profileImp.addNote(new String[]{"This is a test Invoice"});
        double total = entryList.stream().mapToDouble(OrderEntryDto::getSubtotal).sum();
        for (OrderEntryDto entry : entryList) {
            profileImp.addIncludedSupplyChainTradeLineItem(format4dec(entry.getQuantity()), "C62", entry.getProductName());
            profileImp.addApplicableTradeTax(format2dec(total), "UAH", TaxTypeCode.VALUE_ADDED_TAX, format2dec(total), "UAH", format2dec(0));
        }
        profileImp.setMonetarySummation(format2dec(0), "UAH",
                format2dec(0), "UAH",
                format2dec(0), "UAH",
                format2dec(0), "UAH",
                format2dec(0), "UAH",
                format2dec(total), "UAH");
    }

    private String convertDate(Date d, String newFormat) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(newFormat, new Locale("uk", "UA"));
        return "від " + sdf.format(d) + " р.";
    }

    private PdfPCell getPartyAddress(String who, String name, String line1, String postcode, String city, String email, String phone1, String phone2, String phone3) {
        PdfPCell cell = new PdfPCell();
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.addElement(new Paragraph(who, font12b));
        cell.addElement(new Paragraph(name, font12));
        cell.addElement(new Paragraph(line1, font12));
        cell.addElement(new Paragraph(String.format("%s %s", postcode, city), font12));
        cell.addElement(new Paragraph(email, font12));
        cell.addElement(new Paragraph(String.format("%s %s %s", phone1, phone2, phone3), font12));
        return cell;
    }

    private PdfPCell getCell(String value, int alignment, Font font) {
        PdfPCell cell = new PdfPCell();
        cell.setUseAscender(true);
        cell.setUseDescender(true);
        Paragraph p = new Paragraph(value, font);
        p.setAlignment(alignment);
        cell.setPaddingTop(5);
        cell.setPaddingBottom(5);
        cell.setPaddingLeft(3);
        cell.setPaddingRight(3);
        cell.addElement(p);
        return cell;
    }

    private PdfPTable getTotalsTable(String grandTotal, String currency) throws DocumentException {
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.setWidths(new int[]{1, 1});
        table.addCell(getCell("Разом:", Element.ALIGN_LEFT, font12b));
        table.addCell(getCell(grandTotal + " " + currency, Element.ALIGN_RIGHT, font12b));

        return table;
    }
    private Paragraph getPaymentInfo(String[] bankName, String[] bic, String[] iban) {
        Paragraph p = new Paragraph("Реквізити:", font12);
        int n = bic.length;
        for (int i = 0; i < n; i++) {
            p.add(Chunk.NEWLINE);
            p.add(String.format("%s\nBIC: %s\nIBAN: %s", bankName[i], bic[i], iban[i]));
        }
        return p;
    }

    private static String format2dec(double d) {
        return String.format("%.2f", d);
    }

    private static String format4dec(double d) {
        return String.format("%.4f", d);
    }
}
