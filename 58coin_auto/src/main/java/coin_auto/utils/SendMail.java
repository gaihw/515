package coin_auto.utils;

import com.sun.mail.util.MailSSLSocketFactory;
import org.springframework.stereotype.Component;

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

@Component
public class SendMail {
	private static String sender = "gai";
	private static String subject = "58coin-Android-autoTest";
	private static String content = "邮件自动化";
	private static String receiverList = "hongweigai@pyg168.com,568228579@qq.com";
	private static String account = "13020071928@163.com";// 登录账户
	private static String password = "MUQSAOSTXITFJCUE";// 登录密码
	private static String host = "smtp.163.com";// 服务器地址
	private static String port = "465";// 端口
	private static String protocol = "smtp";// 协议

	//初始化参数
	public Session initProperties() {
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
	public  void send() {
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

}
