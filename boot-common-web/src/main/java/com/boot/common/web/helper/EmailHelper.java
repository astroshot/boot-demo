package com.boot.common.web.helper;

import com.boot.common.web.model.MailInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.activation.DataSource;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailHelper {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    protected JavaMailSender mailSender;

    protected MailInfo mailInfo;

    public EmailHelper(JavaMailSender mailSender, MailInfo mailInfo) {
        this.mailSender = mailSender;
        this.mailInfo = mailInfo;
    }

    public void send(String content, String attachFileName, DataSource source) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true, "GBK");
        messageHelper.setFrom(mailInfo.getFrom());
        messageHelper.setTo(InternetAddress.parse(mailInfo.getTo()));
        messageHelper.setCc(InternetAddress.parse(mailInfo.getCc()));
        messageHelper.setSubject(mailInfo.getTitle());
        messageHelper.setText(content, true);
        if (source != null) {
            messageHelper.addAttachment(attachFileName, source);
        }
        mimeMessage = messageHelper.getMimeMessage();
        mailSender.send(mimeMessage);
        logger.info("邮件 (from: {}, to: {}, title: {}) 发送成功 ... ", mailInfo.getFrom(), mailInfo.getTo(),
                mailInfo.getTitle());
    }
}
