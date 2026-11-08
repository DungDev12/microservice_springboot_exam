package com.shop.orderservice.utils;

import com.shop.orderservice.config.sepay.SePayConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class OrderCodeGenerator {

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuvwxyz";

    private static final int CODE_LENGTH = 8;

    private final SecureRandom random = new SecureRandom();

    private final SePayConfig sePayConfig;

    public String generate(){
        StringBuilder code = new StringBuilder(CODE_LENGTH);

        for (int i = 0; i < CODE_LENGTH; i++) {
            int index = random.nextInt(CHARACTERS.length());
            code.append(CHARACTERS.charAt(index));
        }

        return String.format("%s%s", sePayConfig.getSyntaxPrefix(), code.toString());
    }
}
