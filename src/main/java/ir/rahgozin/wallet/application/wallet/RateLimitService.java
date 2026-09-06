package ir.rahgozin.wallet.application.wallet;

import ir.rahgozin.wallet.application.common.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;

import static ir.rahgozin.wallet.application.common.exception.BusinessError.TOO_MANY_REQUEST;

@Service
@RequiredArgsConstructor
public class RateLimitService {

    private final RedissonClient redissonClient;
    private static final int LIMIT = 5;

    public void checkBalanceRateLimit(Long customerId) {
        String key = "balance-rate:" + customerId + ":" + LocalDate.now();
        RAtomicLong counter = redissonClient.getAtomicLong(key);
        long current = counter.incrementAndGet();
        if (current == 1) {
            counter.expire(Duration.ofDays(1));
        }
        if (current > LIMIT) {
            throw new BusinessException(TOO_MANY_REQUEST);
        }
    }
}