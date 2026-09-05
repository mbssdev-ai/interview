package ir.rahgozin.wallet.application.wallet;

import ir.rahgozin.wallet.application.common.NotificationService;
import ir.rahgozin.wallet.application.customer.Customer;
import ir.rahgozin.wallet.application.customer.CustomerService;
import ir.rahgozin.wallet.application.wallet.Account.AccountType;
import ir.rahgozin.wallet.application.wallet.command.CreateAccountCommand;
import ir.rahgozin.wallet.application.wallet.command.CreditAccountCommand;
import ir.rahgozin.wallet.application.wallet.command.DebitAccountCommand;
import ir.rahgozin.wallet.application.wallet.dto.AccountBalanceDTO;
import ir.rahgozin.wallet.application.wallet.dto.CreditAccountDTO;
import ir.rahgozin.wallet.application.wallet.dto.DebitAccountDTO;
import ir.rahgozin.wallet.application.wallet.dto.TransactionDTO;
import ir.rahgozin.wallet.application.wallet.query.AccountBalanceQuery;
import ir.rahgozin.wallet.application.wallet.query.TransactionQuery;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Log4j2
@Service
@AllArgsConstructor
public class WalletService {
    private final AccountRepository accountRepository;
    private final NotificationService notificationService;
    private final CustomerService customerService;

    @Transactional
    public String createAccount(CreateAccountCommand command) {
        Customer customer = createOrFindCustomer(command);
        Account account = new Account();
        account.setOwner(customer);
        account.setType(AccountType.valueOf(command.getType()));
        accountRepository.save(account);
        return account.getNumber();
    }

    private Customer createOrFindCustomer(CreateAccountCommand command) {
        boolean isExist = customerService.existsByNationalCode(command.getNationalCode());
        if (isExist) {
            return customerService.findByNationalCode(command.getNationalCode());
        }
        Customer customer = new Customer();
        customer.setMobile(command.getMobile());
        customer.setFirstName(command.getFirstName());
        customer.setLastName(command.getLastName());
        customer.setNationalCode(command.getNationalCode());
        return customerService.create(customer);
    }

    @Transactional
    public CreditAccountDTO creditAccount(CreditAccountCommand command) {

        Account account = accountRepository.findByNumber(command.getAccountNumber());

        Transaction transaction = new Transaction(
                command.getRequestId(),
                account,
                "credit",
                command.getAmount());

        Set<Transaction> transactions = account.getTransactions();
        transactions.add(transaction);
        account.setTransactions(transactions);

        notificationService.send(account.getOwner().getMobile(), "deposit request of " + command.getAmount() + " has been successfully processed.");

        accountRepository.save(account);

        return new CreditAccountDTO(
                account.getNumber(),
                transaction.getAmount());
    }

    @Transactional
    public DebitAccountDTO debitAccount(DebitAccountCommand command) {
        Account account = accountRepository.findById(command.getAccountNumber()).orElseThrow();

        Transaction transaction = new Transaction(
                command.getRequestId(),
                account,
                "debit",
                command.getAmount()
        );

        Set<Transaction> transactions = account.getTransactions();
        transactions.add(transaction);
        account.setTransactions(transactions);

        notificationService.send(account.getOwner().getMobile(), "withdrawal request of " + command.getAmount() + " has been successfully processed.");

        accountRepository.save(account);

        return new DebitAccountDTO(account.getNumber(), command.getAmount());
    }

    public List<AccountBalanceDTO> accountBalance(AccountBalanceQuery query) {
        return accountRepository.findAll()
                .stream()
                .filter(account -> account.getOwner().getId().equals(query.getCustomerId()))
                .map(account -> {
                    AccountBalanceDTO accountBalanceDTO = new AccountBalanceDTO();
                    accountBalanceDTO.setAmount(account.getTransactions().stream().mapToDouble(Transaction::getAmount).sum());
                    accountBalanceDTO.setNumber(account.getId());
                    return accountBalanceDTO;
                }).collect(Collectors.toList());
    }

    public List<TransactionDTO> listTransactions(TransactionQuery query) {
        // TODO implement the logic
        return Collections.emptyList();
    }
}
