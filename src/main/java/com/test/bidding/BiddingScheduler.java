package com.test.bidding;

import com.test.bidding.task.BiddingWinner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import static java.time.LocalDateTime.now;

@Component
public class BiddingScheduler {

    private static final Logger log = LoggerFactory.getLogger(BiddingScheduler.class);

    @Autowired
    BiddingWinner biddingWinner;

    @Scheduled(cron = "${cron.expression}")
    public void cronJobSch() {
        log.info("BiddingScheduler started at: {}", now());
        biddingWinner.execute();
    }

}
