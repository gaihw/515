package com.test.utils;

import org.databene.feed4testng.FeedTest;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

public class BaseUtils extends FeedTest {
	public static int start ;
	public static int end ;

    @BeforeClass
    public void setUp(){

    }
   @AfterClass
    public void tearDown(){

    }
    public static long time() {
    	return System.currentTimeMillis();
    }

}