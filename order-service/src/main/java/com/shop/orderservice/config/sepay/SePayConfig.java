package com.shop.orderservice.config.sepay;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "sepay")
@Data
public class SePayConfig {
    private String tokenApi;
    private String webhookToken;
    private String bankCode;
    private String accountNumber;
    private String syntaxPrefix;
}
