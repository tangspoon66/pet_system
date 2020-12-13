package com.example.service.impl;

import com.example.service.MailService;
import com.example.util.EmailUtils;
import com.sun.mail.smtp.SMTPAddressFailedException;
import io.github.biezhi.ome.OhMyEmail;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


/**
 * <pre>
 *     邮件发送业务逻辑实现类
 * </pre>

 */
@Service
public class MailServiceImpl implements MailService {

    @Value("${smtp.host}")
    private String SMTP_HOST;

    @Value("${smtp.username}")
    private String SMTP_USERNAME;

    @Value("${smtp.password}")
    private String SMTP_PASSWORD;

        @Value("${smtp.from}")
    private String SMTP_FROM;


    /**
     * 发送邮件
     *
     * @param to      to 接收者
     * @param subject subject 标题
     * @param content content 内容
     */
    @Override
    public void sendMail(String to, String subject, String content) throws Exception {
        //配置邮件服务器

        EmailUtils.configMail(SMTP_HOST, SMTP_USERNAME, SMTP_PASSWORD);

        try {
            OhMyEmail.subject(subject)
                    .from(SMTP_FROM)
                    .to(to)
                    .html(content)
                    .send();
        } catch (SMTPAddressFailedException e) {
            System.out.println("部分邮件不存在");
        }

    }

}
