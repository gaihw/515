package com.uiPackage.test;

import org.testng.annotations.Test;

import com.uiPackage.utils.BaseUtils;
import com.uiPackage.utils.homePage.HomePage;
import com.uiPackage.utils.mine.Accounts;
import com.uiPackage.utils.mine.Login;
import com.uiPackage.utils.welcomePage.WelcomePage;

public class HomePageTestCase extends BaseUtils{
  //@Test
  public void test_rankOfIncrease() {
	  HomePage.rankOfIncrease();
  }
  //@Test
  public void test_currencyMarket() {
	  Login.login();
//	  Accounts.transferAccounts("币币账户", "100000");
	  HomePage.currencyMarket("13020071928",1,1,"33333","1");
  }
  @Test
  public void test_swapContract() {
	  WelcomePage.welcomePage();
	  HomePage.swapContract("BSVUSDT");
  }
}
