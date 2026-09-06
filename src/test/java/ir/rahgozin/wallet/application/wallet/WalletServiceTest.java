//package ir.rahgozin.wallet.application.wallet;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//
//import java.util.HashSet;
//import java.util.List;
//import java.util.Optional;
//import java.util.Set;
//import java.util.stream.Collectors;
//import java.util.stream.IntStream;
//
//import org.apache.commons.lang3.RandomStringUtils;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
//
//import ir.rahgozin.wallet.application.common.NotificationService;
//import ir.rahgozin.wallet.application.customer.Customer;
//import ir.rahgozin.wallet.application.wallet.dto.AccountBalanceDTO;
//import ir.rahgozin.wallet.application.wallet.dto.TransactionDTO;
//import ir.rahgozin.wallet.application.wallet.query.AccountBalanceQuery;
//import ir.rahgozin.wallet.application.wallet.query.TransactionQuery;
//
//
//@SpringJUnitConfig({
//        WalletService.class
//})
//public class WalletServiceTest {
//
//    @Autowired
//    WalletService walletService;
//
//    @MockBean
//    AccountRepository accountRepository;
//
//    @MockBean
//    NotificationService notificationService;
//
//
//    @Test
//    void givenCustomerWithNoTransaction_whenInquiryAccountBalanceMoreThanLimitCountPerDay_thenThrowException() {
//        Long customerId = Long.valueOf(RandomStringUtils.randomNumeric(5));
//
//        var accountOwner = new Customer();
//        accountOwner.setId(customerId);
//
//        var account = new Account();
//        account.setId(1L);
//        account.setOwner(accountOwner);
//
//
//        var debitTransactions =  IntStream.rangeClosed(1, 10)
//                .mapToObj(operand ->
//                        new Transaction(RandomStringUtils.randomNumeric(5), account, "deposit",  10000.0 ))
//                .collect(Collectors.toSet());
//
//        var withdrawalTransactions = IntStream.rangeClosed(1, 5).mapToObj(value ->
//                new Transaction(RandomStringUtils.randomNumeric(5), account, "withdrawal", 10000.0)
//        ).collect(Collectors.toSet());
//
//        Set<Transaction> totalTransactions = new HashSet<>();
//        totalTransactions.addAll(debitTransactions);
//        totalTransactions.addAll(withdrawalTransactions);
//        account.setTransactions(totalTransactions);
//
//        Mockito.doReturn(List.of(account)).when(accountRepository).findAll();
//
//
//        var accountBalanceQuery = new AccountBalanceQuery();
//        accountBalanceQuery.setCustomerId(customerId);
//        Optional<AccountBalanceDTO> accountBalanceDto = walletService.accountBalance(accountBalanceQuery).stream()
//                .filter(accountBalanceDTO -> accountBalanceDTO.getNumber().equals(account.getId()))
//                .findFirst();
//
//        assertTrue(accountBalanceDto.isPresent());
//        assertEquals(50000.0, accountBalanceDto.get().getAmount());
//    }
//
//    @Test
//    void givenTransactionQuery_whenInquiryTransactionList_thenReturnCorrectQueriedList() {
//        Long customerId = Long.valueOf(RandomStringUtils.randomNumeric(5));
//
//        var accountOwner = new Customer();
//        accountOwner.setId(customerId);
//
//        var account = new Account();
//        account.setId(1L);
//        account.setOwner(accountOwner);
//
//        var anotherCustomer = new Customer();
//        anotherCustomer.setId(Long.valueOf(RandomStringUtils.randomNumeric(5)));
//
//        var anotherAccount = new Account();
//        anotherAccount.setId(1L);
//        anotherAccount.setOwner(anotherCustomer);
//
//        var mainAccountTransactions =  IntStream.rangeClosed(1, 10)
//                .mapToObj(operand -> {
//                    if (operand > 5)
//                         return new Transaction(RandomStringUtils.randomNumeric(5), account, "deposit",  10000.0 );
//                    else {
//                        return new Transaction(RandomStringUtils.randomNumeric(5), account, "withdrawal",  5000.0 );
//                    }
//                })
//                .collect(Collectors.toSet());
//        account.setTransactions(mainAccountTransactions);
//
//        var withdrawalTransactions = IntStream.rangeClosed(1, 5).mapToObj(value ->
//                new Transaction(RandomStringUtils.randomNumeric(5), anotherAccount, "withdrawal", 10000.0)
//        ).collect(Collectors.toSet());
//        anotherAccount.setTransactions(withdrawalTransactions);
//
//
//        Mockito.doReturn(List.of(account, anotherAccount)).when(accountRepository).findAll();
//
//        var transactionQuery = new TransactionQuery();
//        transactionQuery.setDirection("deposit");
//        transactionQuery.setCustomerId(customerId);
//        transactionQuery.setFromAmount(6000.0);
//
//
//        var transactions = walletService.listTransactions(transactionQuery);
//        var sum = transactions.stream().map(TransactionDTO::getAmount).reduce(0D, (first, second) ->  first + second);
//
//        assertEquals(75000, sum);
//    }
//}