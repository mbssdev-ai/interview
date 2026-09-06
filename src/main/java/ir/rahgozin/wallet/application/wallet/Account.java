package ir.rahgozin.wallet.application.wallet;

import ir.rahgozin.wallet.application.common.AbstractEntity;
import ir.rahgozin.wallet.application.customer.Customer;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Table(
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_account_number",
                        columnNames = "number"
                )
        }
)
@Entity
public class Account extends AbstractEntity {
    @Column(name = "number", nullable = false)
    private String number;

    @Enumerated(EnumType.STRING)
    private AccountType type;

    @OneToMany(mappedBy = "account")
    private Set<Transaction> transactions = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private Customer owner;

    private LocalDate issueDate;

    public Account() {
        number = UUID.randomUUID().toString();
        issueDate = LocalDate.now();
    }

    enum AccountType {
        CHECKING, SAVING
    }
}
