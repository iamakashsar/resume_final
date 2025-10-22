package com.example.resume_final.service;

import org.springframework.stereotype.Service;
import sibApi.TransactionalEmailsApi;
import sibModel.*;
import sibApi.ApiClient;
import sibApi.Configuration;
import sibApi.auth.ApiKeyAuth;

import java.util.List;

@Service
public class EmailService {

    // Your Brevo API key (keep it secret in env vars later)
    private final String API_KEY = "xsmtpsib-e268358d44734e2b70f6186646799df65fd4200698510303fb710283dbffb692-ii6YSjZXBWdQ9tPu";

    public void sendOtp(String toEmail, String otp) {
        try {
            // Configure Brevo client
            ApiClient defaultClient = Configuration.getDefaultApiClient();
            ApiKeyAuth apiKey = (ApiKeyAuth) defaultClient.getAuthentication("api-key");
            apiKey.setApiKey(API_KEY);

            TransactionalEmailsApi apiInstance = new TransactionalEmailsApi();

            // Sender details
            SendSmtpEmailSender sender = new SendSmtpEmailSender();
            sender.setEmail("iamakashsar@gmail.com"); // must be verified sender in Brevo
            sender.setName("Resume Ecosystem");

            // Recipient details
            SendSmtpEmailTo recipient = new SendSmtpEmailTo();
            recipient.setEmail(toEmail);

            // Email content
            SendSmtpEmail email = new SendSmtpEmail();
            email.setSender(sender);
            email.setTo(List.of(recipient));
            email.setSubject("Resume Ecosystem Verification Code");
            email.setHtmlContent("<p>Your verification code is: <b>" + otp + "</b></p>");

            // Send email via Brevo API
            apiInstance.sendTransacEmail(email);
            System.out.println("✅ OTP email sent successfully via Brevo API to: " + toEmail);
        } catch (Exception e) {
            System.err.println("❌ Failed to send OTP email: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
