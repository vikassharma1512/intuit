package com.bidding.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class BidWinnerDto {
    private Integer projectId;
    private Integer bidId;
    private Integer bidderId;
    private Double quote;
    private LocalDateTime bidDate;
    private LocalDateTime createDate;
}
