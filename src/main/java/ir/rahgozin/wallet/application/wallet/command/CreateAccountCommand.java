package ir.rahgozin.wallet.application.wallet.command;

import lombok.Data;

@Data
public class CreateAccountCommand {
    private String nationalCode;
    private String firstName;
    private String lastName;
    private String mobile;
    private String type;
}
