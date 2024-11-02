package com.santex.service.implementation;

import com.santex.dto.SearchCriteriaAdmin;
import com.santex.entity.Customer;
import com.santex.entity.Customer_;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

final class CustomerSpecifications {

    public CustomerSpecifications() {
    }

    static Specification<Customer> byCustomerAdmin(SearchCriteriaAdmin customerCriteria) {
        return (root, cq, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            cq.orderBy(cb.desc(root.get(Customer_.dateOfRegistration)));


            if (!customerCriteria.getSearch().isEmpty()) {
                predicates.add(bySearchTerm(customerCriteria.getSearch()).toPredicate(root, cq, cb));
            }
            if (customerCriteria.getStartDateTime() != null && customerCriteria.getEndDateTime() != null) {
                predicates.add(betweenDates(customerCriteria.getStartDateTime(), customerCriteria.getEndDateTime()).toPredicate(root, cq, cb));
            }
            if (customerCriteria.getStartDateTime() != null && customerCriteria.getEndDateTime() == null) {
                predicates.add(betweenDates(customerCriteria.getStartDateTime(), LocalDateTime.now()).toPredicate(root, cq, cb));
            }
            if (customerCriteria.getStartDateTime() == null && customerCriteria.getEndDateTime() != null) {
                predicates.add(fromGreatestToGiven(customerCriteria.getEndDateTime()).toPredicate(root, cq, cb));
            }

            return andTogether(predicates, cb);
        };
    }

    private static Specification<Customer> betweenDates(LocalDateTime startDate, LocalDateTime endDate) {
        return (root, cq, cb) -> cb.between(root.get(Customer_.dateOfRegistration), startDate, endDate);
    }

    private static Specification<Customer> fromGreatestToGiven(LocalDateTime endDate) {
        return (root, cq, cb) -> cb.and(
                cb.greaterThan(root.get(Customer_.dateOfRegistration), cb.greatest(root.get(Customer_.dateOfRegistration))),
                cb.lessThan(root.get(Customer_.dateOfRegistration), endDate));
    }

    private static Specification<Customer> bySearchTerm(String searchTerm) {
        String containsLikePattern = getContainsLikePattern(searchTerm);
        return (root, cq, cb) -> cb.or(
                cb.like(cb.lower(root.get(Customer_.firstName)), containsLikePattern),
                cb.like(cb.lower(root.get(Customer_.secondName)), containsLikePattern),
                cb.like(cb.lower(root.get(Customer_.email)), containsLikePattern));
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
