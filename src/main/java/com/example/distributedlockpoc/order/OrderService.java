package com.example.distributedlockpoc.order;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;


@Service
@AllArgsConstructor
@Slf4j
public class OrderService {

    private RedissonClient redissonClient;

    public static final String LOCK_KEY = "order-lock";

    public static final String STOCK_KEY = "stock";

    public OrderStatus makeOrder() throws InterruptedException {
        boolean accquired = redissonClient.getLock(LOCK_KEY).tryLock(20, TimeUnit.SECONDS);

        if (accquired) {
            try {
                Integer stockValue = (Integer) redissonClient.getBucket(STOCK_KEY).get();
                if (stockValue > 0) {
                    redissonClient.getBucket(STOCK_KEY).set(--stockValue);
                    log.info("Order created successfully, Stock left {}", stockValue);
                    return OrderStatus.CREATED;
                } else {
                    log.info("Out of stock, Stock left {}", stockValue);
                    return OrderStatus.OUT_OF_STOCK;
                }
            } finally {
                redissonClient.getLock(LOCK_KEY).unlock();
            }
        } else {
            log.error("Failed to acquire lock, lock timeout exceeded: {}", LOCK_KEY);
            return OrderStatus.FAILED;
        }
    }

}
