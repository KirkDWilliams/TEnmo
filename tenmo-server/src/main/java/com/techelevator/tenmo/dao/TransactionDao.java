package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.RequestMoneyDTO;
import com.techelevator.tenmo.model.Transaction;
import com.techelevator.tenmo.model.SendMoneyDTO;

import java.util.List;

public interface TransactionDao {


    Transaction sendMoney(SendMoneyDTO transaction, String username);

    Transaction requestMoney(RequestMoneyDTO request, String username);

    void acceptOrDeny(Transaction transaction, boolean isAcceptedOrDenied);

    List<Transaction> findAllTransactions(long id);

    List<Transaction> findAllPendingTransactions(long id);

    Transaction findTransaction(long transactionId, String username);
}
