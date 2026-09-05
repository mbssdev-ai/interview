package ir.rahgozin.wallet.infrastructure.api.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateAccountRequest {
    @NotBlank(message = "nationalCode is required")
    @Pattern(
            regexp = "\\d{10}",
            message = "nationalCode must contain exactly 10 digits"
    )
    @Schema(example = "0011234587")
    private String nationalCode;

    @NotBlank(message = "firstName is required")
    @Size(
            min = 2,
            max = 50,
            message = "firstName must be between 2 and 50 characters"
    )
    @Schema(example = "majid")
    private String firstName;

    @NotBlank(message = "lastName is required")
    @Size(
            min = 2,
            max = 50,
            message = "lastName must be between 2 and 50 characters"
    )
    @Schema(example = "barzegar")
    private String lastName;

    @NotBlank(message = "mobile is required")
    @Pattern(
            regexp = "^09\\d{9}$",
            message = "mobile must be a valid Iranian mobile number"
    )
    @Schema(example = "09195411265")
    private String mobile;

    @NotBlank(message = "type is required")
    @Pattern(
            regexp = "CHECKING|SAVING",
            message = "type must be either CHECKING or SAVING"
    )
    @Schema(example = "CHECKING")
    private String type;
}

