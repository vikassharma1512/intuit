package com.bidding.dto;

import java.time.LocalDateTime;

public class BidWinnerDto {
    private Integer projectId;
    private Integer bidId;
    private Integer bidderId;
    private Double quote;
    private LocalDateTime createDate;

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public Integer getBidId() {
        return bidId;
    }

    public void setBidId(Integer bidId) {
        this.bidId = bidId;
    }

    public Integer getBidderId() {
        return bidderId;
    }

    public void setBidderId(Integer bidderId) {
        this.bidderId = bidderId;
    }

    public Double getQuote() {
        return quote;
    }

    public void setQuote(Double quote) {
        this.quote = quote;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    @Override
    public String toString() {
        return "BidWinnerDto{" +
                "projectId=" + projectId +
                ", bidId=" + bidId +
                ", bidderId=" + bidderId +
                ", quote=" + quote +
                ", createDate=" + createDate +
                '}';
    }
}
