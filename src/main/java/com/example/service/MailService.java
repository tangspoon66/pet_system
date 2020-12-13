package com.example.service;



/**
 * <pre>
 *     邮件发送业务逻辑接口
 * </pre>
 *
 */
public interface MailService {

    /**
     * 发送邮件
     *
     * @param to      接收者
     * @param subject 主题
     * @param content 内容
     */
    void sendMail(String to, String subject, String content) throws Exception;

}
