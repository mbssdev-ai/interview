package ir.rahgozin.wallet.infrastructure.api.resources;

import lombok.Data;

@Data
public class CreateAccountRequest {
    private String nationalCode;
    private String firstName;
    private String lastName;
    private String mobile;
    private String type;
}
