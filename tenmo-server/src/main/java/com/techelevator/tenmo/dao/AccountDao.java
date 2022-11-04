package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;

import javax.security.auth.login.AccountNotFoundException;
import java.math.BigDecimal;

public interface AccountDao {

BigDecimal getCurrentBalance(Account account);

    boolean updatePrimaryAccount(Account account, BigDecimal transferAmt);

    boolean updateEndAccount(Account account, BigDecimal transferAmt);

 //   boolean verifyAccountById(long accountId);

    Account getAccountById(long id);
}
