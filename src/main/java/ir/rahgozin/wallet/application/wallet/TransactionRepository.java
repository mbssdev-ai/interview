package ir.rahgozin.wallet.application.wallet;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long>, JpaSpecificationExecutor<Transaction> {

    @Query("""
                SELECT 
                    COALESCE(
                        SUM(
                            CASE 
                                WHEN t.type = 'CREDIT'
                                THEN t.amount
                                ELSE -t.amount
                            END
                        ),0
                    )
                FROM Transaction t
                WHERE t.accountNumber = :accountNumber
            """)
    BigDecimal calculateBalance(String accountNumber);

    Transaction findByRequestId(String requestId);

}
