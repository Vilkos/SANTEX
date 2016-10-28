package com.santex.service.implementation;

import com.santex.dto.SearchCriteria;
import com.santex.dto.SearchCriteriaAdminProduct;
import com.santex.dao.BrandDao;
import com.santex.entity.Brand;
import com.santex.service.BrandService;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.*;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;

@Service
@Repository
@Transactional
public class BrandServiceImpl implements BrandService {
    private static final Path logos = Paths.get(System.getProperty("catalina.home"), "brandLogos");
    private final BrandDao brandDao;

    public BrandServiceImpl(BrandDao brandDao) {
        this.brandDao = brandDao;
    }

    @Override
    public void add(String brandName, MultipartFile logo, String url) {
        Brand brand = brandDao.save(new Brand(brandName, url));
        addLogo(logo, brand.getId());
    }

    @Override
    public void edit(int id, String brandName, boolean logoAvailability, MultipartFile logo, String url) {
        Brand brand = brandDao.findOne(id);
        brand.setBrandName(brandName);
        brand.setLogoAvailability(logoAvailability);
        brand.setUrl(url);
        brandDao.save(brand);
        if (!logo.isEmpty()) {
            addLogo(logo, id);
        } else if (!logoAvailability) {
            deleteLogo(id);
        }
    }

    @Override
    public void remove(int id) {
        deleteLogo(id);
        brandDao.delete(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Brand findById(int id) {
        Brand brand = brandDao.findOne(id);
        brand.setLogoAvailability(brandLogosChecker(brand.getId()));
        return brand;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Brand> findAll() {
        return brandDao.findAll()
                .stream()
                .sorted(Comparator.comparing(Brand::getBrandName))
                .map(BrandServiceImpl::mapper)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Brand> findByCriteriaCustomer(SearchCriteria criteria) {
        return brandDao.findAll(BrandSpecifications.byCriteriaCustomer(criteria));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Brand> findByCriteriaAdmin(SearchCriteriaAdminProduct criteriaAdmin) {
        return brandDao.findAll(BrandSpecifications.byCriteriaAdmin(criteriaAdmin));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Brand> findForFooterLogos() {
        return brandDao.findAll()
                .stream()
                .map(BrandServiceImpl::mapper)
                .sorted(Comparator.comparing(Brand::getBrandName))
                .filter(Brand::isLogoAvailability)
                .collect(Collectors.toList());
    }

    private void addLogo(MultipartFile logo, int id) {
        if (!logo.isEmpty()) {
            Path logoFile = logos.resolve(id + ".svg");
            try (OutputStream out = new BufferedOutputStream(Files.newOutputStream(logoFile, CREATE, TRUNCATE_EXISTING))) {
                out.write(logo.getBytes(), 0, logo.getBytes().length);
                out.flush();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void deleteLogo(int id) {
        Path logoFile = logos.resolve(id + ".svg");
        try {
            Files.deleteIfExists(logoFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Brand mapper(Brand brand) {
        brand.setLogoAvailability(brandLogosChecker(brand.getId()));
        return brand;
    }

    private static boolean brandLogosChecker(int id) {
        Path logo = logos.resolve(id + ".svg");
        return Files.exists(logo);
    }
}
