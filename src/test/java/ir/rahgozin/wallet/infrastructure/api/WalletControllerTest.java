package ir.rahgozin.wallet.infrastructure.api;

import ir.rahgozin.wallet.application.wallet.WalletService;
import ir.rahgozin.wallet.application.wallet.dto.AccountBalanceDTO;

import ir.rahgozin.wallet.application.wallet.query.AccountBalanceQuery;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Log4j2
@SpringBootTest
@AutoConfigureMockMvc
public class WalletControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WalletService walletService;

    @Test
    public void testRateLimit() throws Exception {
        String customerId = "12345";
        AccountBalanceDTO accountBalanceDTO = new AccountBalanceDTO();
        accountBalanceDTO.setNumber(1L);
        accountBalanceDTO.setAmount(1D);
        when(walletService.accountBalance(any(AccountBalanceQuery.class))).thenReturn(List.of(accountBalanceDTO));

        for (int i = 1; i <= 5; i++) {
            mockMvc.perform(post("/wallet/balance")
                            .param("customerId", customerId))
                    .andExpect(status().isOk());
        }


        mockMvc.perform(post("/wallet/balance")
                        .param("customerId", customerId))
                .andExpect(status().isTooManyRequests());
    }

    @Test
    public void testRateLimitResetsForDifferentCustomer() throws Exception {
        String customerId1 = "12345";
        String customerId2 = "67890";

        when(walletService.accountBalance(any())).thenReturn(List.of());

        for (int i = 1; i <= 5; i++) {
            mockMvc.perform(post("/wallet/balance")
                            .param("customerId", customerId1))
                    .andExpect(status().isOk());
        }

        mockMvc.perform(post("/wallet/balance")
                        .param("customerId", customerId2))
                .andExpect(status().isOk());

        mockMvc.perform(post("/wallet/balance")
                        .param("customerId", customerId1))
                .andExpect(status().isTooManyRequests());
    }

    @Test
    public void testRateLimitUnderConcurrentRequests() throws Exception {
        String customerId = "12345";

        AccountBalanceDTO accountBalanceDTO = new AccountBalanceDTO();
        accountBalanceDTO.setNumber(1L);
        accountBalanceDTO.setAmount(1D);
        when(walletService.accountBalance(any(AccountBalanceQuery.class))).thenReturn(List.of(accountBalanceDTO));

        int numThreads = 10;
        int[] okResultCount = {0};

        CountDownLatch latch = new CountDownLatch(1);

        ExecutorService executorService = Executors.newFixedThreadPool(numThreads);

        for (int i = 0; i < numThreads; i++) {
            executorService.submit(() -> {
                try {
                    latch.await();
                    mockMvc.perform(post("/wallet/balance")
                                    .param("customerId", customerId))
                            .andDo(result -> {
                                if(result.getResponse().getStatus() == 200){
                                    okResultCount[0]++;
                                }
                            });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }


        latch.countDown();


        executorService.shutdown();
        boolean awaited = executorService.awaitTermination(10, TimeUnit.SECONDS);
        log.info("executorService is awaited: {}", awaited);
        assert okResultCount[0] <= 5 : "More than 5 requests were successful, showing the rate limiter failed under concurrency.";
    }
}