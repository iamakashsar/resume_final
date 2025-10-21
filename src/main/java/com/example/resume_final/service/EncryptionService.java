package com.example.resume_final.service;



import org.springframework.stereotype.Service;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Service
public class EncryptionService {
    private static final String KEY = "MySecretKey12345";

    public String encrypt(String str) {
        try {
            SecretKeySpec skeySpec = new SecretKeySpec(KEY.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
            byte[] encrypted = cipher.doFinal(str.getBytes());
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception e) { throw new RuntimeException(e); }
    }

    public String decrypt(String str) {
        try {
            SecretKeySpec skeySpec = new SecretKeySpec(KEY.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
            byte[] decoded = Base64.getDecoder().decode(str);
            return new String(cipher.doFinal(decoded));
        } catch (Exception e) { throw new RuntimeException(e); }
    }
}
