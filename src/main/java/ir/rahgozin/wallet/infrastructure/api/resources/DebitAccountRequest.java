package ir.rahgozin.wallet.infrastructure.api.resources;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class DebitAccountRequest {
    @NotBlank(message = "Request id is required")
    private String requestId;

    @NotBlank(message = "Account number is required")
    private String accountNumber;

    @NotNull(message = "Amount is required")
    @DecimalMin(
            value = "0.01",
            message = "Amount must be greater than zero"
    )
    @Digits(
            integer = 15,
            fraction = 2,
            message = "Invalid amount format"
    )
    private BigDecimal amount;
}
