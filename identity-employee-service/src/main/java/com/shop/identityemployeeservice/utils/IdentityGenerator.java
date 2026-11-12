package com.shop.identityemployeeservice.utils;

import com.shop.identityemployeeservice.repository.EmployeeAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.text.Normalizer;
import java.util.Locale;

@Component
@RequiredArgsConstructor
public class IdentityGenerator {
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuvwxyz";

    private final SecureRandom random = new SecureRandom();
    private final EmployeeAccountRepository employeeAccountRepository;

    public String generatePassword(int length){
        StringBuilder password = new StringBuilder(length);

        for (int i = 0; i < 8; i++) {
            int index = random.nextInt(CHARACTERS.length());
            password.append(CHARACTERS.charAt(index));
        }

        return password.toString();
    }

    private static String normalize(String value) {
        String normalized = Normalizer.normalize(
                value.toLowerCase(Locale.ROOT)
                        .replace('đ', 'd'),
                Normalizer.Form.NFD
        );

        return normalized
                .replaceAll("\\p{M}+", "")
                .replaceAll("[^a-z0-9]", "");
    }

    private String generateUsername(String fullName) {

        if (fullName == null || fullName.isBlank()) {
            throw new IllegalArgumentException(
                    "Họ và tên không được để trống"
            );
        }

        String[] parts = fullName.trim().split("\\s+");

        if (parts.length < 2) {
            throw new IllegalArgumentException(
                    "Họ và tên phải có ít nhất 2 từ"
            );
        }

        String lastName = normalize(parts[parts.length - 1]);

        StringBuilder initials = new StringBuilder();

        for (int i = 0; i < parts.length - 1; i++) {
            initials.append(normalize(parts[i]).charAt(0));
        }

        return lastName + "." + initials;
    }

    public String generateUniqueUsername(String fullName) {

        String baseUsername = this.generateUsername(fullName);

        String username = baseUsername;
        int suffix = 1;

        while (employeeAccountRepository.existsByUsername(username)) {
            username = baseUsername + suffix;
            suffix++;
        }

        return username;
    }
}
