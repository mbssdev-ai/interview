package ir.rahgozin.wallet.infrastructure.api.resources;

import lombok.Data;


@Data
public class CreditAccountRequest {
    private String accountNumber;
    private Double amount;
}
