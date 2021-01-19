package test;

import org.databene.feed4testng.FeedTest;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class NewTest extends FeedTest{
  @BeforeClass
  public void f() {
	  System.out.println("@BeforeClass");
  }
  @AfterClass
  public void g() {
	  System.out.println("@AfterClass");
  }
}
