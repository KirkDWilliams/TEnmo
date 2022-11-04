package com.techelevator.tenmo.controller;


import com.techelevator.tenmo.dao.*;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.RequestMoneyDTO;
import com.techelevator.tenmo.model.Transaction;
import com.techelevator.tenmo.model.SendMoneyDTO;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
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

    public UserController(AccountDao accountDao, TransactionDao transactionDao, UserDao userDao) {
        this.accountDao = accountDao;
        this.transactionDao = transactionDao;
        this.userDao = userDao;
    }

    
    @GetMapping(path = "")
    public BigDecimal getBalance(Principal principal)  {
        Account account = accountDao.getAccountByAccountId(userDao.findAccountIdByUserId(userDao.findIdByUsername(principal.getName())));
        if (account != null) {
         //   System.out.println("Your balance is:");
            return account.getBalance();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Balance not found.");
        }
    }


    @GetMapping(path = "/transactions")
    public List<Transaction> viewMyTransactions(Principal principal) {
        List<Transaction> transactions = new ArrayList<>();
        transactions = transactionDao.findAllTransactions(userDao.findAccountIdByUserId(userDao.findIdByUsername(principal.getName())));
       if (transactions.size() == 0) {
       // if (transactions == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No transactions for this user.");
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

    @PostMapping(path = "/transactions")
    public Transaction sendMoney(@RequestBody @Valid SendMoneyDTO transaction, Principal principal){
        return transactionDao.sendMoney(transaction, principal.getName());
    }

    @GetMapping(path = "/requests")
    public Transaction requestMoney(@RequestBody @Valid RequestMoneyDTO transaction, Principal principal){
        return transactionDao.requestMoney(transaction, principal.getName());
    }

    @PostMapping(path = "/requests/{id}")




}
