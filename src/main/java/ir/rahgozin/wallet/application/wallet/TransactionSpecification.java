package ir.rahgozin.wallet.application.wallet;

import ir.rahgozin.wallet.application.wallet.query.TransactionQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class TransactionSpecification {
    public static Specification<Transaction> filter(TransactionQuery query) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if(query.getCustomerId() != null) {
                Join<Transaction, Account> account = root.join("account");
                predicates.add(
                        criteriaBuilder.equal(
                                account.get("owner").get("id"),
                                query.getCustomerId()
                        )
                );
            }

            if(query.getFromAmount() != null) {
                predicates.add(
                        criteriaBuilder.greaterThanOrEqualTo(
                                root.get("amount"),
                                query.getFromAmount()
                        )
                );
            }

            if(query.getToAmount() != null) {
                predicates.add(
                        criteriaBuilder.lessThanOrEqualTo(
                                root.get("amount"),
                                query.getToAmount()
                        )
                );
            }

            if(query.getDirection() != null) {
                predicates.add(
                        criteriaBuilder.equal(
                                root.get("direction"),
                                query.getDirection()
                        )
                );
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
