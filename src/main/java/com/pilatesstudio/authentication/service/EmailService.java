package com.pilatesstudio.authentication.service;

import com.pilatesstudio.common.exception.BusinessException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.springframework.core.io.ClassPathResource;

import java.io.UnsupportedEncodingException;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    @Value("${mail.from}")
    private String from;

    @Value("${mail.from-name}")
    private String fromName;

    @Value("${app.frontend-url}")
    private String frontendUrl;

    public void sendInitialPasswordEmail(
            String email,
            String firstName,
            String lastName,
            String token
    ) {

        try {
            Context context = new Context();

            context.setVariable("firstName", firstName);
            context.setVariable("lastName", lastName);
            context.setVariable(
                    "activationUrl",
                    frontendUrl
                            + "/reset-password?token="
                            + token
                            + "&type=initial"
            );

            String html = templateEngine.process(
                    "email/initial-password",
                    context
            );

            MimeMessage message = mailSender.createMimeMessage();

            MimeMessageHelper helper =
                    new MimeMessageHelper(
                            message,
                            MimeMessageHelper.MULTIPART_MODE_RELATED,
                            "UTF-8"
                    );

            helper.setFrom(from, fromName);
            helper.setTo(email);

            ClassPathResource logo =
                    new ClassPathResource(
                            "static/images/denge-pilates-logo.png"
                    );

            if (!logo.exists()) {
                throw new BusinessException(
                        "Denge Pilates logo not found"
                );
            }

            helper.addInline(
                    "denge-pilates-logo",
                    logo
            );

            helper.setSubject(
                    "Denge Pilates - Hesabınızı Aktifleştirin"
            );
            helper.setText(html, true);

            mailSender.send(message);

        } catch (
                MessagingException |
                UnsupportedEncodingException e
        ) {
            throw new BusinessException(
                    "Initial password email could not be sent"
            );
        }
    }

    public void sendForgotPasswordEmail(
            String email,
            String token
    ) {
        try {
            Context context = new Context();

            context.setVariable(
                    "resetUrl",
                    frontendUrl
                            + "/reset-password?token="
                            + token
                            + "&type=reset"
            );

            String html = templateEngine.process(
                    "email/forgot-password",
                    context
            );

            MimeMessage message = mailSender.createMimeMessage();

            MimeMessageHelper helper =
                    new MimeMessageHelper(
                            message,
                            MimeMessageHelper.MULTIPART_MODE_RELATED,
                            "UTF-8"
                    );

            helper.setFrom(from, fromName);
            helper.setTo(email);

            ClassPathResource logo =
                    new ClassPathResource(
                            "static/images/denge-pilates-logo.png"
                    );

            if (!logo.exists()) {
                throw new BusinessException(
                        "Denge Pilates logo not found"
                );
            }

            helper.addInline(
                    "denge-pilates-logo",
                    logo
            );

            helper.setSubject(
                    "Denge Pilates - Şifre Yenileme"
            );

            helper.setText(html, true);

            mailSender.send(message);

        } catch (
                MessagingException |
                UnsupportedEncodingException e
        ) {
            throw new BusinessException(
                    "Forgot password email could not be sent"
            );
        }
    }
}