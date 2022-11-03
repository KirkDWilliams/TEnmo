package com.techelevator.tenmo.controller;


import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.JdbcUserDao;
import com.techelevator.tenmo.dao.TransactionDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transaction;
import com.techelevator.tenmo.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.TransactionException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.security.auth.login.AccountNotFoundException;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/users")
@PreAuthorize("isAuthenticated()")


public class UserController {

    private AccountDao accountDao;
    private TransactionDao transactionDao;
    private UserDao userDao;

    public UserController() {
    }
    
    @GetMapping(path = "")
    public BigDecimal getBalance(Principal principal)  {
        Account account = accountDao.getAccountById(userDao.findAccountIdByUserId(userDao.findIdByUsername(principal.getName())));
        if (account != null) {
            return account.getBalance();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Balance not found.");
        }
    }


    @GetMapping(path = "/transactions/")
    public List<Transaction> viewMyTransactions(Principal principal) {
        List<Transaction> transactions = new ArrayList<>();
        transactions = transactionDao.findAllTransactions(userDao.findAccountIdByUserId(userDao.findIdByUsername(principal.getName())));
        if (transactions.size() == 0) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "No transactions for this user.");
        } else {
            return transactions;
        }
    }

    @GetMapping(path = "/transactions/{id}")
    public Transaction viewMyTransactionById(@PathVariable int id, Principal principal)  {
        Transaction transaction = transactionDao.findTransaction(id, principal.getName());
        if (transaction != null) {
            return transaction;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No transaction with given identification.");
        }


    }

}
