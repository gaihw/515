package com.test.cases;

import org.testng.Assert;
import org.testng.annotations.Test;

public class demo {
    public static void main(String[] args) {

    }

    @Test
    public void one(){
        System.out.println("one ...");
    }

    @Test
    public void two(){
        System.out.println("two ...");
    }

    @Test
    public void three(){
        Assert.assertEquals(1,2);
    }
}
