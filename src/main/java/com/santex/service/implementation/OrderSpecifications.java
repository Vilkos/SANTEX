package com.santex.service.implementation;

import com.santex.dto.SearchCriteriaAdmin;
import com.santex.entity.Customer_;
import com.santex.entity.Order_;

import org.springframework.data.jpa.domain.Specification;
import com.santex.entity.Order;

import javax.persistence.criteria.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

final class OrderSpecifications {

    public OrderSpecifications() {
    }

    static Specification<Order> byOrderAdmin(SearchCriteriaAdmin orderCriteria) {
        return (root, cq, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            cq.orderBy(cb.desc(root.get(Order_.timeOfOrder)));


            if (!orderCriteria.getSearch().isEmpty()) {
                predicates.add(bySearchTerm(orderCriteria.getSearch()).toPredicate(root, cq, cb));
            }
            if (orderCriteria.getStartDateTime() != null && orderCriteria.getEndDateTime() != null) {
                predicates.add(betweenDates(orderCriteria.getStartDateTime(), orderCriteria.getEndDateTime()).toPredicate(root, cq, cb));
            }
            if (orderCriteria.getStartDateTime() != null && orderCriteria.getEndDateTime() == null) {
                predicates.add(betweenDates(orderCriteria.getStartDateTime(), LocalDateTime.now()).toPredicate(root, cq, cb));
            }
            if (orderCriteria.getStartDateTime() == null && orderCriteria.getEndDateTime() != null) {
                predicates.add(fromGreatestToGiven(orderCriteria.getEndDateTime()).toPredicate(root, cq, cb));
            }

            return andTogether(predicates, cb);
        };
    }

    private static Specification<Order> betweenDates(LocalDateTime startDate, LocalDateTime endDate) {
        return (root, cq, cb) -> cb.between(root.get(Order_.timeOfOrder), startDate, endDate);
    }

    private static Specification<Order> fromGreatestToGiven(LocalDateTime endDate) {
        return (root, cq, cb) -> cb.and(
                cb.greaterThan(root.get(Order_.timeOfOrder), cb.greatest(root.get(Order_.timeOfOrder))),
                cb.lessThan(root.get(Order_.timeOfOrder), endDate));
    }

    private static Specification<Order> bySearchTerm(String searchTerm) {
        String containsLikePattern = getContainsLikePattern(searchTerm);
        return (root, cq, cb) -> cb.or(
                cb.like(cb.lower(root.get(Order_.street)), containsLikePattern),
                cb.like(cb.lower(root.get(Order_.message)), containsLikePattern),
                cb.like(cb.lower(root.get(Order_.city)), containsLikePattern),
                cb.like(cb.lower(root.get(Order_.customer).get(Customer_.firstName)), containsLikePattern),
                cb.like(cb.lower(root.get(Order_.customer).get(Customer_.secondName)), containsLikePattern),
                cb.like(cb.lower(root.get(Order_.customer).get(Customer_.email)), containsLikePattern));
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
