package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transaction;
import com.techelevator.tenmo.model.User;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.constraints.Min;
import java.math.BigDecimal;
import java.util.Date;
import java.util.NoSuchElementException;

@Component
public class JdbcTransactionDao implements TransactionDao {

    private JdbcTemplate jdbcTemplate;
    private AccountDao accountDao;

    @Override
    public Transaction sendMoney(Transaction transaction) {
        // How much money is being transferred
        BigDecimal transferAmt = transaction.getTransferAmount();
        Account endAccount = transaction.getEndAccount();
        Account primaryAccount = transaction.getPrimaryAccount();
        Date transactionDate = transaction.getTransactionDate();

        String primaryAccntSql = "UPDATE account SET balance = ? WHERE account_id = ?";
        String endAccntSql = "UPDATE account SET balance = ? WHERE account_id = ?";

        try {
            jdbcTemplate.update(primaryAccntSql, (primaryAccount.getBalance().subtract(transferAmt)), primaryAccount.getAccount_id());
        } catch (IllegalArgumentException e) {
            System.out.println("Insufficient funds for transfer");
        }
        try {
            jdbcTemplate.update(endAccntSql, (endAccount.getBalance().add(transferAmt)), endAccount.getAccount_id());
        } catch (IllegalArgumentException e) {
            System.err.println("ERROR Insufficient funds");
        }
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
