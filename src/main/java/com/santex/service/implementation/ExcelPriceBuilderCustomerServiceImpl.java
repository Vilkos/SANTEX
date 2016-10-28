package com.santex.service.implementation;

import com.santex.dto.Event;
import com.santex.dto.ProductCustomerDto;
import com.santex.service.ExcelPriceBuilderCustomerService;
import com.santex.service.ProductService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

import java.io.*;
import java.nio.file.*;
import java.util.List;
import java.util.concurrent.*;

import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;

@Service
public class ExcelPriceBuilderCustomerServiceImpl implements ExcelPriceBuilderCustomerService {
    private static final Path priceList = Paths.get(System.getProperty("catalina.home"), "priceList");
    private static final Path file = priceList.resolve("priceList.xlsx");
    private final ProductService productService;
    private final ScheduledExecutorService scheduler;
    private final Runnable builder = this::buildExcelDocument;
    private ScheduledFuture futurePriceList;

    public ExcelPriceBuilderCustomerServiceImpl(ProductService productService) {
        this.productService = productService;
        this.scheduler = Executors.newSingleThreadScheduledExecutor();
        this.futurePriceList = scheduler.schedule(builder, 5, TimeUnit.MINUTES);
    }

    @TransactionalEventListener
    public void buildWithDelay(Event event) {
        if (!futurePriceList.isDone()) futurePriceList.cancel(false);
        futurePriceList = scheduler.schedule(builder, 5, TimeUnit.MINUTES);
    }

    @EventListener
    public void shutdownScheduler(ContextClosedEvent event) {
        scheduler.shutdown();
    }

    @Override
    @Transactional(readOnly = true)
    public void buildExcelDocument() {

        Workbook workbook = new XSSFWorkbook();

        Sheet sheet = workbook.createSheet("PRODUCTS");
        setExcelHeader(sheet);

        List<ProductCustomerDto> products = productService.findAllForCustomerPriceList();
        setExcelRows(sheet, workbook, products);

        sheet.setFitToPage(true);
        sheet.autoSizeColumn(0);
        sheet.autoSizeColumn(1);
        sheet.autoSizeColumn(2);
        sheet.autoSizeColumn(4);
        sheet.autoSizeColumn(5);

        writeDocumentToFile(workbook);
    }

    private void writeDocumentToFile(Workbook workbook) {
        if (Files.notExists(priceList)) try {
            Files.createDirectory(priceList);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (OutputStream out = new BufferedOutputStream(Files.newOutputStream(file, CREATE, TRUNCATE_EXISTING))) {
            workbook.write(out);
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setExcelHeader(Sheet sheet) {
        Row excelHeader = sheet.createRow(0);
        excelHeader.createCell(0).setCellValue("Артикул");
        excelHeader.createCell(1).setCellValue("Найменування");
        excelHeader.createCell(2).setCellValue("Ціна (грн.)");
        excelHeader.createCell(3).setCellValue("Одиниця");
        excelHeader.createCell(4).setCellValue("Бренд");
        excelHeader.createCell(5).setCellValue("Категорія");
    }

    private void setExcelRows(Sheet sheet, Workbook wb, List<ProductCustomerDto> products) {
        DataFormat df = wb.createDataFormat();
        CellStyle uahStyle = wb.createCellStyle();

        uahStyle.setDataFormat(df.getFormat("# ##0.00 [$₴-422]"));

        int record = 1;
        for (ProductCustomerDto p : products) {
            Row excelRow = sheet.createRow(record++);
            excelRow.createCell(0).setCellValue(p.getSKU());
            excelRow.createCell(1).setCellValue(p.getProductName());
            excelRow.createCell(2);
            if (p.getFinalPrice() != 0.0) {
                excelRow.getCell(2).setCellValue(p.getFinalPrice());
                excelRow.getCell(2).setCellStyle(uahStyle);
            } else {
                excelRow.getCell(2).setCellValue("За запитом");
            }
            excelRow.createCell(3).setCellValue(p.getUnit().getUnitName());
            excelRow.createCell(4).setCellValue(p.getBrand().getBrandName());
            excelRow.createCell(5).setCellValue(p.getSubcategory().getCategory().getCategoryName());
        }
    }
}
