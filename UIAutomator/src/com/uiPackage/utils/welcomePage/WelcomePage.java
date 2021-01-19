package com.uiPackage.utils.welcomePage;

import com.uiPackage.utils.BaseUtils;

public class WelcomePage {
	public static void welcomePage() {
		BaseUtils.sleep(20);
		if(BaseUtils.xpathBoolean("//android.support.v4.view.ViewPager/android.widget.RelativeLayout/android.widget.Button")) {
			BaseUtils.xpath("//android.support.v4.view.ViewPager/android.widget.RelativeLayout/android.widget.Button").click();
			if(BaseUtils.idBoolean("com.android.packageinstaller:id/permission_allow_button")) {				
				BaseUtils.id("com.android.packageinstaller:id/permission_allow_button").click();
			}			
			System.out.println("已跳过欢迎页面...");
		}
	}
}
