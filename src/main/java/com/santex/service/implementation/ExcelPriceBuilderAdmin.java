package com.santex.service.implementation;

import com.santex.dto.ProductAdminDto;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.servlet.view.document.AbstractXlsxView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

public class ExcelPriceBuilderAdmin extends AbstractXlsxView {

    @Async
    @Override
    protected void buildExcelDocument(Map<String, Object> map,
                                      Workbook workbook,
                                      HttpServletRequest httpServletRequest,
                                      HttpServletResponse httpServletResponse) throws Exception {

        Sheet sheet = workbook.createSheet("PRODUCTS");
        setExcelHeader(sheet);

        @SuppressWarnings("unchecked")
        List<ProductAdminDto> productList = (List<ProductAdminDto>) map.get("productList");
        setExcelRows(sheet, workbook, productList);

        sheet.setFitToPage(true);
        sheet.autoSizeColumn(0);
        sheet.autoSizeColumn(1);
        sheet.autoSizeColumn(4);
        sheet.autoSizeColumn(5);
        sheet.autoSizeColumn(6);

        String ldt = LocalDateTime.now(ZoneId.of("Europe/Kiev")).format(DateTimeFormatter.ISO_DATE);
        String fileName = "attachment;filename=santex_" + ldt + ".xlsx";

        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        httpServletResponse.setHeader("Content-Disposition", fileName);
    }

    private void setExcelHeader(Sheet sheet) {
        Row excelHeader = sheet.createRow(0);

        excelHeader.createCell(0).setCellValue("SKU");
        excelHeader.createCell(1).setCellValue("Product name");
        excelHeader.createCell(2).setCellValue("Price");
        excelHeader.createCell(3).setCellValue("Unit");
        excelHeader.createCell(4).setCellValue("Category");
        excelHeader.createCell(5).setCellValue("Subcategory");
        excelHeader.createCell(6).setCellValue("Brand");
    }

    private void setExcelRows(Sheet sheet, Workbook wb, List<ProductAdminDto> products) {

        DataFormat df = wb.createDataFormat();
        CellStyle usdStyle = wb.createCellStyle();
        CellStyle eurStyle = wb.createCellStyle();
        CellStyle uahStyle = wb.createCellStyle();
        CellStyle defaultCurrency = wb.createCellStyle();

        SheetConditionalFormatting sheetCF = sheet.getSheetConditionalFormatting();
        ConditionalFormattingRule rule1 = sheetCF.createConditionalFormattingRule("MOD(ROW(),2)");
        PatternFormatting fill1 = rule1.createPatternFormatting();
        fill1.setFillBackgroundColor(IndexedColors.GREY_25_PERCENT.index);
        fill1.setFillPattern(PatternFormatting.SOLID_FOREGROUND);

        CellRangeAddress[] regions = {CellRangeAddress.valueOf("A1:G2")};

        sheetCF.addConditionalFormatting(regions, rule1);

        usdStyle.setDataFormat(df.getFormat("[$$-409]# ##0.00"));
        eurStyle.setDataFormat(df.getFormat("# ##0.00 [$€-1];-# ##0.00 [$€-1]"));
        uahStyle.setDataFormat(df.getFormat("# ##0.00 [$₴-422]"));
        defaultCurrency.setDataFormat(df.getFormat("# ##0.00;-# ##0.00"));

        int record = 1;
        for (ProductAdminDto p : products) {
            Row excelRow = sheet.createRow(record++);

            excelRow.createCell(0).setCellValue(p.getSKU());
            excelRow.createCell(1).setCellValue(p.getProductName());
            excelRow.createCell(2).setCellValue(p.getPrice());
            switch (p.getCurrency()) {
                case USD:
                    excelRow.getCell(2).setCellStyle(usdStyle);
                    break;
                case EUR:
                    excelRow.getCell(2).setCellStyle(eurStyle);
                    break;
                case UAH:
                    excelRow.getCell(2).setCellStyle(uahStyle);
                    break;
                default:
                    excelRow.getCell(2).setCellStyle(defaultCurrency);
            }
            excelRow.createCell(3).setCellValue(p.getUnit().getUnitName());
            excelRow.createCell(4).setCellValue(p.getSubcategory().getCategory().getCategoryName());
            excelRow.createCell(5).setCellValue(p.getSubcategory().getSubcategoryName());
            excelRow.createCell(6).setCellValue(p.getBrand().getBrandName());
        }
    }
}
