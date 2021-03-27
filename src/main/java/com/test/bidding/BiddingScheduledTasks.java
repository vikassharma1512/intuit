package com.test.bidding;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.test.bidding.task.BiddingWinner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class BiddingScheduledTasks {

    private static final Logger log = LoggerFactory.getLogger(BiddingScheduledTasks.class);

    @Autowired
    BiddingWinner biddingWinner;

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Scheduled(fixedRateString ="${scheduler.timer}", initialDelay=1000)
    public void reportCurrentTime() {
        biddingWinner.execute();
    }

}
