package com.techelevator.tenmo.model;

import java.math.BigDecimal;
import java.util.Date;

public class Transaction {
    private Long transactionId;
    private Long primaryAccountId;
    private Long endAccountId;
    private BigDecimal transferAmount;
    private boolean endUserApproval;
    private Date transactionDate;

    public Transaction(){}

    public Transaction(long transactionId, long primaryAccountId, long endAccountId, BigDecimal transferAmount, Date transactionDate){
        this.transactionId = transactionId;
        this.primaryAccountId = primaryAccountId;
        this.endAccountId = endAccountId;
        this.transferAmount = transferAmount;
        this.endUserApproval = false;
        this.transactionDate = transactionDate;
    }

    public Long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    public Long getPrimaryAccountId() {
        return primaryAccountId;
    }

    public void setPrimaryAccountId(Long primaryAccountId) {
        this.primaryAccountId = primaryAccountId;
    }

    public Long getEndAccountId() {
        return endAccountId;
    }

    public void setEndAccountId(Long endAccountId) {
        this.endAccountId = endAccountId;
    }

    public BigDecimal getTransferAmount() {
        return transferAmount;
    }

    public void setTransferAmount(BigDecimal transferAmount) {
        this.transferAmount = transferAmount;
    }

    public boolean isEndUserApproval() {
        return endUserApproval;
    }

    public void setEndUserApproval(boolean endUserApproval) {
        this.endUserApproval = endUserApproval;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }
}
