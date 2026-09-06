package ir.rahgozin.wallet.application.wallet.command;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreditAccountCommand {
    private String requestId;
    private String accountNumber;
    private BigDecimal amount;
}
