package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.NoSuchElementException;

@Component
public class JdbcAccountDao implements AccountDao{

    private JdbcTemplate jdbcTemplate;

    @Override
    public BigDecimal getCurrentBalance(Account account) throws NoSuchElementException {
        String sql = "SELECT balance FROM account WHERE user_id = ?;";
        SqlRowSet sqlBalance = jdbcTemplate.queryForRowSet(sql, account.getUser_id());
        if(sqlBalance.next()){
            return mapRowToAccount(sqlBalance).getBalance();
        } else {throw new NoSuchElementException("Balance not found.");
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
