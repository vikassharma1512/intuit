package com.bidding;

import com.bidding.dto.BidWinnerDto;
import org.awaitility.Duration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.verify;

@SpringBootTest
//@EnableConfigurationProperties(H2DBConfiguration.class)
@ActiveProfiles(  profiles = { "test"})
//@JdbcTest
//@Sql({"classpath:db/schema.sql", "classpath:db/data.sql"})
public class Test1 {
    //@Autowired
    @SpyBean
    private BiddingScheduler tasks;

   /* @Test
    void contextLoads() {
        // Basic integration test that shows the context starts up properly
        assertThat(tasks).isNotNull();
    }*/

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void whenInjectInMemoryDataSource_thenReturnCorrectEmployeeCount() {
        assertThat(tasks).isNotNull();
        assertThat(jdbcTemplate).isNotNull();

        await().atMost(Duration.ONE_MINUTE).untilAsserted(() -> {
            verify(tasks, atLeast(1)).cronJobSch();
        });

        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String SELECT_WINNERS = "SELECT project_id, bid_id, bidder_id, quote, create_date FROM bid_winner";

        List<BidWinnerDto> winningBid = jdbcTemplate.query(SELECT_WINNERS, (rs, rownumber) -> {
            BidWinnerDto winnerDto = new BidWinnerDto();
            winnerDto.setProjectId(rs.getInt(1));
            winnerDto.setBidId(rs.getInt(2));
            winnerDto.setBidderId(rs.getInt(3));
            winnerDto.setQuote(rs.getDouble(4));
            winnerDto.setCreateDate(rs.getTimestamp(5).toLocalDateTime());
            return winnerDto;
        });

        assertEquals(1, winningBid.size());
    }
}
