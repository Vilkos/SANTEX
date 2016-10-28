package com.santex.service.implementation;

import com.santex.dto.SearchCriteria;
import com.santex.dto.SearchCriteriaAdminProduct;
import com.santex.entity.*;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

final class ProductSpecifications {

    private ProductSpecifications() {
    }

    static Specification<Product> byCriteriaCustomer(SearchCriteria criteria) {
        return (root, cq, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            predicates.add(availabilityTrue().toPredicate(root, cq, cb));
            if (!criteria.getSearch().isEmpty()) {
                predicates.add(bySearchTerm(criteria.getSearch()).toPredicate(root, cq, cb));
            }
            if (criteria.getBrandId() > 0) {
                predicates.add(byBrand(criteria.getBrandId()).toPredicate(root, cq, cb));
            }
            if (criteria.getCatId() > 0) {
                predicates.add(byCategory(criteria.getCatId()).toPredicate(root, cq, cb));
            }
            if (criteria.getSubId() > 0) {
                predicates.add(bySubcategory(criteria.getSubId()).toPredicate(root, cq, cb));
            }

            Subquery<Integer> subquery = cq.subquery(Integer.class);
            Root<CurrencyRate> currency_root = subquery.from(CurrencyRate.class);

            switch (criteria.getSrt()) {
                case name_asc:
                    cq.orderBy(cb.asc(root.get(Product_.productName)));
                    break;
                case name_desc:
                    cq.orderBy(cb.desc(root.get(Product_.productName)));
                    break;
                case price_asc:
                    cq.orderBy(cb.asc(cb.selectCase()
                            .when(cb.equal(root.get(Product_.currency), Currency.EUR), cb.prod(root.get(Product_.price),
                                    subquery.select(currency_root.get(CurrencyRate_.rate)).where(cb.equal(currency_root.get(CurrencyRate_.currency), Currency.EUR))))
                            .when(cb.equal(root.get(Product_.currency), Currency.USD), cb.prod(root.get(Product_.price),
                                    subquery.select(currency_root.get(CurrencyRate_.rate)).where(cb.equal(currency_root.get(CurrencyRate_.currency), Currency.USD))))
                            .otherwise(cb.prod(root.get(Product_.price), 100))));
                    break;
                case price_desc:
                    cq.orderBy(cb.desc(cb.selectCase()
                            .when(cb.equal(root.get(Product_.currency), Currency.EUR), cb.prod(root.get(Product_.price),
                                    subquery.select(currency_root.get(CurrencyRate_.rate)).where(cb.equal(currency_root.get(CurrencyRate_.currency), Currency.EUR))))
                            .when(cb.equal(root.get(Product_.currency), Currency.USD), cb.prod(root.get(Product_.price),
                                    subquery.select(currency_root.get(CurrencyRate_.rate)).where(cb.equal(currency_root.get(CurrencyRate_.currency), Currency.USD))))
                            .otherwise(cb.prod(root.get(Product_.price), 100))));
                    break;
                case disc_price:
                    predicates.add(byDiscountPrice().toPredicate(root, cq, cb));
                    cq.orderBy(cb.desc(cb.selectCase()
                            .when(cb.equal(root.get(Product_.currency), Currency.EUR), cb.prod(root.get(Product_.price),
                                    subquery.select(currency_root.get(CurrencyRate_.rate)).where(cb.equal(currency_root.get(CurrencyRate_.currency), Currency.EUR))))
                            .when(cb.equal(root.get(Product_.currency), Currency.USD), cb.prod(root.get(Product_.price),
                                    subquery.select(currency_root.get(CurrencyRate_.rate)).where(cb.equal(currency_root.get(CurrencyRate_.currency), Currency.USD))))
                            .otherwise(cb.prod(root.get(Product_.price), 100))));
                default:
                    cq.orderBy(cb.asc(root.get(Product_.productName)));
                    break;
            }
            return andTogether(predicates, cb);
        };
    }

    static Specification<Product> byCriteriaAdmin(SearchCriteriaAdminProduct criteria) {
        return (root, cq, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            predicates.add(bySearchTerm(criteria.getSearch()).toPredicate(root, cq, cb));
            if (criteria.getAvail() != null) {
                predicates.add(byAvailability(criteria.getAvail()).toPredicate(root, cq, cb));
            }
            if (criteria.getPr_vis() != null) {
                predicates.add(byPriceVisibility(criteria.getPr_vis()).toPredicate(root, cq, cb));
            }
            if (criteria.getDisc_pr() != null) {
                if (criteria.getDisc_pr()) {
                    predicates.add(byDiscountPrice().toPredicate(root, cq, cb));
                } else {
                    predicates.add(byNormalPrice().toPredicate(root, cq, cb));
                }
            }
            if (criteria.getCur() != null) {
                predicates.add(byCurrency(criteria.getCur()).toPredicate(root, cq, cb));
            }
            if (criteria.getBrandId() > 0) {
                predicates.add(byBrand(criteria.getBrandId()).toPredicate(root, cq, cb));
            }
            if (criteria.getCatId() > 0) {
                predicates.add(byCategory(criteria.getCatId()).toPredicate(root, cq, cb));
            }
            if (criteria.getSubId() > 0) {
                predicates.add(bySubcategory(criteria.getSubId()).toPredicate(root, cq, cb));
            }

            switch (criteria.getSrt()) {
                case name_asc:
                    cq.orderBy(cb.asc(root.get(Product_.productName)));
                    break;
                case name_desc:
                    cq.orderBy(cb.desc(root.get(Product_.productName)));
                    break;
                case sku_asc:
                    cq.orderBy(cb.asc(root.get(Product_.SKU)));
                    break;
                case sku_desc:
                    cq.orderBy(cb.desc(root.get(Product_.SKU)));
                    break;
                case price_asc:
                    cq.orderBy(cb.asc(root.get(Product_.price)));
                    break;
                case price_desc:
                    cq.orderBy(cb.desc(root.get(Product_.price)));
                    break;
                case sales_asc:
                    cq.orderBy(cb.asc(root.get(Product_.quantityOfSales)));
                    break;
                case sales_desc:
                    cq.orderBy(cb.desc(root.get(Product_.quantityOfSales)));
                    break;
                default:
                    cq.orderBy(cb.asc(root.get(Product_.productName)));
                    break;
            }

            return andTogether(predicates, cb);
        };
    }

    private static Specification<Product> byCategory(Integer catId) {
        return (root, cq, cb) -> cb.equal(root.get(Product_.subcategory).get(Subcategory_.category).get(Category_.id), catId);
    }

    private static Specification<Product> bySubcategory(Integer subId) {
        return (root, cq, cb) -> cb.equal(root.get(Product_.subcategory).get(Subcategory_.id), subId);
    }

    private static Specification<Product> byBrand(Integer brandId) {
        return (root, cq, cb) -> cb.equal(root.get(Product_.brand).get(Brand_.id), brandId);
    }

    private static Specification<Product> availabilityTrue() {
        return (root, cq, cb) -> cb.isTrue(root.get(Product_.availability));
    }

    private static Specification<Product> byAvailability(Boolean availability) {
        return (root, cq, cb) -> cb.equal(root.get(Product_.availability), availability);
    }

    private static Specification<Product> byPriceVisibility(Boolean priceVisibility) {
        return (root, cq, cb) -> cb.equal(root.get(Product_.priceVisibility), priceVisibility);
    }

    private static Specification<Product> byDiscountPrice() {
        return (root, cq, cb) -> cb.gt(root.get(Product_.discountPrice), 0);
    }

    private static Specification<Product> byNormalPrice() {
        return (root, cq, cb) -> cb.equal(root.get(Product_.discountPrice), 0);
    }

    private static Specification<Product> byCurrency(Currency currency) {
        return (root, cq, cb) -> cb.equal(root.get(Product_.currency), currency);
    }

    private static Specification<Product> bySearchTerm(String searchTerm) {
        String containsLikePattern = getContainsLikePattern(searchTerm);
        return (root, cq, cb) -> cb.or(
                cb.like(cb.lower(root.get(Product_.productName)), containsLikePattern),
                cb.like(cb.lower(root.get(Product_.SKU)), containsLikePattern));
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
