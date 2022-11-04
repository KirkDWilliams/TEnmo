package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class SendMoneyDTO {
    String endUsername;
    BigDecimal transferAmount;



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
