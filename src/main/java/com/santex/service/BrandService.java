package com.santex.service;

import com.santex.dto.SearchCriteria;
import com.santex.dto.SearchCriteriaAdminProduct;
import com.santex.entity.Brand;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface BrandService {

    void add(String brandName, MultipartFile logo, String url);

    void edit(int id, String brandName, boolean logoAvailability, MultipartFile logo, String url);

    void remove(int id);

    Brand findById(int id);

    List<Brand> findAll();

    List<Brand> findByCriteriaCustomer(SearchCriteria criteria);

    List<Brand> findByCriteriaAdmin(SearchCriteriaAdminProduct criteriaAdmin);

    List<Brand> findForFooterLogos();

}
