package ir.rahgozin.wallet.application.wallet.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class DebitAccountDTO {
    private String account;
    private BigDecimal amount;
}
