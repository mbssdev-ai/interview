package ir.rahgozin.wallet.application.wallet.query;

import ir.rahgozin.wallet.application.wallet.Direction;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransactionQuery {
    private Long customerId;
    private BigDecimal fromAmount;
    private BigDecimal toAmount;
    private Direction direction;
}
