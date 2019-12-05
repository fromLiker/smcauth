package com.ibm.fsdsmc.service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class MailService {
	
	private static Logger logger = LoggerFactory.getLogger(MailService.class);
	
    @Autowired
    private JavaMailSender mailSender;
    
    @Autowired
    private JavaMailSender javaMailSender;

    /**
     * a发送简单文本文件
     */
    public void sendSimpleEmail(){
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("liker007@163.com");
            message.setTo("xuexp@cn.ibm.com");
            message.setSubject("hello");
            message.setText("helloha");
            message.setCc("liker007@163.com");
            mailSender.send(message);

        }catch (Exception e){
        	e.printStackTrace();
            System.out.println("发送简单文本文件-发生异常"+e);
        }
    }

    /**
     * a发送html文本
     * @param
     * @throws MessagingException 
     */
    @Async
    public void sendHTMLMail() throws MessagingException{
    	
    	MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);

        helper.setFrom("liker007@163.com");
        helper.setTo("xuexp@cn.ibm.com");
        //helper.setTo(mailBean.getReceiver().split(";"));
        helper.setSubject("hello");
        helper.setText("hi", true);

        javaMailSender.send(mimeMessage);
        
//        try {
//            MimeMessage message= mailSender.createMimeMessage();
//            MimeMessageHelper messageHelper=new MimeMessageHelper(message,true,"utf-8");
//            messageHelper.setFrom("liker007@163.com");
//            messageHelper.setTo("xuexp@cn.ibm.com");
//            messageHelper.setCc("liker007@163.com");
////            addRecipients(Message.RecipienType.CC, InterentAddress.pares(props.getProperty("liker007@163.com")));
//            messageHelper.setSubject("欢迎访问SMC");
//            messageHelper.setText("<a href='http://localhost:8080/smc/users/confirmed/Liker'>please clink to confirm</a>",true);
//
//            mailSender.send(message);
//        }catch (Exception e){
//        	e.printStackTrace();
//        	System.out.println("发送html文本文件-发生异常");
//        	logger.error("邮件发送失败", e.getMessage());
//        }
    }
}