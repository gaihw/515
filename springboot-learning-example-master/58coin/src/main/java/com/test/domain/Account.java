package com.test.domain;

import lombok.Data;

import java.math.BigDecimal;
import java.sql.Time;

@Data
public class Account {
    private int id;
    private int userId;
    private int currencyId;
    private BigDecimal available;
    private BigDecimal hold;
    private int status;
    private Time createDate;
    private Time modifyDate;
}
