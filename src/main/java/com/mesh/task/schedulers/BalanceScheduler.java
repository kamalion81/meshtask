package com.mesh.task.schedulers;

import com.mesh.task.entity.Account;
import com.mesh.task.service.*;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.*;

@Component
public class BalanceScheduler {

    private Map<Long, BigDecimal> maxBalances;

    @Autowired
    private AccountService accountService;

    @PostConstruct
    public void init() {
        maxBalances = new ConcurrentHashMap<>();
        List<Account> accounts = accountService.findAll();
        accounts.stream().forEach(account -> {
            BigDecimal maxDepo = account.getBalance().multiply(new BigDecimal("2.07"));
            maxDepo.setScale(2, RoundingMode.HALF_UP);
            maxBalances.put(account.getId(), maxDepo);
        });

    }

    @Scheduled(fixedRate = 30000)
    public void increaseBalance() {
        List<Account> accounts = accountService.findAll();
        ExecutorService executorService = Executors.newFixedThreadPool(accounts.size());

        for (Account account : accounts) {
            executorService.execute(() -> {
                BigDecimal maxBalance = maxBalances.get(account.getId());
                if (maxBalance == null) {
                    BigDecimal maxDepo = account.getBalance().multiply(new BigDecimal("2.07"));
                    maxDepo.setScale(2, RoundingMode.HALF_UP);
                    maxBalances.put(account.getId(), maxDepo);
                }
                BigDecimal increaseAmount = account.getBalance().multiply(new BigDecimal("0.1"));
                double maxValue = maxBalance.doubleValue();
                BigDecimal newMaxValue = BigDecimal.valueOf(maxValue);
                BigDecimal totalSum = account.getBalance().add(increaseAmount).setScale(2, RoundingMode.HALF_UP);


                if (newMaxValue.compareTo(totalSum) > 0) {
                    account.setBalance(totalSum);
                    accountService.save(account);
                }


            });
        }

        executorService.shutdown();
    }
}




