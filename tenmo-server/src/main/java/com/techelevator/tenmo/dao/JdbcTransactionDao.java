package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.RequestMoneyDTO;
import com.techelevator.tenmo.model.Transaction;
import com.techelevator.tenmo.model.SendMoneyDTO;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.objenesis.ObjenesisException;
import org.springframework.stereotype.Component;

import java.awt.dnd.InvalidDnDOperationException;
import java.math.BigDecimal;
import java.util.ArrayList;
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

    // sends money to a registered account.
    @Override
    public Transaction sendMoney(SendMoneyDTO transactionRequest, String username) {

        BigDecimal transferAmt = transactionRequest.getTransferAmount();
        Account endAccount = accountDao.getAccountByAccountId(userDao.findAccountIdByUserId(userDao.findIdByUsername(transactionRequest.getEndUsername())));
        Account primaryAccount = accountDao.getAccountByAccountId(userDao.findAccountIdByUserId(userDao.findIdByUsername(username)));


        // can't send more money than in account
        if (transferAmt.compareTo(primaryAccount.getBalance()) > 0) {
            throw new InvalidDnDOperationException();
        }
        // can't send money to self
        if (endAccount.equals(primaryAccount)) {
            throw new InvalidDnDOperationException();
        }
        // both users are authenticated
        // can't send zero or negative transfer
        if (transferAmt.compareTo(ZERO) < 1) {
            throw new InvalidDnDOperationException();
        }
        // primary account exists
        if (primaryAccount == null) {
            throw new ObjenesisException("poop");
        }
        //  end account exists
        if (endAccount == null) {
            throw new ObjenesisException("poop");
        }
        //updatePrimaryAccount
        accountDao.sendFromPrimaryAccount(primaryAccount, transferAmt);
        //updateEndAccount
        accountDao.receiveIntoEndAccount(endAccount, transferAmt);

        String sql = "INSERT INTO user_transactions (primary_account_id, end_account_id, transfer_amount, end_user_approval) VALUES (?, ?, ?, ?) RETURNING transaction_id;";
        Long newTransactionId = jdbcTemplate.queryForObject(sql, Long.class, primaryAccount.getAccount_id(), endAccount.getAccount_id(), transferAmt, true);
        return findTransaction(newTransactionId, username);
    }

    //beginning of requesting money from another registered user
    @Override
    public Transaction requestMoney(RequestMoneyDTO request, String username) {

        BigDecimal transferAmt = request.getTransferAmount();
        Account endAccount = accountDao.getAccountByAccountId(userDao.findAccountIdByUserId(userDao.findIdByUsername(request.getEndUsername())));
        Account primaryAccount = accountDao.getAccountByAccountId(userDao.findAccountIdByUserId(userDao.findIdByUsername(username)));

        if (transferAmt.compareTo(primaryAccount.getBalance()) > 0) {
            throw new InvalidDnDOperationException();
        }
        if (endAccount.equals(primaryAccount)) {
            throw new InvalidDnDOperationException();
        }
        if (transferAmt.compareTo(ZERO) < 1) {
            throw new InvalidDnDOperationException();
        }
        if (primaryAccount == null) {
            throw new ObjenesisException("poop");
        }
        if (endAccount == null) {
            throw new ObjenesisException("poop");
        }
        String sql = "INSERT INTO user_transactions (primary_account_id, end_account_id, transfer_amount, end_user_approval) VALUES (?, ?, ?, ?) RETURNING transaction_id;";
        Long newTransactionId = jdbcTemplate.queryForObject(sql, Long.class, primaryAccount.getAccount_id(), endAccount.getAccount_id(), transferAmt, false);
        return findTransaction(newTransactionId, username);
    }
    //TODO: find way to get user approval from site

    @Override
    public void acceptOrDeny(Transaction transaction, boolean isApprovedOrRejected) {
        if (isApprovedOrRejected) {
            accountDao.acceptTransferOutOfEndAccount(transaction.getPrimaryAccount(), transaction.getTransferAmount());
            accountDao.requestTransferIntoPrimaryAccount(transaction.getEndAccount(), transaction.getTransferAmount());

        } else if (!isApprovedOrRejected) {
            deleteTransaction(transaction);
        }
    }

    public void deleteTransaction(Transaction transaction) {
        String sql = "DELETE FROM user_transactions WHERE transaction_id = ?;";
        jdbcTemplate.update(sql, transaction.getTransactionId());
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
    public List<Transaction> findAllPendingTransactions(long id) {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT transaction_id, primary_account_id, end_account_id, transfer_amount," +
                " transaction_date, end_user_approval FROM user_transactions WHERE primary_account_id = ? AND end_user_approval = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id, false);
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
        transaction.setPrimaryAccount(accountDao.getAccountByAccountId(rs.getLong("primary_account_id")));
        transaction.setEndAccount(accountDao.getAccountByAccountId(rs.getLong("end_account_id")));
        transaction.setTransferAmount(rs.getBigDecimal("transfer_amount"));
        transaction.setEndUserApproval(rs.getBoolean("end_user_approval"));
        transaction.setTransactionDate(rs.getDate("transaction_date"));
        return transaction;
    }


}
