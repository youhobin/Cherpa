package com.cerpha.cerphaproject.common.converter;

import com.cerpha.cerphaproject.common.encryption.AESEncryption;
import jakarta.persistence.AttributeConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EncryptionConverter implements AttributeConverter<String, String> {

    private final AESEncryption aesEncryption;

    @Override
    public String convertToDatabaseColumn(String attribute) {
        try {
            return aesEncryption.encrypt(attribute);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        try {
            return aesEncryption.decrypt(dbData);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
