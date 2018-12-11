package com.nhb.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;

@Component
public class EmailUtil {
    //注入邮件发送者
    @Autowired
    private JavaMailSender mailSender;
    //注入发送者用户
    @Value("${spring.mail.username}")
    private String fromUser;

    /**
     * @param subject 邮件主题
     * @param toUser  邮件接收者
     * @param content 邮件内容
     */
    public void sendSimpleMail(String subject, String toUser, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromUser);
        message.setTo(toUser);
        message.setSubject(subject);
        message.setText(content);
        mailSender.send(message);
    }

    /**
     *
     * @param toUser 邮件接收者
     * @param code   激活码
     */
    public void sendCodeMail(String toUser, String code) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper mailHelper = new MimeMessageHelper(message,true);
            mailHelper.setFrom(fromUser);
            mailHelper.setTo(toUser);
            mailHelper.setSubject("跳蚤市场 激活码");
            String content = "<html>\n" +
                    "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />"+
                    "              <body>\n" +
                    "                   <h1>此邮件用来激活账户，非本人注册请勿点击</h1>\n" +
                                            "<a href=\"http://localhost/index.html\" style='color:red'>"+code+"</a>"+
                    "               </body>\n" +
                    "          </html>";
            mailHelper.setText(content,true);
            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param toUser   邮件接收者
     * @param subject  主题
     * @param content  邮件内容
     * @param path     附加文件路径
     * @param newName  附件新名称
     */
    public void sendAttachmentsMail(String toUser,String subject,String content,String path,String newName){
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(message,true);
            messageHelper.setFrom(fromUser);
            messageHelper.setTo(toUser);
            messageHelper.setSubject(subject);
            messageHelper.setText(content);
            //注意项目路径问题，自动补用项目路径
            FileSystemResource file = new FileSystemResource(new File(path));
            //加入邮件
            messageHelper.addAttachment(newName, file);
            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }

    }
}
