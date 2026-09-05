package ir.rahgozin.wallet.application.wallet.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TransactionDTO {
    private Long account;
    private Double amount;
}
