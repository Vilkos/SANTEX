package com.santex.service.implementation;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.ICC_Profile;
import com.itextpdf.text.pdf.PdfAConformanceLevel;
import com.itextpdf.text.pdf.PdfAWriter;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.xml.xmp.PdfAXmpWriter;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.view.AbstractView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Map;

public abstract class AbstractITextPdfView extends AbstractView {

    public AbstractITextPdfView() {
        this.setContentType("application/pdf");
    }

    protected boolean generatesDownloadContent() {
        return true;
    }

    protected final void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ByteArrayOutputStream baos = this.createTemporaryOutputStream();
        Document document = new Document(PageSize.A4);
        document.addAuthor("ecommerce online system");
        document.addCreator("ecommerce online system");
        document.addSubject("Commercial invoice");
        document.addCreationDate();
        PdfAWriter writer = this.newWriter(document, baos);
        writer.setPdfVersion(PdfWriter.VERSION_1_7);
        writer.createXmpMetadata();
        writer.getXmpWriter().setProperty(PdfAXmpWriter.zugferdSchemaNS, PdfAXmpWriter.zugferdDocumentFileName, "ZUGFeRD-invoice.xml");
        this.prepareWriter(model, writer, request);
        this.buildPdfMetadata(model, document, request);
        document.open();
        Resource resource = getApplicationContext().getResource("classpath:colorProfile/sRGB_CS_profile.icm");
        ICC_Profile icc = ICC_Profile.getInstance(resource.getInputStream());
        writer.setOutputIntents("Custom", "", "http://www.color.org", "sRGB IEC61966-2.1", icc);
        this.buildPdfDocument(model, document, writer, request, response);
        document.close();
        this.writeToResponse(response, baos);
    }

    private PdfAWriter newWriter(Document document, OutputStream os) throws DocumentException {
        return PdfAWriter.getInstance(document, os, PdfAConformanceLevel.ZUGFeRDBasic);
    }

    private void prepareWriter(Map<String, Object> model, PdfAWriter writer, HttpServletRequest request) throws DocumentException {
        writer.setViewerPreferences(this.getViewerPreferences());
    }

    private int getViewerPreferences() {
        return PdfAWriter.ALLOW_PRINTING | PdfAWriter.PageLayoutSinglePage;
    }

    private void buildPdfMetadata(Map<String, Object> model, Document document, HttpServletRequest request) {
    }

    protected abstract void buildPdfDocument(Map<String, Object> var1, Document var2, PdfAWriter var3, HttpServletRequest var4, HttpServletResponse var5) throws Exception;
}


