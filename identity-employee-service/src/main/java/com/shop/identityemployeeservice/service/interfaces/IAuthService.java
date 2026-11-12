package com.shop.identityemployeeservice.service.interfaces;


import com.shop.identityemployeeservice.cache.UserAuthorizationCache;
import com.shop.identityemployeeservice.dto.request.auth.LoginRequest;

public interface IAuthService {

    String login(LoginRequest request);

    UserAuthorizationCache loadCurrentAuthorization(String userUuid);


}
