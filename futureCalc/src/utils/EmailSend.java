package utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import config.ConfigCenter;

/**
 * 
 * @author Muguozheng
 * @date 2018年10月18日
 * @version 2.0
 * @description 定义了邮件发送的工具类,配置读取自配置中心
 */
public class EmailSend {
	public static void emailSend(String subject, String content) throws FileNotFoundException, IOException {
		// 从配置中心加载配置
		Properties emailProp = ConfigCenter.emailParser();

		// 发件人信息
		String nickName = emailProp.getProperty("nickName");
		// 发件人的 邮箱 和 密码
		String fromAccount = emailProp.getProperty("fromAccount"); // 发件人账号
		// 发件人密码,这里要填smtp授权码
		String fromPassword = emailProp.getProperty("fromPassword");
		// 指定发送邮件的主机
		String smtpHost = emailProp.getProperty("smtpHost"); //
		// 指定收件人
		String recipients = emailProp.getProperty("recipients");
		// 指定抄送人
		String ccList = emailProp.getProperty("ccList");
		// 创建参数配置, 获取系统属性
		Properties properties = System.getProperties();
		properties.setProperty("mail.transport.protocol", "smtp");
		properties.setProperty("mail.smtp.host", smtpHost);
		properties.put("mail.smtp.auth", "true");

		// 根据配置创建会话对象,获取默认session对象
		Session session = Session.getDefaultInstance(properties, new Authenticator() {
			@Override
			public PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(fromAccount, fromPassword); // 发件人邮件用户名、密码
			}
		});
		session.setDebug(false); // true可以显示更多信息

		// 数据源文件目录
		String sourceFilePath = emailProp.getProperty("sourceFilePath");
		// 压缩包目录
		String zipFilePath = emailProp.getProperty("zipFilePath");
		// 压缩包名
		String zipFileName = emailProp.getProperty("zipFileName");
		// 压缩包路径+压缩包名
		String zipPath = ToZip.tozip(sourceFilePath, zipFilePath, zipFileName);

		// 创建邮件
		try {
			// 创建默认的 MimeMessage 对象
			MimeMessage message = new MimeMessage(session);
			// 发送人地址
			InternetAddress fromAddress = new InternetAddress(fromAccount, nickName, "utf-8");
			// 设置发送人
			message.setFrom(fromAddress);
			// 收件人地址:支持发给多个人
			Address[] internetAddressTo = InternetAddress.parse(recipients);
			// 抄送人地址:支持发送给多人
			Address[] internetAddressCC = InternetAddress.parse(ccList);
			// TO:发送 CC:抄送、BCC:暗送
			message.setRecipients(MimeMessage.RecipientType.TO, internetAddressTo);
			message.setRecipients(MimeMessage.RecipientType.CC, internetAddressCC);
			// Set Subject: 头部头字段
			message.setSubject(subject);

			// 创建多重消息
			Multipart multipart = new MimeMultipart();

			// 创建图片节点
			MimeBodyPart image = new MimeBodyPart();
			DataHandler dh = new DataHandler(new FileDataSource(emailProp.getProperty("imagePath"))); // 读取本地文件
			image.setDataHandler(dh); // 将数据添加到节点
			image.setContentID("image_id");// 为“节点”设置一个唯一编号（在文本“节点”将引用该ID）
			multipart.addBodyPart(image);

			// 创建文本节点
			MimeBodyPart text = new MimeBodyPart();
			// 将图片包含到文本内容中
			text.setContent(content.toString() + "这是奖励给你的！！！<br/><img src='cid:image_id'/>", "text/html;charset=UTF-8");
			// 将文本和图片节点结合
			MimeMultipart text_image = new MimeMultipart();
			text_image.addBodyPart(text);
			text_image.addBodyPart(image);
			text_image.setSubType("related"); // 关联关系 有内嵌资源要定义related

			// 将混合节点封装成普通节点BodyPart,邮件最终由多个BodyPart组成
			MimeBodyPart text_image_body = new MimeBodyPart();
			text_image_body.setContent(text_image);
			multipart.addBodyPart(text_image_body);

			// 为邮件添加多个附件
			MimeBodyPart attachment = null;
			File source = new File(sourceFilePath);
			if (!source.exists()) {
				System.out.println(sourceFilePath + " not exists");
				return;
			}

			// 读取配置文件是否发送源文件包
			if (emailProp.getProperty("isSendSourceFile").equals("yes")) {
				if (source.isDirectory()) {
					File[] files = source.listFiles();
					for (File f : files) {
						attachment = new MimeBodyPart();
						String filePath = f.getPath();
						// 根据附件文件创建文件数据源
						DataSource ds = new FileDataSource(filePath);
						attachment.setDataHandler(new DataHandler(ds));
						// 为附件设置文件名
						attachment.setFileName(ds.getName());
						multipart.addBodyPart(attachment);
					}
				} else if (source.isFile()) {
					// 添加源文件
					attachment = new MimeBodyPart();
					// 根据附件文件创建文件数据源
					DataSource ds = new FileDataSource(source.getPath());
					attachment.setDataHandler(new DataHandler(ds));
					// 为附件设置文件名
					attachment.setFileName(ds.getName());
					multipart.addBodyPart(attachment);
				}
			}

			// 读取配置文件 是否发送压缩包
			if (emailProp.getProperty("isSendZip").equals("yes") && source.isDirectory()) {
				// 添加zip附件
				attachment = new MimeBodyPart();
				// 根据附件文件创建文件数据源
				DataSource ds = new FileDataSource(zipPath);
				attachment.setDataHandler(new DataHandler(ds));
				// 为附件设置文件名
				attachment.setFileName(ds.getName());
				multipart.addBodyPart(attachment);
			}

			// 发送完整消息
			message.setContent(multipart);
			// 发送消息
			Transport.send(message);

		} catch (MessagingException mex) {
			mex.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
