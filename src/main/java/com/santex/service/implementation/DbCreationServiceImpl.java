package com.santex.service.implementation;

import com.santex.dao.*;
import com.santex.entity.*;
import com.santex.entity.Currency;
import com.santex.service.DbCreationService;
import com.santex.service.ProductService;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.*;

@Lazy
@Service
public class DbCreationServiceImpl implements DbCreationService {
    private final Path path = Paths.get(System.getProperty("catalina.home"), "excel");
    private final Path file = path.resolve("santex_db.xlsx");
    private final String[] array = {"AKFIX", "AMETIST", "ANGO", "ANSAN", "AQUA KUT", "AQUAFILTER", "AQUASANITA", "ARCO", "ARMATURA", "ASPRINN", "ATAK", "ATLAS", "BOSSINI CUBE", "BRITA", "CHAMPION", "CHEMOS", "CRISTAL", "CRYSTAL", "DANIEL", "DISHI", "DOGUS", "DOSPEL", "EMMEVI", "EVCI", "FILMTEC", "FORMA", "G-LAUF", "GEBEX", "GO PLAST", "GROHE", "HANSGROHE", "HAIBA", "HESSA", "HIDROTEK", "ICMA", "ITAP", "K.K.POL", "KFA", "KLUDI", "KOMETA", "LIDER", "MASTER", "MATEU", "MC ALPINE", "MIRANDA", "MIXXEN", "MOFEM", "NOVA PLASTIK", "NTM", "O&L", "ODA", "ORALUX", "OSRAM/SIEMENS", "RAF", "RAMSPOTT", "RAVAK", "REMER", "SAM HOLDING", "SANIT", "SANITECH", "SANNEX", "SCHELL", "SIROFLEX", "SLOVARM", "SOLOMON", "SPIROFLEX", "STORM", "TEKA", "TEKNO-TEL", "TORK", "TORO", "TRADING", "TRION", "UK INOX", "UNIPAK", "VENTS", "VIDIMA", "VIEGA", "VIOLLI", "VIOR", "VIYA", "WASSERFIX", "WATERSTAL", "WELA", "WKRET-MET", "ZEGOR", "ZERIX", "АББАТ", "АЗОЦМ", "АКВАФОР", "АНИ ПЛАСТ", "БАРЬЕР", "в-цтво Китай", "в-цтво Польща", "в-цтво Туреччина", "в-цтво Україна", "ГЕЙЗЕР", "ДВАЙСЕН", "ДОМОВЕНТ", "ЕВРОПЛАСТ", "НВО ФРУНЗЕ", "САНТЕХКОМПЛЕКТ", "СВОД", "СКЛОПРИЛАД", "УКЛАД", "УКРСАНТАРМ"};
    private final ProductService productService;
    private final ProductDao productDao;
    private final CategoryDao categoryDao;
    private final SubcategoryDao subcategoryDao;
    private final BrandDao brandDao;
    private final UnitDao unitDao;

    public DbCreationServiceImpl(ProductService productService, ProductDao productDao, CategoryDao categoryDao, SubcategoryDao subcategoryDao, BrandDao brandDao, UnitDao unitDao) {
        this.productService = productService;
        this.productDao = productDao;
        this.categoryDao = categoryDao;
        this.subcategoryDao = subcategoryDao;
        this.brandDao = brandDao;
        this.unitDao = unitDao;
    }

    @Async
    @Override
    @Transactional
    public void run() {
        List<Product> products = parse();
        for (Product product : products) {

            String unitName = product.getUnit().getUnitName();
            Unit unit = unitDao.findByUnitName(unitName).orElse(new Unit(unitName));
            unitDao.save(unit);

            String brandName = product.getBrand().getBrandName();
            Brand brand = brandDao.findByBrandName(brandName).orElse(new Brand(brandName, ""));
            brandDao.save(brand);

            String categoryName = product.getSubcategory().getCategory().getCategoryName();
            Category category = categoryDao.findByCategoryName(categoryName).orElse(new Category(categoryName));
            categoryDao.save(category);

            String subcategoryName = product.getSubcategory().getSubcategoryName();
            List<Subcategory> subcategories = subcategoryDao.findByCategoryId(category.getId());
            Subcategory subcategory = subcategoryDao.getByCatIdAndSubName(category.getId(), subcategoryName);
            if (subcategories.stream().noneMatch(sub -> sub.getSubcategoryName().equals(subcategoryName)) || subcategories.isEmpty()) {
                Subcategory s = new Subcategory(subcategoryName);
                s.setCategory(category);
                subcategory = subcategoryDao.save(s);
            }

            Optional<Product> productFromDb = productDao.findBySKU(product.getSKU());
            if (productFromDb.isPresent()) {
                productFromDb.get().setSKU(product.getSKU());
                productFromDb.get().setPrice(product.getPrice());
                productFromDb.get().setDiscountPrice(product.getDiscountPrice());
                productFromDb.get().setCurrency(product.getCurrency());
                productFromDb.get().setProductName(product.getProductName());
                productFromDb.get().setSubcategory(subcategory);
                productFromDb.get().setBrand(brand);
                productFromDb.get().setUnit(unit);
                productDao.save(productFromDb.get());
            } else {
                productService.add(product.getSKU(),
                        true,
                        true,
                        product.getPrice(),
                        product.getDiscountPrice(),
                        product.getCurrency(),
                        product.getProductName(),
                        subcategory,
                        brand,
                        unit);
            }
        }
    }

    private List<Product> parse() {
        Workbook workbook;
        List<Product> productList = new ArrayList<>();
        try {
            InputStream inputStream = Files.newInputStream(file);
            workbook = new XSSFWorkbook(inputStream);
            Sheet firstSheet = workbook.getSheetAt(0);
            for (Row nextRow : firstSheet) {
                Iterator<Cell> cellIterator = nextRow.cellIterator();
                Product product = new Product();
                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();
                    int columnIndex = cell.getColumnIndex();
                    switch (columnIndex) {
                        case 0:
                            product.setSKU(String.valueOf((int) (cell.getNumericCellValue())));
                            break;
                        case 1:
                            String price = cell.getStringCellValue();
                            NumberFormat format = null;
                            format = DecimalFormat.getInstance(Locale.FRANCE);
                            Number number = format != null ? format.parse(price) : null;
                            product.setPrice((int) ((number != null ? number.doubleValue() : 0) * 100));
                            if (price.contains("$")) {
                                product.setCurrency(Currency.USD);
                            } else if (price.contains("eur")) {
                                product.setCurrency(Currency.EUR);
                            } else if (price.contains("грн")) {
                                product.setCurrency(Currency.UAH);
                            } else if (price.isEmpty()) {
                                product.setPrice(0);
                            }
                            break;
                        case 2:
                            product.setProductName(cell.getStringCellValue());

                            Brand brand = new Brand(brandChecker(array, cell.getStringCellValue()), "");
                            product.setBrand(brand);

                            break;
                        case 3:
                        /*String brandName = cell.getStringCellValue();
                        Brand brand = new Brand(brandName, "description");
                        product.setBrand(brand);*/
                            break;
                        case 4:
                            String subcategoryName = cell.getStringCellValue();
                            Subcategory subcategory = new Subcategory(subcategoryName);
                            product.setSubcategory(subcategory);
                            break;
                        case 5:
                            String categoryName = cell.getStringCellValue();
                            Category category = new Category(categoryName);
                            product.getSubcategory().setCategory(category);
                            break;
                        case 6:
                            String unitName = cell.getStringCellValue();
                            Unit unit = new Unit(unitName);
                            product.setUnit(unit);
                            break;
                    }
                }
                productList.add(product);
            }
            workbook.close();
            inputStream.close();
        } catch (IOException | ParseException e) {
            e.printStackTrace();
            if (Files.notExists(path)) {
                try {
                    Files.createDirectory(path);
                    System.out.println("Directory was successfully created.");
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return productList;
    }

    private String brandChecker(String[] arr, String targetValue) {
        String brandName = "NONAME";
        for (String anArr : arr) {
            if (targetValue.contains(anArr)) {
                brandName = anArr;
            }
        }
        if (targetValue.contains(" SANIT ")) {
            brandName = "SANIT";
        }
        if (targetValue.contains(" AQUASANITA ")) {
            brandName = "AQUASANITA";
        }
        return brandName;
    }
}
