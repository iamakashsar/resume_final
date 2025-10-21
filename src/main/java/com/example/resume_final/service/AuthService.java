package com.example.resume_final.service;

import com.example.resume_final.entity.OtpVerification;
import com.example.resume_final.entity.User;
import com.example.resume_final.repository.OtpRepository;
import com.example.resume_final.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
public class AuthService {

    @Autowired private UserRepository userRepo;
    @Autowired private OtpRepository otpRepo;
    @Autowired private EmailService emailService;
    @Autowired private EncryptionService encryptionService;
    @Autowired private JwtService jwtService;

    // ---------- SEND OTP ----------
    public void sendOtp(String email) {
        String otp = String.valueOf(100000 + new Random().nextInt(900000));

        OtpVerification otpEntity = otpRepo.findByEmail(email).orElse(new OtpVerification());
        otpEntity.setEmail(email);
        otpEntity.setOtp(otp);
        otpEntity.setCreatedAt(LocalDateTime.now());

        otpRepo.save(otpEntity);

        emailService.sendOtp(email, otp);
    }

    // ---------- REGISTER ----------
    public String register(String email, String name, String password, String otp) {
        Optional<OtpVerification> otpData = otpRepo.findByEmail(email);

        if (otpData.isEmpty()) {
            return "OTP not found for this email";
        }

        OtpVerification otpRecord = otpData.get();
        if (!otpRecord.getOtp().equals(otp)) {
            return "Invalid OTP";
        }

        // encrypt password
        String encryptedPass = encryptionService.encrypt(password);

        // create user
        User user = new User();
        user.setEmail(email);
        user.setName(name);
        user.setPassword(encryptedPass);
        user.setVerified(true);

        userRepo.save(user);

        // remove OTP record (optional cleanup)
        otpRepo.delete(otpRecord);

        // return JWT
        return jwtService.generateToken(email);
    }

    // ---------- LOGIN ----------
    public String login(String email, String password) {
        User user = userRepo.findByEmail(email).orElse(null);
        if (user == null) return "User not found";

        String decrypted = encryptionService.decrypt(user.getPassword());
        if (!decrypted.equals(password)) return "Invalid credentials";

        return jwtService.generateToken(email);
    }
}
