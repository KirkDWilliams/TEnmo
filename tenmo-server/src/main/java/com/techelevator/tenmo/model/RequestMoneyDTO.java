package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class RequestMoneyDTO {
    String endUsername;
    BigDecimal transferAmount;
    boolean approvalStatus;

    public boolean isApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(boolean approvalStatus) {
        this.approvalStatus = approvalStatus;
    }





    public String getEndUsername() {
        return endUsername;
    }

    public void setEndUsername(String endUsername) {
        this.endUsername = endUsername;
    }

    public BigDecimal getTransferAmount() {
        return transferAmount;
    }

    public void setTransferAmount(BigDecimal transferAmount) {
        this.transferAmount = transferAmount;
    }

}
