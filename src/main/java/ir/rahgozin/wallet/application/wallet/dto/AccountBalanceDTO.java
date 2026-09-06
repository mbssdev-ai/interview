package ir.rahgozin.wallet.application.wallet.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AccountBalanceDTO {
    private String number;
    private BigDecimal amount;
}
