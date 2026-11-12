package com.shop.identityemployeeservice.service;

import com.shop.grpc.employee.EmployeeGrpcRequest;
import com.shop.grpc.employee.EmployeeGrpcResponse;
import com.shop.identityemployeeservice.dto.request.auth.CreateEmployeeRequest;
import com.shop.identityemployeeservice.dto.response.auth.CreateEmployeeResponse;
import com.shop.identityemployeeservice.entity.EmployeeAccount;
import com.shop.identityemployeeservice.entity.Role;
import com.shop.identityemployeeservice.exception.InternalServerException;
import com.shop.identityemployeeservice.exception.NotFoundException;
import com.shop.identityemployeeservice.grpc.EmployeeGrpcClient;
import com.shop.identityemployeeservice.mapper.AuthMapper;
import com.shop.identityemployeeservice.repository.EmployeeAccountRepository;
import com.shop.identityemployeeservice.service.interfaces.IAccountService;
import com.shop.identityemployeeservice.utils.IdentityGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountService implements IAccountService {

    private final EmployeeAccountRepository employeeAccountRepository;
    private final EmployeeGrpcClient employeeGrpcClient;
    private final IdentityGenerator identityGenerator;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;

    @Override
    public CreateEmployeeResponse createEmployee(String userUuid, CreateEmployeeRequest request) {

        EmployeeGrpcRequest employeeGrpcRequest = EmployeeGrpcRequest.newBuilder()
                .setFirstName(request.firstName())
                .setLastName(request.lastName())
                .setAddress(request.address())
                .setPhoneNumber(request.phoneNumber())
                .setDateOfBirth(request.dateOfBirth().toString())
                .setUuid(userUuid)
                .build();
        EmployeeGrpcResponse response = employeeGrpcClient.createEmployee(employeeGrpcRequest);
        if(!response.getSuccess()){
            throw new InternalServerException("Tạo tài khoản thất bại");
        }
        String username = identityGenerator.generateUniqueUsername(request.firstName() + " " + request.lastName());
        String password = identityGenerator.generatePassword(12);
        EmployeeAccount employeeAccount = EmployeeAccount.builder()
                .username(username)
                .passwordHash(passwordEncoder.encode(password))
                .build();

        employeeAccountRepository.save(employeeAccount);
        return new CreateEmployeeResponse(
                username,
                password,
                String.format("%s %s", request.firstName(), request.lastName())
        );
    }

    @Override
    public void addRole(String userUuid, Long roleId) {
        EmployeeAccount account = this.getByUserUuid(userUuid);
        Role role = roleService.getById(roleId);
        account.getRoles().add(role);
        employeeAccountRepository.save(account);
    }

    @Override
    public EmployeeAccount getByUserUuid(String userUuid) {
        return employeeAccountRepository.findById(UUID.fromString(userUuid))
                .orElseThrow(() -> new NotFoundException("Không tìm thấy tài khoản"));
    }
}
