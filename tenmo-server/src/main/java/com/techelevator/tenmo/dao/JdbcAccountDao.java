package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
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
    public void sendFromPrimaryAccount(Account account, BigDecimal transferAmt) {
        String primaryAccntSql = "UPDATE account SET balance = ? WHERE account_id = ?";
        try {
            jdbcTemplate.update(primaryAccntSql, (account.getBalance().subtract(transferAmt)), account.getAccount_id());
        } catch (IllegalArgumentException e) {
            System.out.println("Insufficient funds for transfer");

        }

    }

    @Override
    public void receiveIntoEndAccount(Account account, BigDecimal transferAmt) {
        String endAccntSql = "UPDATE account SET balance = ? WHERE account_id = ?";
        try {
            jdbcTemplate.update(endAccntSql, (account.getBalance().add(transferAmt)), account.getAccount_id());
        } catch (IllegalArgumentException e) {
            System.err.println("ERROR Insufficient funds");
        }
    }

    @Override
    public void requestTransferIntoPrimaryAccount(Account account, BigDecimal transferAmt)  {
        String primaryAccntSql = "UPDATE account SET balance = ? WHERE account_id = ?";
        try {
            jdbcTemplate.update(primaryAccntSql, (account.getBalance().add(transferAmt)), account.getAccount_id());
        } catch (IllegalArgumentException e) {
            System.out.println("Insufficient funds for transfer");
        }

    }

    @Override
    public void acceptTransferOutOfEndAccount(Account account, BigDecimal transferAmt) {
        String endAccntSql = "UPDATE account SET balance = ? WHERE account_id = ?";
        try {
            jdbcTemplate.update(endAccntSql, (account.getBalance().subtract(transferAmt)), account.getAccount_id());
        } catch (IllegalArgumentException e) {
            System.err.println("ERROR Insufficient funds");

        }

    }

//    @Override
//    public boolean verifyAccountById(long accountId) {
//        String sql = "SELECT account_id, user_id, current_balance FROM account WHERE account_id = ?;";
//        Account foundAccount = jdbcTemplate.queryForRowSet(sql, accountId);
//        if (foundAccount != null) {
//            return true;
//        } else {
//            System.err.println("Account does exist!");
//            return false;
//        }
//    }

    @Override
    public Account getAccountByAccountId(long AccountId){
        String sql = "SELECT account_id, user_id, balance FROM account WHERE account_id = ?";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, AccountId);
        if (rowSet.next()){
            return mapRowToAccount(rowSet);
        }
        return null;
    }


    private Account mapRowToAccount(SqlRowSet rs) {
        Account account = new Account();
        account.setUser_id(rs.getLong("user_id"));
        account.setBalance(rs.getBigDecimal("balance"));
        account.setAccount_id(rs.getLong("account_id"));
        return account;
    }
}
