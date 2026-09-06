package ir.rahgozin.wallet.application.wallet.dto;

import ir.rahgozin.wallet.application.wallet.Direction;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class TransactionDTO {
    private String requestId;
    private String account;
    private Direction direction;
    private BigDecimal amount;
}
