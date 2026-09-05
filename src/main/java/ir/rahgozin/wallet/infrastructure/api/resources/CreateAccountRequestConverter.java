package ir.rahgozin.wallet.infrastructure.api.resources;

import ir.rahgozin.wallet.application.wallet.command.CreateAccountCommand;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class CreateAccountRequestConverter implements Converter<CreateAccountRequest, CreateAccountCommand> {
    @Override
    public CreateAccountCommand convert(CreateAccountRequest source) {
        CreateAccountCommand createAccountCommand = new CreateAccountCommand();
        createAccountCommand.setNationalCode(source.getNationalCode());
        createAccountCommand.setType(source.getType());
        createAccountCommand.setFirstName(source.getFirstName());
        createAccountCommand.setLastName(source.getLastName());
        createAccountCommand.setMobile(source.getMobile());
        return createAccountCommand;
    }
}
