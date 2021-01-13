package com.zmj.domain.orther;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class DepositChain implements Serializable {

    private int id;
    private long user_id;
    private String address;
    private int currency_id;
    private int block;
    private int status;
    private String txid;
    private int vout;
    private BigDecimal amount;
    private int site_id;
    private Date create_date;
    private Date modify_date;
}
