package com.techelevator.tenmo.model;

import java.math.BigDecimal;
import java.util.Date;

public class Transaction {
    private Long transaction_id;
    private Long demanding_account_id;
    private Long proffering_account_id;
    private Long end_account_id;
    private BigDecimal transfer_amount;
    private boolean end_user_approval;
    private Date transaction_date;
}
