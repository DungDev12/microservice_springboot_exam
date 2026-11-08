package com.shop.orderservice.service;

import com.shop.orderservice.config.sepay.SePayConfig;
import com.shop.orderservice.dto.response.SePayPaymentResponse;
import com.shop.orderservice.service.interfaces.ISePayService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class SePayService implements ISePayService {

    private final SePayConfig sepayConfig;

    @Override
    public SePayPaymentResponse createCheckoutUrl(BigDecimal amount, String orderCode) {
        String baseQrUrl = String.format("https://vietqr.app/img");

        String finalQrUrl = UriComponentsBuilder.fromUriString(baseQrUrl)
                .queryParam("amount", amount.longValue())
                .queryParam("acc", sepayConfig.getAccountNumber())
                .queryParam("bank",sepayConfig.getBankCode())
                .queryParam("des", orderCode)
                .queryParam("template", "standee")
                .queryParam("showinfo",true)
                .toUriString();

        long defaultTimeoutSeconds = 300;
        Instant now = Instant.now();
        Instant expiredAt = now.plus(6, ChronoUnit.MINUTES);

        return new SePayPaymentResponse(
                orderCode,
                amount,
                finalQrUrl,
                defaultTimeoutSeconds,
                now,
                expiredAt
        );
    }
}
