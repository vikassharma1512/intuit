package com.test.bidding;

import com.test.bidding.config.JdbcConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@EnableConfigurationProperties(JdbcConfig.class)
class BiddingApplicationTests {

    @Autowired
    private BiddingScheduledTasks tasks;

    @Test
    public void contextLoads() {
        // Basic integration test that shows the context starts up properly
        assertThat(tasks).isNotNull();
    }

}
