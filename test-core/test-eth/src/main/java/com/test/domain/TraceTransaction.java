package com.test.domain;

import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;

@Getter
@Setter
public class TraceTransaction {

    private String fromAddress;
    private String toAddress;
    private BigInteger value;
    private BigInteger gas;
    private String inputData;

}
