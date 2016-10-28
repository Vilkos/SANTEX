package com.santex.service.implementation;


import com.santex.dto.SearchCriteria;
import com.santex.dto.SearchCriteriaAdminProduct;
import com.santex.entity.*;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

final class BrandSpecifications {

    public BrandSpecifications() {
    }

    static Specification<Brand> byCriteriaCustomer(SearchCriteria criteria) {
        return (root, cq, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            Join<Brand, Product> product_joined = root.join(Brand_.productList);

            cb.isTrue(product_joined.get(Product_.availability));
            cq.orderBy(cb.asc(root.get(Brand_.brandName))).distinct(true);

            if (!criteria.getSearch().isEmpty()) {
                predicates.add(byProductSearchTerm(product_joined, criteria.getSearch()).toPredicate(root, cq, cb));
            }
            if (criteria.getCatId() > 0) {
                predicates.add(byProductCatId(product_joined, criteria.getCatId()).toPredicate(root, cq, cb));
            }
            if (criteria.getSubId() > 0) {
                predicates.add(byProductSubId(product_joined, criteria.getSubId()).toPredicate(root, cq, cb));
            }
            return andTogether(predicates, cb);
        };
    }

    static Specification<Brand> byCriteriaAdmin(SearchCriteriaAdminProduct criteria) {
        return (root, cq, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            Join<Brand, Product> product_joined = root.join(Brand_.productList);

            cq.distinct(true);
            cq.orderBy(cb.asc(root.get(Brand_.brandName)));

            if (!criteria.getSearch().isEmpty()) {
                predicates.add(byProductSearchTerm(product_joined, criteria.getSearch()).toPredicate(root, cq, cb));
            }
            if (criteria.getCatId() > 0) {
                predicates.add(byProductCatId(product_joined, criteria.getCatId()).toPredicate(root, cq, cb));
            }
            if (criteria.getSubId() > 0) {
                predicates.add(byProductSubId(product_joined, criteria.getSubId()).toPredicate(root, cq, cb));
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

    private static Specification<Brand> byProductCatId(Join<Brand, Product> product_joined, int catId) {
        return (root, cq, cb) -> cb.equal(product_joined.get(Product_.subcategory).get(Subcategory_.category).get(Category_.id), catId);
    }

    private static Specification<Brand> byProductSubId(Join<Brand, Product> product_joined, int subId) {
        return (root, cq, cb) -> cb.equal(product_joined.get(Product_.subcategory).get(Subcategory_.id), subId);
    }

    private static Specification<Brand> byProductAvailability(Join<Brand, Product> product_joined, Boolean availability) {
        return (root, cq, cb) -> cb.equal(product_joined.get(Product_.availability), availability);
    }
    private static Specification<Brand> byProductPriceVisibility(Join<Brand, Product> product_joined, Boolean priceVisibility) {
        return (root, cq, cb) -> cb.equal(product_joined.get(Product_.priceVisibility), priceVisibility);
    }

    private static Specification<Brand> byProductDiscountPrice(Join<Brand, Product> product_joined) {
        return (root, cq, cb) -> cb.gt(product_joined.get(Product_.discountPrice), 0);
    }

    private static Specification<Brand> byProductNormalPrice(Join<Brand, Product> product_joined) {
        return (root, cq, cb) -> cb.equal(product_joined.get(Product_.discountPrice), 0);
    }

    private static Specification<Brand> byProductCurrency(Join<Brand, Product> product_joined, Currency currency) {
        return (root, cq, cb) -> cb.equal(product_joined.get(Product_.currency), currency);
    }

    private static Specification<Brand> byProductSearchTerm(Join<Brand, Product> product_joined, String searchTerm) {
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
