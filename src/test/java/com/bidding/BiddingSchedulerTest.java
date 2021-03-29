package com.bidding;

import com.bidding.dto.BidWinnerDto;
import org.awaitility.Duration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.ScriptUtils;

import java.sql.SQLException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.verify;

@SpringBootTest
class BiddingSchedulerTest {
    @SpyBean
    private BiddingScheduler tasks;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final String CREATE_SCHEMA = "db/schema.sql";
    private static final String SINGLE_PROJECT_BID_DATA = "db/single_project_bid_data.sql";
    private static final String MULTIPLE_PROJECT_BID_DATA = "db/multiple_project_bid_data.sql";


    @BeforeEach
    void before() throws SQLException {
        ScriptUtils.executeSqlScript(jdbcTemplate.getDataSource().getConnection(), new ClassPathResource(CREATE_SCHEMA));
    }

    @Test
    void testBiddingSchedulerRun() {
        assertThat(tasks).isNotNull();
        assertThat(jdbcTemplate).isNotNull();

        await().
                atMost(Duration.TEN_SECONDS).
                untilAsserted(() -> verify(tasks, atLeast(1)).cronJobSch());
    }


    @Test
    void testSingleProjectBidWinner() throws SQLException {
        ScriptUtils.executeSqlScript(jdbcTemplate.getDataSource().getConnection(), new ClassPathResource(SINGLE_PROJECT_BID_DATA));
        await().
                atMost(Duration.ONE_MINUTE).
                untilAsserted(() -> verify(tasks, atLeast(2)).cronJobSch());

        List<BidWinnerDto> winningBid = getBidWinnerDtos();

        assertEquals(1, winningBid.size());
        assertEquals(125.0, winningBid.get(0).getQuote());
    }


    @Test
    void testMultipleProjectBidWinner() throws SQLException {
        ScriptUtils.executeSqlScript(jdbcTemplate.getDataSource().getConnection(), new ClassPathResource(MULTIPLE_PROJECT_BID_DATA));
        await().
                atMost(Duration.FIVE_MINUTES).
                untilAsserted(() -> verify(tasks, atLeast(2)).cronJobSch());

        List<BidWinnerDto> winningBid = getBidWinnerDtos();

        assertEquals(2, winningBid.size());
        assertEquals(125, winningBid.get(0).getQuote());
        assertEquals(300, winningBid.get(1).getQuote());
    }


    private List<BidWinnerDto> getBidWinnerDtos() {
        String SELECT_WINNERS = "SELECT project_id, bid_id, bidder_id, quote, create_date FROM bid_winner";

        return jdbcTemplate.query(SELECT_WINNERS, (rs, rownumber) -> {
            BidWinnerDto winnerDto = new BidWinnerDto();
            winnerDto.setProjectId(rs.getInt(1));
            winnerDto.setBidId(rs.getInt(2));
            winnerDto.setBidderId(rs.getInt(3));
            winnerDto.setQuote(rs.getDouble(4));
            winnerDto.setCreateDate(rs.getTimestamp(5).toLocalDateTime());
            return winnerDto;
        });
    }
}
