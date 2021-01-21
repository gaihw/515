package com.test.cases;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class Demo {
    static {
        System.setProperty("fileName", "contract/info.log");
    }
    public static Logger log = LoggerFactory.getLogger(Demo.class);

    @BeforeClass
    public void b(){
        System.out.println("b ...");
        log.info("b ....");
    }
    @Test
    public void one(){
        System.out.println("one ...");
        log.info("one ....");
    }
    @Test
    public void three(){
        Assert.assertEquals(1,2);
    }



    @Test
    public void two(){
        System.out.println("two ...");
    }


}
