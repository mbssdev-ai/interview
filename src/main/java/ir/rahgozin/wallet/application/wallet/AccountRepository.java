package ir.rahgozin.wallet.application.wallet;

import ir.rahgozin.wallet.application.wallet.dto.AccountBalanceDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    Account findByNumber(String number);

    @Query("""
                select a.number
                from Account a
                where a.owner.id = :customerId
            """)
    List<String> findNumbersByOwnerId(Long customerId);


    @Query("""
                select new com.example.dto.AccountBalanceDTO(
                    a.number,
                    coalesce(
                        sum(
                            case
                                when t.type = 'CREDIT'
                                then t.amount
                                else -t.amount
                            end
                        ),
                        0
                    )
                )
                from Account a
                left join Transaction t
                    on t.account.id = a.id
                where a.owner.id = :customerId
                group by a.number
            """)
    List<AccountBalanceDTO> findBalancesByCustomerId(Long customerId);
}
