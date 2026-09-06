package ir.rahgozin.wallet.application.wallet.dto;

import lombok.AllArgsConstructor;

import java.math.BigDecimal;


@AllArgsConstructor
public class CreditAccountDTO {
    private String accountNumber;
    private BigDecimal amount;
}
