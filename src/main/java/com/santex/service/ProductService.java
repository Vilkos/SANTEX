package com.santex.service;

import com.santex.dto.SearchCriteria;
import com.santex.dto.SearchCriteriaAdminProduct;
import com.santex.dto.ProductAdminDto;
import com.santex.dto.ProductCustomerDto;
import com.santex.entity.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface ProductService {

    void add(String SKU, boolean availability, boolean priceVisibility, int price, int discountPrice, Currency currency, String productName, MultipartFile image, Subcategory subcategory, Brand brand, Unit unit);

    void add(String SKU, boolean availability, boolean priceVisibility, int price, int discountPrice, Currency currency, String productName, Subcategory subcategory, Brand brand, Unit unit);

    void edit(int id, String SKU, boolean availability, boolean priceVisibility, int price, int discount, Currency currency, String productName, MultipartFile image, boolean imageAvailability, int subcategory, int brand, int unit);

    Product remove(int id);

    Product findById(int id);

    Optional<ProductCustomerDto> findBySKU(String SKU);

    ProductCustomerDto findByIdForCustomer(int id);

    ProductAdminDto findByIdForAdmin(int id);

    List<ProductCustomerDto> findAllForCustomerPriceList();

    List<ProductAdminDto> findAllForAdminPriceList();

    Page<ProductCustomerDto> findByCriteriaCustomer(SearchCriteria criteria, Pageable p);

    Page<ProductAdminDto> findByCriteriaAdmin(SearchCriteriaAdminProduct criteriaAdmin, Pageable p);
}

