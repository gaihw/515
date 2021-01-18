package com.test.cases;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class demo {
    public static void main(String[] args) {

    }

    @BeforeClass
    public void b(){
        System.out.println("b ...");
    }
    @Test
    public void one(){
        System.out.println("one ...");
    }
    @Test
    public void three(){
        Assert.assertEquals(1,1);
    }



    @Test
    public void two(){
        System.out.println("two ...");
    }


}
