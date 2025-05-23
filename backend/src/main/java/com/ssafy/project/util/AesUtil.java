package com.ssafy.project.util;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.InputStream;
import java.util.Base64;

@Component
public class AesUtil {
    @Value("${aes.key.path}")
    private String keyPath;

    private SecretKeySpec secretKey;

    @PostConstruct
    public void init() {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(keyPath)) {
            if (is == null) {
                throw new IllegalStateException("❌ 리소스 경로에서 AES 키 파일을 찾을 수 없습니다: " + keyPath);
            }

            String base64Key = new String(is.readAllBytes()).trim();
            byte[] keyBytes = Base64.getDecoder().decode(base64Key);
            this.secretKey = new SecretKeySpec(keyBytes, "AES");
            System.out.println("✅ AES 키 초기화 완료 (리소스 로드)");
        } catch (Exception e) {
            throw new IllegalStateException("AES 키 초기화 실패", e);
        }
    }

    public String encrypt(Double doubleText) {
        return encrypt(String.valueOf(doubleText));
    }
    public String encrypt(String plainText) {
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] encrypted = cipher.doFinal(plainText.getBytes());
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception e) {
            throw new RuntimeException("AES 암호화 실패", e);
        }
    }

    public String decrypt(String encryptedText) {
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] decoded = Base64.getDecoder().decode(encryptedText);
            byte[] decrypted = cipher.doFinal(decoded);
            return new String(decrypted);
        } catch (Exception e) {
            throw new RuntimeException("AES 복호화 실패", e);
        }
    }
}
