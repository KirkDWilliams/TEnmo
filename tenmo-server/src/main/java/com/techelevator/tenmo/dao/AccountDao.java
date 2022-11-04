package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;

import java.math.BigDecimal;

public interface AccountDao {

BigDecimal getCurrentBalance(Account account);

   void sendFromPrimaryAccount(Account account, BigDecimal transferAmt);

   void receiveIntoEndAccount(Account account, BigDecimal transferAmt);

 //   boolean verifyAccountById(long accountId);

    void requestTransferIntoPrimaryAccount(Account account, BigDecimal transferAmt);

    void acceptTransferOutOfEndAccount(Account account, BigDecimal transferAmt);

    Account getAccountByAccountId(long id);
}
