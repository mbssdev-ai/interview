package ir.rahgozin.wallet.infrastructure.api.resources;

import lombok.Data;

@Data
public class DebitAccountRequest {
    private String requestId;
    private Long accountNumber;
    private Double amount;
}
