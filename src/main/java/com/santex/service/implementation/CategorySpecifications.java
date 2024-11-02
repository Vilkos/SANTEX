package com.santex.service.implementation;


import com.santex.dto.SearchCriteriaAdminProduct;
import com.santex.entity.*;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

final class CategorySpecifications {

    public CategorySpecifications() {
    }

    static Specification<Category> byCriteriaAdmin(SearchCriteriaAdminProduct criteria) {
        return (root, cq, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            Join<Category, Subcategory> subcategory_joined = root.join(Category_.subcategoryList);
            Join<Subcategory, Product> product_joined = subcategory_joined.join(Subcategory_.productList);
            cq.orderBy(cb.asc(root.get(Category_.categoryName))).distinct(true);

            if (criteria.getBrandId() > 0) {
                predicates.add(byProductBrand(product_joined, criteria.getBrandId()).toPredicate(root, cq, cb));
            }
            if (!criteria.getSearch().isEmpty()) {
                predicates.add(byProductSearchTerm(product_joined, criteria.getSearch()).toPredicate(root, cq, cb));
            }
            if (criteria.getAvail() != null) {
                predicates.add(byProductAvailability(product_joined, criteria.getAvail()).toPredicate(root, cq, cb));
            }
            if (criteria.getPr_vis() != null) {
                predicates.add(byProductPriceVisibility(product_joined, criteria.getPr_vis()).toPredicate(root, cq, cb));
            }
            if (criteria.getDisc_pr() != null) {
                if (criteria.getDisc_pr()) {
                    predicates.add(byProductDiscountPrice(product_joined).toPredicate(root, cq, cb));
                } else {
                    predicates.add(byProductNormalPrice(product_joined).toPredicate(root, cq, cb));
                }
            }
            if (criteria.getCur() != null) {
                predicates.add(byProductCurrency(product_joined, criteria.getCur()).toPredicate(root, cq, cb));
            }

            return andTogether(predicates, cb);
        };
    }

    private static Specification<Category> byProductBrand(Join<Subcategory, Product> product_joined, int brandId) {
        return (root, cq, cb) -> cb.equal(product_joined.get(Product_.brand).get(Brand_.id), brandId);
    }

    private static Specification<Category> byProductAvailability(Join<Subcategory, Product> product_joined, Boolean availability) {
        return (root, cq, cb) -> cb.equal(product_joined.get(Product_.availability), availability);
    }
    private static Specification<Category> byProductPriceVisibility(Join<Subcategory, Product> product_joined, Boolean priceVisibility) {
        return (root, cq, cb) -> cb.equal(product_joined.get(Product_.priceVisibility), priceVisibility);
    }

    private static Specification<Category> byProductDiscountPrice(Join<Subcategory, Product> product_joined) {
        return (root, cq, cb) -> cb.gt(product_joined.get(Product_.discountPrice), 0);
    }

    private static Specification<Category> byProductNormalPrice(Join<Subcategory, Product> product_joined) {
        return (root, cq, cb) -> cb.equal(product_joined.get(Product_.discountPrice), 0);
    }

    private static Specification<Category> byProductCurrency(Join<Subcategory, Product> product_joined, Currency currency) {
        return (root, cq, cb) -> cb.equal(product_joined.get(Product_.currency), currency);
    }

    private static Specification<Category> byProductSearchTerm(Join<Subcategory, Product> product_joined, String searchTerm) {
        String containsLikePattern = getContainsLikePattern(searchTerm);
        return (root, cq, cb) -> cb.or(
                cb.like(cb.lower(product_joined.get(Product_.productName)), containsLikePattern),
                cb.like(cb.lower(product_joined.get(Product_.SKU)), containsLikePattern));
    }

    private static Predicate andTogether(List<Predicate> predicates, CriteriaBuilder cb) {
        return cb.and(predicates.toArray(new Predicate[0]));
    }

    private static String getContainsLikePattern(String searchTerm) {
        if (searchTerm == null || searchTerm.isEmpty()) {
            return "%";
        } else {
            return "%" + searchTerm.toLowerCase() + "%";
        }
    }
}
