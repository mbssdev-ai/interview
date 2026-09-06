package ir.rahgozin.wallet.application.wallet;

import ir.rahgozin.wallet.application.common.Message;
import ir.rahgozin.wallet.application.common.MessageRepository;
import ir.rahgozin.wallet.application.common.MessageStatus;
import ir.rahgozin.wallet.application.common.exception.BusinessException;
import ir.rahgozin.wallet.application.customer.CustomerRepository;
import ir.rahgozin.wallet.application.wallet.command.DebitAccountCommand;
import ir.rahgozin.wallet.application.wallet.dto.DebitAccountDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Locale;

import static ir.rahgozin.wallet.application.common.exception.BusinessError.*;

@Service
@RequiredArgsConstructor
public class DebitTransactionService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final MessageRepository messageRepository;
    private final MessageSource messageSource;
    private final CustomerRepository customerRepository;


    @Transactional
    public DebitAccountDTO debitAccount(DebitAccountCommand command) {
        Transaction existing = transactionRepository.findByRequestId(command.getRequestId());
        if (existing != null) {
            return new DebitAccountDTO(
                    existing.getAccount().getNumber(),
                    existing.getAmount()
            );
        }
        Account account = findAccount(command.getAccountNumber());
        if(!account.getOwner().isVerified() || account.getOwner().isDeleted()){
            throw new BusinessException(INVALID_CUSTOMER_STATUS);
        }
        if(account.getType().equals(Account.AccountType.SAVING)){
            throw new BusinessException(INVALID_ACCOUNT_TYPE_FOR_WITHDRAW);
        }

        BigDecimal balance = transactionRepository.calculateBalance(command.getAccountNumber());
        if (balance.compareTo(command.getAmount()) < 0) {
            throw new BusinessException(INSUFFICIENT_BALANCE);
        }

        Transaction transaction = new Transaction(
                command.getRequestId(),
                account,
                Direction.DEBIT,
                command.getAmount()
        );
        transactionRepository.save(transaction);
        messageRepository.save(
                new Message(
                        getMessage("WITHDRAW_MESSAGE", account.getNumber(), command.getAmount().toPlainString()),
                        MessageStatus.PENDING,
                        command.getRequestId()
                )
        );

        return new DebitAccountDTO(account.getNumber(), command.getAmount());
    }

    private Account findAccount(String accountNumber) {
        Account account = accountRepository.findByNumber(accountNumber);
        if (account == null) {
            throw new BusinessException(ACCOUNT_NOT_FOUND);
        }
        return account;
    }

    private String getMessage(String key, String... args) {
        return messageSource.getMessage(key, args, Locale.of("fa"));
    }
}
