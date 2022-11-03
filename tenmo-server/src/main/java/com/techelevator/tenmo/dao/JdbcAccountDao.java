package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.objenesis.ObjenesisException;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.NoSuchElementException;

@Component
public class JdbcAccountDao implements AccountDao {

    private JdbcTemplate jdbcTemplate;

    public JdbcAccountDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public BigDecimal getCurrentBalance(Account account) throws NoSuchElementException {
        String sql = "SELECT balance FROM account WHERE user_id = ?;";
        SqlRowSet sqlBalance = jdbcTemplate.queryForRowSet(sql, account.getUser_id());
        if (sqlBalance.next()) {
            return mapRowToAccount(sqlBalance).getBalance();
        } else {
            throw new NoSuchElementException("Balance not found.");
        }
    }

    @Override
    public boolean updatePrimaryAccount(Account account, BigDecimal transferAmt) {
        String primaryAccntSql = "UPDATE account SET balance = ? WHERE account_id = ?";
        try {
            jdbcTemplate.update(primaryAccntSql, (account.getBalance().subtract(transferAmt)), account.getAccount_id());
        } catch (IllegalArgumentException e) {
            System.out.println("Insufficient funds for transfer");
            return false;
        }
        return true;
    }

    @Override
    public boolean updateEndAccount(Account account, BigDecimal transferAmt) {
        String endAccntSql = "UPDATE account SET balance = ? WHERE account_id = ?";
        try {
            jdbcTemplate.update(endAccntSql, (account.getBalance().add(transferAmt)), account.getAccount_id());
        } catch (IllegalArgumentException e) {
            System.err.println("ERROR Insufficient funds");
            return false;
        }
        return true;
    }

    @Override
    public boolean verifyAccountById(long accountId) {
        String sql = "SELECT account_id FROM account WHERE account_id = ?;";
        Account foundAccount = jdbcTemplate.queryForObject(sql, Account.class, accountId);
        if (foundAccount != null) {
            return true;
        } else {
            System.err.println("Account does exist!");
            return false;
        }
    }

    private Account mapRowToAccount(SqlRowSet rs) {
        Account account = new Account();
        account.setAccount_id(rs.getLong("user_id"));
        account.setBalance(rs.getBigDecimal("balance"));
        account.setUser_id(rs.getLong("account_id"));
        return account;
    }
}
