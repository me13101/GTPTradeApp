package com.gtp.tradeapp.schedule;

import com.gtp.tradeapp.service.portfolio.HistoricalPortfolioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTasks {

    @Autowired
    HistoricalPortfolioService historicalPortfolioService;

    @Scheduled(cron = "0 0 20 1/1 * *")
//    @Scheduled(cron = "0 0/1 * 1/1 * *")
    public void reportCurrentTime() {
        historicalPortfolioService.save();
    }
}
