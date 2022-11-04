package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transaction;
import com.techelevator.tenmo.model.TransactionRequestDTO;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface TransactionDao {


    Transaction sendMoney(TransactionRequestDTO transaction, String username);

    List<Transaction> findAllTransactions(long id);

    Transaction findTransaction(long transcationId, String username);
}
