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
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<?> creditAccount(@RequestBody @Valid CreditAccountRequest request) {

        CreditAccountCommand creditAccountCommand = conversionService.convert(request, CreditAccountCommand.class);

        return ResponseEntity.ok(walletService.creditAccount(creditAccountCommand));
    }

    @PostMapping(path = "/debit")
    public ResponseEntity<?> debitAccount(@RequestBody @Valid DebitAccountRequest request) {
        DebitAccountCommand debitAccountCommand = conversionService.convert(request, DebitAccountCommand.class);
        return ResponseEntity.ok(walletService.debitAccount(debitAccountCommand));
    }

    @GetMapping(path = "/balance")
    public ResponseEntity<?> accountBalance(@RequestParam("customerId") String customerId) {
        AccountBalanceQuery query = new AccountBalanceQuery();
        query.setCustomerId(Long.valueOf(customerId));
        return ResponseEntity.ok(walletService.accountBalance(query));
    }

    @GetMapping(path = "/transactions")
    public ResponseEntity<?> transactionList(@ModelAttribute TransactionQuery query,  Pageable pageable) {
        return ResponseEntity.ok(walletService.listTransactions(query, pageable));
    }
}
