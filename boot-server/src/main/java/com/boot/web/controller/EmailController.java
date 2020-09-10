package com.boot.web.controller;

import com.boot.common.helper.ExcelHelper;
import com.boot.common.web.constant.Constants;
import com.boot.common.web.controller.BaseController;
import com.boot.common.web.helper.EmailHelper;
import com.boot.common.web.model.MailInfo;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.util.ByteArrayDataSource;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/email")
public class EmailController extends BaseController {

    protected EmailHelper emailHelper;

    @Resource
    protected JavaMailSender mailSender;

    @Resource
    protected MailInfo mailInfo;

    @PostConstruct
    public void init () {
        emailHelper = new EmailHelper(mailSender, mailInfo);
    }

    @GetMapping("/test")
    public String testSendMail() throws IOException, MessagingException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        String[] titles = {"id", "name", "score"};
        List<String[]> data = new ArrayList<>();
        data.add(new String[]{"1", "Jack", "65"});
        data.add(new String[]{"2", "Tim", "77"});
        ExcelHelper.generateSimpleExcel("Sheet1", titles, data, os);

        InputStream is = new ByteArrayInputStream(os.toByteArray());
        ByteArrayDataSource source = new ByteArrayDataSource(is, Constants.EXCEL_2003);
        String content = "This mail is sent by Spring boot";
        emailHelper.send(content, "成绩单.xls", source);
        return "ok";
    }
}
