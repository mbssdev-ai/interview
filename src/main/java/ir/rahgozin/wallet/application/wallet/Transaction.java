package ir.rahgozin.wallet.application.wallet;

import ir.rahgozin.wallet.application.common.AbstractEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Table
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Transaction extends AbstractEntity {
    @Column(unique = true)
    private String requestId;
    @ManyToOne(optional = false)
    @JoinColumn(name = "account_id")
    private Account account;
    private String direction;
    private Double amount;

}
