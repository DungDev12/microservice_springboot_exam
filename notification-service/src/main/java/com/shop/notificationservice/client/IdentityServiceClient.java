package com.shop.notificationservice.client;


import com.shop.notificationservice.dto.response.IdentityApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class IdentityServiceClient {

    private final RestTemplate restTemplate;

    public String getEmailUserByUuid(UUID customerUuid) {
        // Gọi bằng TÊN SERVICE thay vì IP. Eureka + LoadBalancer sẽ tự đổi thành IP thực tế
        String url = "http://localhost:8081/api/accounts/" + customerUuid.toString();
        try {
            IdentityApiResponse apiResponse = restTemplate.getForObject(url, IdentityApiResponse.class);
            if (apiResponse != null && apiResponse.data() != null) {
                return apiResponse.data().email(); // Trả về "dungdev12@gmail.com"
            }
            return null;
        }
         catch (Exception e) {
            System.err.println("❌ Lỗi không thể lấy dữ liệu từ IDENT-SERVICE qua Eureka: " + e.getMessage());
            return null;
        }
    }
}
