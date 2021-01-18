package com.test.utils;

import com.sun.mail.util.MailSSLSocketFactory;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.util.Date;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

public class SendMail {
	private static ResourceBundle bundle= ResourceBundle.getBundle("application", Locale.CHINA);
	private static String sender;
	private static String subject;
	private static String content;
	private static String receiverList;
	private static String account ;// 登录账户
	private static String password ;// 登录密码
	private static String host ;// 服务器地址
	private static String port ;// 端口
	private static String protocol ;// 协议
	
	static {
		try {
			sender = new String(bundle.getString("sender").getBytes("ISO-8859-1"),"UTF-8");
			content = new String(bundle.getString("content").getBytes("ISO-8859-1"),"UTF-8");
			subject = new String(bundle.getString("subject").getBytes("ISO-8859-1"),"UTF-8");
			receiverList = new String(bundle.getString("receiverList").getBytes("ISO-8859-1"),"UTF-8");
			account = new String(bundle.getString("account").getBytes("ISO-8859-1"),"UTF-8");
			password = new String(bundle.getString("password").getBytes("ISO-8859-1"),"UTF-8");
			host = new String(bundle.getString("host").getBytes("ISO-8859-1"),"UTF-8");
			protocol = new String(bundle.getString("protocol").getBytes("ISO-8859-1"),"UTF-8");
			port = new String(bundle.getString("port").getBytes("ISO-8859-1"),"UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	//初始化参数
	public static Session initProperties() {
	    Properties properties = new Properties();
	    properties.setProperty("mail.transport.protocol", protocol);
	    properties.setProperty("mail.smtp.host", host);
	    properties.setProperty("mail.smtp.port", port);
	    // 使用smtp身份验证
	    properties.put("mail.smtp.auth", "true");
	    // 使用SSL,企业邮箱必需 start
	    // 开启安全协议
	    MailSSLSocketFactory mailSSLSocketFactory = null;
	    try {
	        mailSSLSocketFactory = new MailSSLSocketFactory();
	        mailSSLSocketFactory.setTrustAllHosts(true);
	    } catch (GeneralSecurityException e) {
	        e.printStackTrace();
	    }
	    properties.put("mail.smtp.enable", "true");
	    properties.put("mail.smtp.ssl.socketFactory", mailSSLSocketFactory);
	    properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
	    properties.put("mail.smtp.socketFactory.fallback", "false");
	    properties.put("mail.smtp.socketFactory.port", port);
	    Session session = Session.getDefaultInstance(properties, new Authenticator() {
	        @Override
	        protected PasswordAuthentication getPasswordAuthentication() {
	            return new PasswordAuthentication(account, password);
	        }
	    });
	    // 使用SSL,企业邮箱必需 end
	    //显示debug信息 正式环境注释掉
//	    session.setDebug(true);
	    return session;
	}
	 
	// @param sender 发件人别名
	// @param subject 邮件主题
	//@param content 邮件内容
	//@param receiverList 接收者列表,多个接收者之间用","隔开
	public static void send() {
	    try {
	        Session session = initProperties();
	        MimeMessage mimeMessage = new MimeMessage(session);
	        mimeMessage.setFrom(new InternetAddress(account, sender));// 发件人,可以设置发件人的别名
	        // 收件人,多人接收
	        InternetAddress[] internetAddressTo = new InternetAddress().parse(receiverList);
	        mimeMessage.setRecipients(Message.RecipientType.TO, internetAddressTo);
	        // 主题
	        mimeMessage.setSubject(subject);
	        // 时间
	        mimeMessage.setSentDate(new Date());
	        // 容器类 附件
	        MimeMultipart mimeMultipart = new MimeMultipart();
	        // 可以包装文本,图片,附件
	        MimeBodyPart bodyPart = new MimeBodyPart();
	        // 设置内容
	        bodyPart.setContent(content, "text/html; charset=UTF-8");
	        mimeMultipart.addBodyPart(bodyPart);
	        mimeMessage.setContent(mimeMultipart);
	        mimeMessage.saveChanges();
	        Transport.send(mimeMessage);
	    } catch (MessagingException e) {
	        e.printStackTrace();
	    } catch (UnsupportedEncodingException e) {
	        e.printStackTrace();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}

	public static void main(String[] args) {
		send();
	}
}
