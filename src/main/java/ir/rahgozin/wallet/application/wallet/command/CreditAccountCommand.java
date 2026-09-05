package ir.rahgozin.wallet.application.wallet.command;

import lombok.Data;

@Data
public class CreditAccountCommand {
    private String requestId;
    private String accountNumber;
    private Double amount;

}
