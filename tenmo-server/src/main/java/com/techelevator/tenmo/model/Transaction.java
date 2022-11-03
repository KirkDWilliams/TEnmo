package com.techelevator.tenmo.model;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import java.math.BigDecimal;
import java.util.Date;



public class Transaction {
    @NotBlank
    private Long transactionId;
    @NotBlank
    private Account primaryAccount;
  //  @NotBlank
  //  private long primaryAccountId;
    @NotBlank
    private Account endAccount;
  //  @NotBlank
  //  private long endAccountId;
    @NotBlank
    @DecimalMin("0.0")
    private BigDecimal transferAmount;
    @NotBlank
    private boolean endUserApproval;
    @NotBlank
    @Past
    private Date transactionDate;

    public Transaction(long transactionId, Account primaryAccount, Account endAccount, BigDecimal transferAmount, Date transactionDate){
        this.transactionId = transactionId;
        this.primaryAccount = primaryAccount;
  //      this.primaryAccountId = primaryAccount.getAccount_id();
        this.endAccount = endAccount;
  //      this.endAccountId = endAccount.getAccount_id();
        this.transferAmount = transferAmount;
        this.endUserApproval = false;
        this.transactionDate = transactionDate;
    }

    public Transaction(){}


    public Long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    public Account getPrimaryAccount() {
        return primaryAccount;
    }

    public void setPrimaryAccount(Account primaryAccount) {
        this.primaryAccount = primaryAccount;
    }

    public Account getEndAccount() {
        return endAccount;
    }

    public void setEndAccount(Account endAccount) {
        this.endAccount = endAccount;
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
