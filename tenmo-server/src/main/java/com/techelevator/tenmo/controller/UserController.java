package com.techelevator.tenmo.controller;


import com.techelevator.tenmo.dao.JdbcUserDao;
import com.techelevator.tenmo.dao.UserDao;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/user")
@PreAuthorize("isAuthenticated()")

public class UserController {

    private UserDao dao;

    public UserController() {this.dao = new ;}


    @GetMapping(path = "/{id}")
    public BigDecimal getBalance(int id) {
        public List

    }


    @RequestMapping
    public proffer() {

    }

    @RequestMapping
    public demand() {

    }

    @RequestMapping
    public viewMyTransactions() {

    }

    @RequestMapping
    public viewMyTransactionsById() {

    }


}
