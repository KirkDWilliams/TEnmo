package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transaction;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import java.math.BigDecimal;
import java.util.NoSuchElementException;

public class JdbcTransactionDao implements UserDao{

    private JdbcTemplate jdbcTemplate;


    @Override
    public BigDecimal getBalance(long userId) throws NoSuchElementException {
        String sql = "SELECT balance FROM account WHERE user_id = ?;";
        SqlRowSet sqlBalance = jdbcTemplate.queryForRowSet(sql, userId);
        if(sqlBalance.next()){
            return mapRowToAccount(sqlBalance).getBalance();
        } else {throw new NoSuchElementException("Balance not found.");
        }
    }

    @Override
    public Transaction demandTransaction(long primaryAccountId, long endAccountId, BigDecimal transferAmount) {
        return null;
    }

    @Override
    public Transaction profferTransaction(long primaryAccountId, long endAccountId, BigDecimal transferAmount) {
        return null;
    }

    @Override
    public Boolean acceptTransaction(Transaction transaction) {
        return null;
    }

    @Override
    public Boolean refuseTransaction(Transaction transaction) {
        return null;
    }

    @Override
    public BigDecimal updateBalances(Transaction transaction) {
        return null;
    }

    private Account mapRowToAccount(SqlRowSet rs) {
        Account account = new Account();
        account.setAccount_id(rs.getLong("user_id"));
        account.setBalance(rs.getBigDecimal("balance"));
        account.setUser_id(rs.getLong("account_id"));
        return account;
    }
}
