package com.test.btc.domian;

import lombok.*;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class BtcdUtxo {

    private int id;
    private String coin;
    private int block;
    private String address;
    private String txid;
    private int vout;
    private BigDecimal amount;
    private int status;

}
