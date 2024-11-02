package com.santex.service.implementation;

import com.santex.dto.ProductAdminDto;
import com.santex.dto.ProductCustomerDto;
import com.santex.entity.Product;
import org.springframework.data.domain.Page;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.santex.service.PriceService.*;
import static com.santex.service.implementation.CurrencyConverter.currencyConverter;

final class ProductMapper {
    private static final Path path = Paths.get(System.getProperty("catalina.home"), "images");

    static List<ProductCustomerDto> mapEntitiesIntoDTOs(List<Product> entities) {
        return entities.stream().map(ProductMapper::mapEntityIntoDTO).collect(Collectors.toList());
    }

    static List<ProductAdminDto> mapEntitiesIntoAdminDTOs(List<Product> entities) {
        return entities.stream().map(ProductMapper::mapEntityIntoAdminDTO).collect(Collectors.toList());
    }

    static Page<ProductCustomerDto> mapEntityPageIntoDTOPage(Page<Product> source) {
        return source.map(ProductMapper::mapEntityIntoDTO);
    }

    static Page<ProductAdminDto> mapEntityPageIntoAdminDTOPage(Page<Product> source) {
        return source.map(ProductMapper::mapEntityIntoAdminDTO);
    }

    static Optional<ProductCustomerDto> mapEntityOptionalIntoDTO(Optional<Product> source) {
        return source.map(ProductMapper::mapEntityIntoDTO);
    }

    static ProductCustomerDto mapEntityIntoDTO(Product entity) {
        double price = currencyConverter(entity.getPrice(), entity.getCurrency());
        double discountPrice = entity.getDiscountPrice() > 0 ? currencyConverter(entity.getDiscountPrice(), entity.getCurrency()) : 0;
        double finalPrice = entity.getDiscountPrice() > 0 ? discountPrice : price;
        return new ProductCustomerDto(
                entity.getId(),
                entity.getSKU(),
                price,
                discountPrice,
                finalPrice,
                toStringWithLocale(price),
                toStringWithLocale(discountPrice),
                toStringEN(finalPrice),
                entity.isPriceVisibility(),
                entity.getProductName(),
                imageChecker(entity.getSKU()),
                entity.getSubcategory(),
                entity.getBrand(),
                entity.getUnit());
    }

    static ProductAdminDto mapEntityIntoAdminDTO(Product entity) {
        return new ProductAdminDto(
                entity.getId(),
                entity.getSKU(),
                entity.isAvailability(),
                entity.isPriceVisibility(),
                toDouble(entity.getPrice()),
                currencyConverter(entity.getPrice(), entity.getCurrency()),
                toDouble(entity.getDiscountPrice()),
                entity.getCurrency(),
                entity.getProductName(),
                entity.getQuantityOfSales(),
                imageChecker(entity.getSKU()),
                entity.getSubcategory(),
                entity.getBrand(),
                entity.getUnit());
    }

    private static boolean imageChecker(String SKU) {
        Path image = path.resolve(SKU);
        return Files.exists(image);
    }
}
