package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transaction;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.objenesis.ObjenesisException;
import org.springframework.stereotype.Component;

import java.awt.dnd.InvalidDnDOperationException;
import java.math.BigDecimal;
import java.util.Date;

@Component
public class JdbcTransactionDao implements TransactionDao {

    private JdbcTemplate jdbcTemplate;
    private AccountDao accountDao;
    private final BigDecimal ZERO = new BigDecimal("0.0");

    public JdbcTransactionDao(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Transaction sendMoney(Transaction transaction) {

        BigDecimal transferAmt = transaction.getTransferAmount();
        Account endAccount = transaction.getEndAccount();
        Account primaryAccount = transaction.getPrimaryAccount();
        Date transactionDate = transaction.getTransactionDate();

        // checks
        // 1. can't send more money than in account
        if(transferAmt.compareTo(primaryAccount.getBalance()) > 0){
            throw new InvalidDnDOperationException();
        }
        // 2. can't send money to self
        if(endAccount.equals(primaryAccount)){
            throw new InvalidDnDOperationException();
        }
        // 3. both users are authenticated
        // 4. can't send zero or negative transfer
        if(transferAmt.compareTo(ZERO) < 1){
            throw new InvalidDnDOperationException();
        }
        // 5.  primary account exists
        if(!accountDao.verifyAccountById(primaryAccount.getAccount_id())){
            throw new ObjenesisException("poop");
        }
        // 6. end account exists
        if(!accountDao.verifyAccountById(endAccount.getAccount_id())){
            throw new ObjenesisException("poop");
        }
        //updatePrimaryAccount
        accountDao.updatePrimaryAccount(primaryAccount, transferAmt);
        //updateEndAccount
        accountDao.updateEndAccount(endAccount, transferAmt);
        return transaction;
    }


//    @Override
//    public Transaction demandTransaction(Transaction transaction) {
//        String sql = ""
//
//        return null;
//    }
//
//    @Override
//    public Transaction profferTransaction(long primaryAccountId, long endAccountId, BigDecimal transferAmount) {
//        return null;
//    }
//
//    @Override
//    public Boolean acceptTransaction(Transaction transaction) {
//        return null;
//    }
//
//    @Override
//    public Boolean refuseTransaction(Transaction transaction) {
//        return null;
//    }
//
//    @Override
//    public BigDecimal updateBalances(Transaction transaction) {
//        return null;
//    }


}
