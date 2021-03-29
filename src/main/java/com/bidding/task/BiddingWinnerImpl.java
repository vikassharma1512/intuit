package com.bidding.task;

import com.bidding.dto.BidWinnerDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class BiddingWinnerImpl implements BiddingWinner {

    private static final Logger log = LoggerFactory.getLogger(BiddingWinnerImpl.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final String INSERT_WINNERS = "INSERT INTO BID_WINNER VALUES (?, ?, ?, ?, ?)";

    private static final String SELECT_WINNERS = "select \n"
            + "p.id  id \n"
            + ", b.id bid_id \n"
            + ", b.bidder_id \n"
            + ",CASE \n"
            + "    WHEN b.fixed_price IS NOT NULL THEN  b.fixed_price \n"
            + "    WHEN b.hourly_price IS NOT NULL THEN b.hourly_price * p.estimated_hours \n"
            + "    ELSE 0   \n"
            + "END quote \n"
            + ",  b.create_date bid_date \n"
            + ", sysdate() create_date  \n"
            + "   from project p \n"
            + "join  \n"
            + "bid b on p.id = b.project_id \n"
            + "where \n"
            + "b.project_id not in \n"
            + "(select distinct bw.project_id from  bid_winner bw) \n"
            + "and \n"
            + " b.create_date <= p.deadline \n"
            + " and  p.deadline < sysdate() \n"

            + " group by p.id, b.id, b.bidder_id \n"
            + "  order by quote, b.create_date asc \n";

    @Override
    public void execute() {
        log.info("Checking for project deadlines.");

        List<BidWinnerDto> winningBids = jdbcTemplate.query(SELECT_WINNERS, (rs, rownumber) -> {
            BidWinnerDto winnerDto = new BidWinnerDto();
            winnerDto.setProjectId(rs.getInt(1));
            winnerDto.setBidId(rs.getInt(2));
            winnerDto.setBidderId(rs.getInt(3));
            winnerDto.setQuote(rs.getDouble(4));
            winnerDto.setBidDate(rs.getTimestamp(5).toLocalDateTime());
            winnerDto.setCreateDate(rs.getTimestamp(6).toLocalDateTime());
            return winnerDto;
        });

        Map<Integer, List<BidWinnerDto>> winningBidsMap = winningBids.stream().collect(Collectors.groupingBy(BidWinnerDto::getProjectId));

        Set<Integer> projectIds = winningBidsMap.keySet();

        if (projectIds.isEmpty()) {
            log.warn("No Bids settled.");
        } else {
            log.info("Winning Bids: ");
            projectIds.forEach(id -> {
                BidWinnerDto record = winningBidsMap.get(id).get(0);
                log.info(record.toString());
                jdbcTemplate.update(
                        INSERT_WINNERS,
                        record.getProjectId()
                        , record.getBidId()
                        , record.getBidderId()
                        , record.getQuote()
                        , record.getCreateDate()
                );
            });
        }

    }

}

