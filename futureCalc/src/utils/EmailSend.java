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
 * @date 2018��10��18��
 * @version 2.0
 * @description �������ʼ����͵Ĺ�����,���ö�ȡ����������
 */
public class EmailSend {
	public static void emailSend(String subject, String content) throws FileNotFoundException, IOException {
		// ���������ļ�������
		Properties emailProp = ConfigCenter.emailParser();

		// ��������Ϣ
		String nickName = emailProp.getProperty("nickName");
		// �����˵� ���� �� ����
		String fromAccount = emailProp.getProperty("fromAccount"); // �������˺�
		// ����������,����Ҫ��smtp��Ȩ��
		String fromPassword = emailProp.getProperty("fromPassword");
		// ָ�������ʼ�������
		String smtpHost = emailProp.getProperty("smtpHost"); //
		// ָ���ռ���
		String recipients = emailProp.getProperty("recipients");
		// ָ��������
		String ccList = emailProp.getProperty("ccList");
		// ������������, ��ȡϵͳ����
		Properties properties = System.getProperties();
		properties.setProperty("mail.transport.protocol", "smtp");
		properties.setProperty("mail.smtp.host", smtpHost);
		properties.put("mail.smtp.auth", "true");

		// �������ô����Ự����,��ȡĬ��session����
		Session session = Session.getDefaultInstance(properties, new Authenticator() {
			@Override
			public PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(fromAccount, fromPassword); // �������ʼ��û���������
			}
		});
		session.setDebug(false); // true������ʾ������Ϣ

		// ����Դ�ļ�Ŀ¼
		String sourceFilePath = emailProp.getProperty("sourceFilePath");
		// ѹ����Ŀ¼
		String zipFilePath = emailProp.getProperty("zipFilePath");
		// ѹ������
		String zipFileName = emailProp.getProperty("zipFileName");
		// ѹ����·��+ѹ������
		String zipPath = ToZip.tozip(sourceFilePath, zipFilePath, zipFileName);

		// �����ʼ�
		try {
			// ����Ĭ�ϵ� MimeMessage ����
			MimeMessage message = new MimeMessage(session);
			// �����˵�ַ
			InternetAddress fromAddress = new InternetAddress(fromAccount, nickName, "utf-8");
			// ���÷�����
			message.setFrom(fromAddress);
			// �ռ��˵�ַ:֧�ַ��������
			Address[] internetAddressTo = InternetAddress.parse(recipients);
			// �����˵�ַ:֧�ַ��͸�����
			Address[] internetAddressCC = InternetAddress.parse(ccList);
			// TO:���� CC:���͡�BCC:����
			message.setRecipients(MimeMessage.RecipientType.TO, internetAddressTo);
			message.setRecipients(MimeMessage.RecipientType.CC, internetAddressCC);
			// Set Subject: ͷ��ͷ�ֶ�
			message.setSubject(subject);

			// ����������Ϣ
			Multipart multipart = new MimeMultipart();

			// ����ͼƬ�ڵ�
			MimeBodyPart image = new MimeBodyPart();
			DataHandler dh = new DataHandler(new FileDataSource(emailProp.getProperty("imagePath"))); // ��ȡ�����ļ�
			image.setDataHandler(dh); // ��������ӵ��ڵ�
			image.setContentID("image_id");// Ϊ���ڵ㡱����һ��Ψһ��ţ����ı����ڵ㡱�����ø�ID��
			multipart.addBodyPart(image);

			// �����ı��ڵ�
			MimeBodyPart text = new MimeBodyPart();
			// ��ͼƬ�������ı�������
			text.setContent(content.toString() + "���ǽ�������ģ�����<br/><img src='cid:image_id'/>", "text/html;charset=UTF-8");
			// ���ı���ͼƬ�ڵ���
			MimeMultipart text_image = new MimeMultipart();
			text_image.addBodyPart(text);
			text_image.addBodyPart(image);
			text_image.setSubType("related"); // ������ϵ ����Ƕ��ԴҪ����related

			// ����Ͻڵ��װ����ͨ�ڵ�BodyPart,�ʼ������ɶ��BodyPart���
			MimeBodyPart text_image_body = new MimeBodyPart();
			text_image_body.setContent(text_image);
			multipart.addBodyPart(text_image_body);

			// Ϊ�ʼ���Ӷ������
			MimeBodyPart attachment = null;
			File source = new File(sourceFilePath);
			if (!source.exists()) {
				System.out.println(sourceFilePath + " not exists");
				return;
			}

			// ��ȡ�����ļ��Ƿ���Դ�ļ���
			if (emailProp.getProperty("isSendSourceFile").equals("yes")) {
				if (source.isDirectory()) {
					File[] files = source.listFiles();
					for (File f : files) {
						attachment = new MimeBodyPart();
						String filePath = f.getPath();
						// ���ݸ����ļ������ļ�����Դ
						DataSource ds = new FileDataSource(filePath);
						attachment.setDataHandler(new DataHandler(ds));
						// Ϊ���������ļ���
						attachment.setFileName(ds.getName());
						multipart.addBodyPart(attachment);
					}
				} else if (source.isFile()) {
					// ���Դ�ļ�
					attachment = new MimeBodyPart();
					// ���ݸ����ļ������ļ�����Դ
					DataSource ds = new FileDataSource(source.getPath());
					attachment.setDataHandler(new DataHandler(ds));
					// Ϊ���������ļ���
					attachment.setFileName(ds.getName());
					multipart.addBodyPart(attachment);
				}
			}

			// ��ȡ�����ļ� �Ƿ���ѹ����
			if (emailProp.getProperty("isSendZip").equals("yes") && source.isDirectory()) {
				// ���zip����
				attachment = new MimeBodyPart();
				// ���ݸ����ļ������ļ�����Դ
				DataSource ds = new FileDataSource(zipPath);
				attachment.setDataHandler(new DataHandler(ds));
				// Ϊ���������ļ���
				attachment.setFileName(ds.getName());
				multipart.addBodyPart(attachment);
			}

			// ����������Ϣ
			message.setContent(multipart);
			// ������Ϣ
			Transport.send(message);

		} catch (MessagingException mex) {
			mex.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
