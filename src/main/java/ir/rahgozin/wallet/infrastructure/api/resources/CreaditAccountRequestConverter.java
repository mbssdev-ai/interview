package ir.rahgozin.wallet.infrastructure.api.resources;

import ir.rahgozin.wallet.application.wallet.command.CreditAccountCommand;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class CreaditAccountRequestConverter implements Converter<CreditAccountRequest, CreditAccountCommand> {
    @Override
    public CreditAccountCommand convert(CreditAccountRequest source) {
        CreditAccountCommand creditAccountCommand = new CreditAccountCommand();
        creditAccountCommand.setAccountNumber(source.getAccountNumber());
        creditAccountCommand.setAmount(source.getAmount());
        return creditAccountCommand;
    }
}
