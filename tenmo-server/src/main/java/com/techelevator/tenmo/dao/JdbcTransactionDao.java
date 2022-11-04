package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transaction;
import com.techelevator.tenmo.model.TransactionRequestDTO;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.objenesis.ObjenesisException;
import org.springframework.stereotype.Component;

import java.awt.dnd.InvalidDnDOperationException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static java.time.LocalTime.now;

@Component
public class JdbcTransactionDao implements TransactionDao {

    private JdbcTemplate jdbcTemplate;
    private AccountDao accountDao;
    private final BigDecimal ZERO = new BigDecimal("0.0");
    private UserDao userDao;

    public JdbcTransactionDao(JdbcTemplate jdbcTemplate, AccountDao accountDao, UserDao userDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.accountDao = accountDao;
        this.userDao = userDao;
    }

    @Override
    public Transaction sendMoney(TransactionRequestDTO transactionRequest, String username) {

        BigDecimal transferAmt = transactionRequest.getTransferAmount();
        Account endAccount = accountDao.getAccountById(userDao.findIdByUsername(transactionRequest.getEndUsername()));
        Account primaryAccount = accountDao.getAccountById(userDao.findIdByUsername(username));

        // checks
        // 1. can't send more money than in account
        if (transferAmt.compareTo(primaryAccount.getBalance()) > 0) {
            throw new InvalidDnDOperationException();
        }
        // 2. can't send money to self
        if (endAccount.equals(primaryAccount)) {
            throw new InvalidDnDOperationException();
        }
        // 3. both users are authenticated
        // 4. can't send zero or negative transfer
        if (transferAmt.compareTo(ZERO) < 1) {
            throw new InvalidDnDOperationException();
        }
        // 5.  primary account exists
        if (!accountDao.verifyAccountById(primaryAccount.getAccount_id())) {
            throw new ObjenesisException("poop");
        }
        // 6. end account exists
        if (!accountDao.verifyAccountById(endAccount.getAccount_id())) {
            throw new ObjenesisException("poop");
        }
        //updatePrimaryAccount
        accountDao.updatePrimaryAccount(primaryAccount, transferAmt);
        //updateEndAccount
        accountDao.updateEndAccount(endAccount, transferAmt);

        String sql = "INSERT INTO user_transactions (primary_account_id, end_account_id, transfer_amount, end_user_approval) VALUES (?, ?, ?, ?) RETURNING transaction_id;";
        Long newTransactionId = jdbcTemplate.queryForObject(sql, Long.class, primaryAccount, endAccount, transferAmt, true);
        return findTransaction(newTransactionId, username);
    }

    @Override
    public List<Transaction> findAllTransactions(long id) {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT transaction_id, primary_account_id, end_account_id, transfer_amount," +
                " transaction_date, end_user_approval FROM user_transactions WHERE primary_account_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id);
        while (results.next()) {
            Transaction transaction = mapRowToTransaction(results);
            transactions.add(transaction);
        }
        return transactions;
    }

    @Override
    public Transaction findTransaction(long transactionId, String username) {
        String sql = "SELECT transaction_id, primary_account_id, end_account_id, transfer_amount, transaction_date, end_user_approval " +
                "FROM user_transactions ut " +
                "JOIN account a ON a.account_id=ut.primary_account_id " +
                "JOIN tenmo_user tu ON tu.user_id = a.user_id  WHERE transaction_id = ? AND username = ?;";
        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, transactionId, username);

        if (result.next()) {
            return mapRowToTransaction(result);
        } else {
            return null;
        }
    }

    private Transaction mapRowToTransaction(SqlRowSet rs) {
        Transaction transaction = new Transaction();
        transaction.setTransactionId(rs.getLong("transaction_id"));
        transaction.setPrimaryAccount(accountDao.getAccountById(rs.getLong("primary_account_id")));
        transaction.setEndAccount(accountDao.getAccountById(rs.getLong("end_account_id")));
        transaction.setTransferAmount(rs.getBigDecimal("transfer_amount"));
        transaction.setEndUserApproval(rs.getBoolean("end_user_approval"));
        transaction.setTransactionDate(rs.getDate("transaction_date"));
        return transaction;
    }


}
