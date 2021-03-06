package com.test.bidding.task;

import com.test.bidding.dto.BidWinnerDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class BiddingWinnerImpl implements BiddingWinner {

    private static final Logger log = LoggerFactory.getLogger(BiddingWinnerImpl.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static  final  String queryInsertWinners = "INSERT INTO BID_WINNER VALUES (?, ?, ?, ?, ?)";


    private  static final String query = "select \n" +
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

        List<BidWinnerDto> winningBid = jdbcTemplate.query(query, new RowMapper<BidWinnerDto>() {
            @Override
            public BidWinnerDto mapRow(ResultSet rs, int rownumber) throws SQLException {
                BidWinnerDto e = new BidWinnerDto();
                e.setProjectId(rs.getInt(1));
                e.setBidId(rs.getInt(2));
                e.setBidderId(rs.getInt(3));
                e.setQuote(rs.getDouble(4));
                e.setCreateDate(rs.getTimestamp(5).toLocalDateTime());
                return e;
            }
        });

        if (winningBid == null || winningBid.size() == 0) {
            log.info("No Bids settled.");
        } else {
            log.info("winning bids: ");
            winningBid.forEach(record -> {
                log.info(record.toString());
                jdbcTemplate.update(
                        queryInsertWinners,
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

