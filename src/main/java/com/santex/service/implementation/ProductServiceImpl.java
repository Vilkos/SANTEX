package com.santex.service.implementation;

import com.santex.dto.SearchCriteria;
import com.santex.dto.SearchCriteriaAdminProduct;
import com.santex.dao.*;
import com.santex.dto.ProductAdminDto;
import com.santex.dto.ProductCustomerDto;
import com.santex.entity.*;
import com.santex.dto.Event;
import com.santex.service.ProductImageService;
import com.santex.service.ProductService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

import static com.santex.service.implementation.ProductMapper.*;
import static com.santex.service.implementation.ProductSpecifications.byCriteriaAdmin;
import static com.santex.service.implementation.ProductSpecifications.byCriteriaCustomer;

@Service
@Repository
@Transactional
public class ProductServiceImpl implements ProductService {
    private final ProductDao productDao;
    private final SubcategoryDao subcategoryDao;
    private final BrandDao brandDao;
    private final UnitDao unitDao;
    private final ProductImageService imageService;
    private final ApplicationEventPublisher publisher;

    public ProductServiceImpl(ProductDao productDao, SubcategoryDao subcategoryDao, BrandDao brandDao, UnitDao unitDao, ProductImageService imageService, ApplicationEventPublisher publisher) {
        this.productDao = productDao;
        this.subcategoryDao = subcategoryDao;
        this.brandDao = brandDao;
        this.unitDao = unitDao;
        this.imageService = imageService;
        this.publisher = publisher;
    }

    @Override
    public void add(String SKU, boolean availability, boolean priceVisibility, int price, int discountPrice, Currency currency, String productName, MultipartFile image, Subcategory subcategory, Brand brand, Unit unit) {
        Product product = new Product(SKU, availability, priceVisibility, price, discountPrice, currency, productName);
        product.setSubcategory(subcategory);
        product.setBrand(brand);
        product.setUnit(unit);
        productDao.save(product);
        publisher.publishEvent(new Event());
        if (!image.isEmpty()) {
            imageService.addFromWeb(image, SKU);
        }
    }

    @Override
    public void add(String SKU, boolean availability, boolean priceVisibility, int price, int discountPrice, Currency currency, String productName, Subcategory subcategory, Brand brand, Unit unit) {
        Product product = new Product(SKU, availability, priceVisibility, price, discountPrice, currency, productName);
        product.setSubcategory(subcategory);
        product.setBrand(brand);
        product.setUnit(unit);
        productDao.save(product);
        publisher.publishEvent(new Event());
    }

    @Override
    public void edit(int id, String SKU, boolean availability, boolean priceVisibility, int price, int discountPrice, Currency currency, String productName, MultipartFile image, boolean imageAvailability, int subcategory, int brand, int unit) {
        Product product = productDao.findById(id);
        product.setSKU(SKU);
        product.setAvailability(availability);
        product.setPrice(price);
        product.setDiscountPrice(discountPrice);
        product.setCurrency(currency);
        product.setProductName(productName);
        product.setPriceVisibility(priceVisibility);
        product.setSubcategory(subcategoryDao.findById(subcategory).orElse(null));
        product.setBrand(brandDao.findById(brand).orElse(null));
        product.setUnit(unitDao.findById(unit).orElse(null));
        productDao.save(product);
        publisher.publishEvent(new Event());
        if (!image.isEmpty()) {
            imageService.addFromWeb(image, SKU);
        } else if (!imageAvailability) {
            imageService.remove(SKU);
        }
    }

    @Override
    public Product remove(int id) {
        Product product = productDao.findById(id);
        imageService.remove(product.getSKU());
        productDao.deleteById(id);
        publisher.publishEvent(new Event());
        return product;
    }

    @Override
    @Transactional(readOnly = true)
    public Product findById(int id) {
        return productDao.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProductCustomerDto> findBySKU(String SKU) {
        return mapEntityOptionalIntoDTO(productDao.findBySKU(SKU));
    }

    @Override
    @Transactional(readOnly = true)
    public ProductCustomerDto findByIdForCustomer(int id) {
        return mapEntityIntoDTO(productDao.findById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public ProductAdminDto findByIdForAdmin(int id) {
        return mapEntityIntoAdminDTO(productDao.findById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductCustomerDto> findAllForCustomerPriceList() {
        return mapEntitiesIntoDTOs(productDao.findAllForCustomerPrice());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductAdminDto> findAllForAdminPriceList() {
        return mapEntitiesIntoAdminDTOs(productDao.findAll(Sort.by(Sort.Direction.ASC, "productName")));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductAdminDto> findByCriteriaAdmin(SearchCriteriaAdminProduct criteriaAdmin, Pageable p) {
        return mapEntityPageIntoAdminDTOPage(productDao.findAll(byCriteriaAdmin(criteriaAdmin), p));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductCustomerDto> findByCriteriaCustomer(SearchCriteria criteria, Pageable p) {
        return mapEntityPageIntoDTOPage(productDao.findAll(byCriteriaCustomer(criteria), p));
    }
}
