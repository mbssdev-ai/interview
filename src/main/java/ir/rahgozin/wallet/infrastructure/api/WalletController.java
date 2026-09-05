package ir.rahgozin.wallet.infrastructure.api;

import ir.rahgozin.wallet.application.wallet.WalletService;
import ir.rahgozin.wallet.application.wallet.command.CreateAccountCommand;
import ir.rahgozin.wallet.application.wallet.command.CreditAccountCommand;
import ir.rahgozin.wallet.application.wallet.command.DebitAccountCommand;
import ir.rahgozin.wallet.application.wallet.query.AccountBalanceQuery;
import ir.rahgozin.wallet.application.wallet.query.TransactionQuery;
import ir.rahgozin.wallet.infrastructure.api.resources.CreateAccountRequest;
import ir.rahgozin.wallet.infrastructure.api.resources.CreditAccountRequest;
import ir.rahgozin.wallet.infrastructure.api.resources.DebitAccountRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Log4j2
@RestController
@RequiredArgsConstructor
public class WalletController {
    private final static Map<String, Integer> requestCounter = new HashMap<>();
    private final int limitation = 5;

    private final WalletService walletService;
    private final ConversionService conversionService;

    @PostMapping(path = "/create")
    public ResponseEntity<?> createAccount(@RequestBody @Valid CreateAccountRequest request) {

        CreateAccountCommand createAccountCommand = conversionService.convert(request, CreateAccountCommand.class);

        return ResponseEntity.ok(walletService.createAccount(createAccountCommand));
    }

    @PostMapping(path = "/credit")
    public ResponseEntity<?> creditAccount(@RequestBody CreditAccountRequest request) {

        CreditAccountCommand creditAccountCommand = conversionService.convert(request, CreditAccountCommand.class);

        return ResponseEntity.ok(walletService.creditAccount(creditAccountCommand));
    }

    @PostMapping(path = "/debit")
    public ResponseEntity<?> debitAccount(@RequestBody DebitAccountRequest request) {
        DebitAccountCommand debitAccountCommand = conversionService.convert(request, DebitAccountCommand.class);
        return ResponseEntity.ok(walletService.debitAccount(debitAccountCommand));
    }

    @PostMapping(path = "/balance")
    public ResponseEntity<?> accountBalance(@RequestParam("customerId") String customerId) {

        if (requestCounter.containsKey(customerId)) {
            Integer counter = requestCounter.get(customerId);
            if (counter > limitation) {
                return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
            }
            requestCounter.replace(customerId, counter + 1);
        } else {
            requestCounter.put(customerId, 1);
        }

        if (customerId.isBlank()) {
            throw new RuntimeException("customerId must not be null or empty");
        }

        AccountBalanceQuery query = new AccountBalanceQuery();
        query.setCustomerId(Long.valueOf(customerId));
        return ResponseEntity.ok(walletService.accountBalance(query));
    }

    @PostMapping(path = "/transactions")
    public ResponseEntity<?> transactionList(@RequestParam("customerId") String customerId) {
        TransactionQuery query = new TransactionQuery();
        query.setCustomerId(Long.valueOf(customerId));
        return ResponseEntity.ok(walletService.listTransactions(query));
    }
}
