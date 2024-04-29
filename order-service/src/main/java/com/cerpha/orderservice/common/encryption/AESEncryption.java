package com.cerpha.orderservice.common.encryption;

import com.cerpha.orderservice.common.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;

import static com.cerpha.orderservice.common.exception.ExceptionCode.AES_ENCRYPTION_ERROR;

@Slf4j
@Component
public class AESEncryption {

    private static final String ALGORITHM = "AES";

    @Value("${env.encryption.key}")
    private String secretKey;

    public String encrypt(String plainText) {
        try {
            Key key = generateKey();
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] encryptedBytes = cipher.doFinal(plainText.getBytes());
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            throw new BusinessException(AES_ENCRYPTION_ERROR);
        }
    }

    public String decrypt(String encryptedText) {
        try {
            Key key = generateKey();
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedText));
            return new String(decryptedBytes);
        } catch (Exception e) {
            throw new BusinessException(AES_ENCRYPTION_ERROR);
        }
    }

    private Key generateKey() {
        return new SecretKeySpec(secretKey.getBytes(), ALGORITHM);
    }

}
