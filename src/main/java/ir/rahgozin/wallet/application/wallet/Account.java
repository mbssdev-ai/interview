package ir.rahgozin.wallet.application.wallet;

import ir.rahgozin.wallet.application.common.AbstractEntity;
import ir.rahgozin.wallet.application.customer.Customer;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;
import java.util.UUID;

@Data
@Table
@Entity
public class Account extends AbstractEntity {
    private String number;

    private AccountType type;

    @OneToMany(mappedBy = "account")
    private Set<Transaction> transactions;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "owner_id")
    private Customer owner;

    public Account() {
        number = UUID.randomUUID().toString();
    }

    String issueDate;

    enum AccountType {
        CHECKING, SAVING
    }
}
