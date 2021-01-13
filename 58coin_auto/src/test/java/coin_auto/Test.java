package coin_auto;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.Random;

public class Test {
     public static final String FROM = "13020071928@163.com";//发件人的email
        public static final String PWD = "6666666";//发件人密码--邮箱密码
        public static final int TIMELIMIT = 1000 * 60 * 60 * 24; //邮件过期时间24小时
        public static final String TITLE = "*********平台!";
        public static final String HOST = "smtp.163.com";
        public static final String SMTP = "smtp";
        public static Transport transport;

        /**
         * 邮件处理
         * @param toEmail  邮箱
         * @return
         */
        public static String sendEmail(String toEmail) throws AddressException, MessagingException {
            Properties props = new Properties(); //可以加载一个配置文件
            // 使用smtp：简单邮件传输协议
            props.put("mail.smtp.host", HOST);//存储发送邮件服务器的信息
            props.put("mail.smtp.auth", "true");//同时通过验证
            props.put("mail.user", FROM);//用户
            props.put("mail.password", PWD);//密码
            props.put("mail.smtp.port", "25");//你开启pop3/smtp时的验证码
            props.put("mail.smtp.starttls.enable", "true"); //开启tls
            Session session = Session.getInstance(props);//根据属性新建一个邮件会话
            //session.setDebug(true); //有他会打印一些调试信息。
            String code = getNumber();
            MimeMessage message = new MimeMessage(session);// 由邮件会话新建一个消息对象
            message.setFrom(new InternetAddress(FROM));// 设置发件人的地址
            message.setRecipient(Message.RecipientType.TO,
                    new InternetAddress(toEmail));// 设置收件人,并设置其接收类型为TO
            message.setSubject(TITLE);// 设置标题
            // 设置信件内容
            // message.setText(mailContent); //发送 纯文本 邮件 todo
            Date date = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日");
            String dateString = formatter.format(date);
            message.setContent("你已经请求了重置密码,验证码为:" + "<span style ='color:red'>"
                            + code + "</span>"
                            + "<br/><span style = 'font-size:12px;color:#C0C0C0'>(为保障您账号的安全性,请您在5分钟内完成重置。)"
                            + "</span><br/>如果你没有请求重置密码,请忽略这份邮件<br/><br/><br/><br/><br/>"
                            + "<br/>*********某某平台<br/>" + dateString,
                    "text/html;charset=gbk"); // 发送HTML邮件，内容样式比较丰富
            message.setSentDate(new Date());// 设置发信时间
            message.saveChanges();// 存储邮件信息
            // 发送邮件
            // Transport transport = session.getTransport();
            if (transport == null) {
                transport = session.getTransport(SMTP);
            }
            if (!transport.isConnected()) {
                transport.connect(FROM, PWD);
            }
            transport.sendMessage(message, message.getAllRecipients());// 发送邮件,其中第二个参数是所有已设好的收件人地址
            transport.close();
            return code;
        }


        /**
         * 得到验证码
         * @return
         */
        public static String getNumber() {
            String number = "";
            String chars = "abcdefghijkl0123456789ABCDEFGHIJKLMNOPQLSTUVWXYZ";
            Random r = new Random();
            for (int i = 0; i < 4; ++i) {
                number = number + chars.charAt(r.nextInt(chars.length()));
            }
            return number;
        }
}
