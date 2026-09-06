package ir.rahgozin.wallet.application.wallet;

import ir.rahgozin.wallet.application.common.Message;
import ir.rahgozin.wallet.application.common.MessageRepository;
import ir.rahgozin.wallet.application.common.MessageStatus;
import ir.rahgozin.wallet.application.common.NotificationService;
import ir.rahgozin.wallet.application.common.exception.BusinessException;
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
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import static ir.rahgozin.wallet.application.common.exception.BusinessError.*;

@Log4j2
@Service
@AllArgsConstructor
public class WalletService {
    private final AccountRepository accountRepository;
    private final CustomerService customerService;
    private final TransactionRepository transactionRepository;
    private final RedissonClient redissonClient;
    private final MessageRepository messageRepository;
    private final MessageSource messageSource;
    private final DebitTransactionService debitTransactionService;
    private final RateLimitService rateLimitService;


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
        Transaction existing = transactionRepository.findByRequestId(command.getRequestId());
        if (existing != null) {
            return new CreditAccountDTO(
                    existing.getAccount().getNumber(),
                    existing.getAmount()
            );
        }

        Account account = findAccount(command.getAccountNumber());
        if(!account.getOwner().isVerified() || account.getOwner().isDeleted()){
            throw new BusinessException(INVALID_CUSTOMER_STATUS);
        }

        Transaction transaction = new Transaction(
                command.getRequestId(),
                account,
                Direction.CREDIT,
                command.getAmount());
        transactionRepository.save(transaction);

        messageRepository.save(
                new Message(
                        getMessage("DEPOSIT_MESSAGE", account.getNumber(), command.getAmount().toPlainString()),
                        MessageStatus.PENDING,
                        command.getRequestId()
                )
        );
        return new CreditAccountDTO(
                account.getNumber(),
                command.getAmount());
    }

    public DebitAccountDTO debitAccount(DebitAccountCommand command) {
        RLock lock = redissonClient.getLock("debit-lock:" + command.getAccountNumber());
        boolean locked = false;
        try {
            locked = lock.tryLock(5, 30, TimeUnit.SECONDS);
            if (!locked) {
                throw new BusinessException(ACCOUNT_DEBIT_IN_PROGRESS);
            }
            return debitTransactionService.debitAccount(command);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new BusinessException(KEEF_DEFAULT_ERROR);
        } finally {
            if (locked && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    private Account findAccount(String accountNumber) {
        Account account = accountRepository.findByNumber(accountNumber);
        if (account == null) {
            throw new BusinessException(ACCOUNT_NOT_FOUND);
        }
        return account;
    }

    public List<AccountBalanceDTO> accountBalance(AccountBalanceQuery query) {
        rateLimitService.checkBalanceRateLimit(query.getCustomerId());
        List<AccountBalanceDTO> balances = accountRepository.findBalancesByCustomerId(query.getCustomerId());
        if (CollectionUtils.isEmpty(balances)) {
            throw new BusinessException(ACCOUNT_NOT_FOUND);
        }
        return balances;
    }

    public Page<TransactionDTO> listTransactions(TransactionQuery query, Pageable pageable) {
        Page<Transaction> transactions = transactionRepository.findAll(TransactionSpecification.filter(query), pageable);
        return transactions.map(this::toDTO);
    }

    private TransactionDTO toDTO(Transaction transaction) {
        return new TransactionDTO(
                transaction.getRequestId(),
                transaction.getAccount().getNumber(),
                transaction.getDirection(),
                transaction.getAmount()
        );
    }

    private String getMessage(String key, String... args) {
        return messageSource.getMessage(key, args, Locale.of("fa"));
    }
}
