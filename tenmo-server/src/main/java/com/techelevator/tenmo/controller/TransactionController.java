package com.techelevator.tenmo.controller;


import com.techelevator.tenmo.dao.TransactionDao;
import com.techelevator.tenmo.dao.UserDao;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transactions")
@PreAuthorize("isAuthenticated()")

public class TransactionController {

    private TransactionDao dao;







}
