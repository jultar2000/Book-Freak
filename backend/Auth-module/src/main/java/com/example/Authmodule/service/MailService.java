package com.example.Authmodule.service;

import com.example.Authmodule.entity.VerificationEmail;
import com.example.Authmodule.exceptions.EmailException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
@Slf4j
public class MailService {

    private final JavaMailSender mailSender;

    private static final String REGEX_EXPRESSION =
            "^(?=.{1,64}@)[A-Za-z0-9_-]" +
            "+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]" +
            "+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";

    @Autowired
    public MailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Async
    void sendMail(VerificationEmail verificationEmail) {
        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setFrom("BookFreak@gmail.com");
            messageHelper.setTo(verificationEmail.getToEmail());
            messageHelper.setSubject(verificationEmail.getSubject());
            messageHelper.setText(verificationEmail.getBody());
        };
        try {
            mailSender.send(messagePreparator);
            log.info("Activation email sent!");
        } catch (MailException e) {
            log.error("Exception occurred when sending mail", e);
            throw new EmailException("Exception when sending mail to " + verificationEmail.getToEmail(), e);
        }
    }

    public boolean validateEmail(String email) {
        return Pattern.compile(REGEX_EXPRESSION)
                .matcher(email)
                .matches();
    }
}
