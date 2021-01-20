package com.test.cases;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Logdemo {

    static {
        System.setProperty("fileName", "contract/info.log");
    }
    public static Logger log = LoggerFactory.getLogger(Logdemo.class);

    public static void main(String[] args) {
        log.info("test...");
    }
}
