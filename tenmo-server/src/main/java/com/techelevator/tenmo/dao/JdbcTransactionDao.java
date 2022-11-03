package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transaction;
import com.techelevator.tenmo.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.objenesis.ObjenesisException;
import org.springframework.stereotype.Component;

import java.awt.dnd.InvalidDnDOperationException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    @Override
    public List<Transaction> findAllTransactions(long id) {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT transaction_id, primary_account_id, end_account_id, transfer_amount," +
                " transfer_date, end_user_approval FROM user_transactions WHERE primary_account_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id);

        while(results.next()) {
            User user = mapRowToUser(results);
            users.add(user);
        }
        return users;
    }
    // TODO: fix this and everything associated. And also complete the above findAllTransactions method
    private Transaction mapRowToTransaction(SqlRowSet rs) {
        Transaction transaction = new Transaction();
        transaction.setTransactionId(rs.getLong("transaction_id"));
        transaction.setPrimaryAccount(accountDao.getAccountById(rs.getLong("primary_account_id"))) ;
        transaction.setEndAccount(rs.getLong("primary_account_id"));
        transaction.setEndAccount(rs.getLong("end_account_id"));
        transaction.setTransferAmount(rs.getBigDecimal("transfer_amount"));
        transaction.setTransactionId(rs.getLong("transaction_id"));
        transaction.setTransactionId(rs.getLong("transaction_id"));

    }





}
