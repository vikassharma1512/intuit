package com.test.bidding;

import org.awaitility.Duration;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;

import static org.awaitility.Awaitility.await;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.verify;

@SpringBootTest
class BiddingSchedulerTest {

    @SpyBean
    BiddingScheduler tasks;

    @Test
    void reportCurrentTime() {
        await().atMost(Duration.ONE_MINUTE).untilAsserted(() -> {
            verify(tasks, atLeast(1)).cronJobSch();
        });
    }

}