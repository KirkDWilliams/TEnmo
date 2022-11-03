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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.security.auth.login.AccountNotFoundException;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/user")
@PreAuthorize("isAuthenticated()")


public class UserController {

    private AccountDao dao;

    private

    public UserController() {
    }


    @GetMapping(path = "/{id}")
    public BigDecimal getBalance(@PathVariable int id) throws AccountNotFoundException {
        BigDecimal balance = dao.getAccountById(id).getBalance();
        if (balance == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Balance not found");
        }
        return balance;
    }

    @GetMapping
    public viewMyTransactions() {


    }

    @RequestMapping
    public viewMyTransactionsById() {

    }


}


