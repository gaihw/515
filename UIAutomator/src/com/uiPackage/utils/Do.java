package com.uiPackage.utils;

public class Do {
	public static void done(String doName) {
		switch(doName) {
		case "我的": if(ProLoading.mine.contains("com.tbex.trader")) {
			BaseUtils.id(ProLoading.mine).click();
		}else if(ProLoading.mine.contains("//*[@text")) {
			BaseUtils.xpath(ProLoading.mine).click();
		}
		case "请登录": if(ProLoading.pleseaLogin.contains("com.tbex.trader")) {
			BaseUtils.id(ProLoading.pleseaLogin).click();
		}else if(ProLoading.pleseaLogin.contains("//*[@text")) {
			BaseUtils.xpath(ProLoading.pleseaLogin).click();
		}
		case "手机号": if(ProLoading.writePhone.contains("com.tbex.trader")) {
			BaseUtils.id(ProLoading.writePhone).clear();
			BaseUtils.id(ProLoading.writePhone).sendKeys("13020071928");
		}else if(ProLoading.writePhone.contains("//*[@text")) {
			BaseUtils.xpath(ProLoading.writePhone).clear();
			BaseUtils.xpath(ProLoading.writePhone).sendKeys("13020071928");
		}
		case "密码": if(ProLoading.writePassword.contains("com.tbex.trader")) {
			BaseUtils.id(ProLoading.writePassword).clear();
			BaseUtils.id(ProLoading.writePassword).sendKeys("12345678");
		}else if(ProLoading.writePassword.contains("//*[@text")) {
			BaseUtils.xpath(ProLoading.writePassword).clear();
			BaseUtils.xpath(ProLoading.writePassword).sendKeys("12345678");
		}
		case "登录": if(ProLoading.loginButton.contains("com.tbex.trader")) {
			BaseUtils.id(ProLoading.loginButton).click();
		}else if(ProLoading.loginButton.contains("//*[@text")) {
			BaseUtils.xpath(ProLoading.loginButton).click();
		}
	}
	}
}
