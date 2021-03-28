package com.test.bidding.task;

import com.test.bidding.dto.BidWinnerDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BiddingWinnerImpl implements BiddingWinner {

    private static final Logger log = LoggerFactory.getLogger(BiddingWinnerImpl.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static  final  String INSERT_WINNERS = "INSERT INTO BID_WINNER VALUES (?, ?, ?, ?, ?)";


    private  static final String SELECT_WINNERS = "select \n" +
            "p.id  id\n" +
            ", b.id\n" +
            ", b.bidder_id\n" +
            ", min(CASE\n" +
            "    WHEN b.fixed_price IS NOT NULL THEN  b.fixed_price\n" +
            "    WHEN b.hourly_price IS NOT NULL THEN b.hourly_price * p.estimated_hours\n" +
            "    ELSE 0  \n" +
            "END ) quote\n" +
            ", sysdate() create_date\n" +
            " from project p \n" +
            "join bid b on p.id = b.project_id\n" +
            " \n" +
            "where\n" +
            "b.project_id not in \n" +
            "(select distinct bw.project_id from  bid_winner bw) \n" +
            "and \n" +
            " b.create_date <= p.deadline \n" +
            " and  p.deadline < sysdate()\n" +
            "group by p.id\n" +
            " order by quote asc";


    @Override
    public void execute() {
        log.info("Checking for project deadlines.");

        List<BidWinnerDto> winningBid = jdbcTemplate.query(SELECT_WINNERS, (rs, rownumber) -> {
            BidWinnerDto winnerDto = new BidWinnerDto();
            winnerDto.setProjectId(rs.getInt(1));
            winnerDto.setBidId(rs.getInt(2));
            winnerDto.setBidderId(rs.getInt(3));
            winnerDto.setQuote(rs.getDouble(4));
            winnerDto.setCreateDate(rs.getTimestamp(5).toLocalDateTime());
            return winnerDto;
        });

        if (winningBid.isEmpty()) {
            log.info("No Bids settled.");
        } else {
            log.info("winning bids: ");
            winningBid.forEach(record -> {
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

