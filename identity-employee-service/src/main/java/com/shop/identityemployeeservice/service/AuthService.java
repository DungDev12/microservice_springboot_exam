package com.shop.identityemployeeservice.service;

import com.shop.identityemployeeservice.config.jwt.JwtProvider;
import com.shop.identityemployeeservice.dto.request.auth.LoginRequest;
import com.shop.identityemployeeservice.entity.EmployeeAccount;
import com.shop.identityemployeeservice.exception.AccessDeniedException;
import com.shop.identityemployeeservice.exception.NotFoundException;
import com.shop.identityemployeeservice.repository.EmployeeAccountRepository;
import com.shop.identityemployeeservice.service.interfaces.IAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService implements IAuthService {

    private final EmployeeAccountRepository employeeAccountRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    @Override
    public String login(LoginRequest request) {
        EmployeeAccount employeeAccount = employeeAccountRepository.findByUsername(request.username())
                .orElseThrow(() -> new NotFoundException("Tài khoản không tồn tại"));

        if(!passwordEncoder.matches( request.password(),employeeAccount.getPasswordHash())){
            throw new AccessDeniedException("Mật khẩu không khớp", HttpStatus.FORBIDDEN);
        }
        return jwtProvider.generateToken(employeeAccount);
    }


}
