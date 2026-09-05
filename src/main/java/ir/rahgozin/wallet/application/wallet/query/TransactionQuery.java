package ir.rahgozin.wallet.application.wallet.query;

import lombok.Data;

@Data
public class TransactionQuery {
    private Long customerId;
    private Double fromAmount;
    private Double toAmount;
    private String direction;
}
