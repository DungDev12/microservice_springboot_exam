package com.shop.identityemployeeservice.service.interfaces;

import com.shop.identityemployeeservice.dto.request.auth.CreateEmployeeRequest;
import com.shop.identityemployeeservice.dto.response.auth.CreateEmployeeResponse;
import com.shop.identityemployeeservice.entity.EmployeeAccount;

public interface IAccountService {

    CreateEmployeeResponse createEmployee(String userUuid, CreateEmployeeRequest request);

    void addRole(String userUuid, Long roleId);

    EmployeeAccount getByUserUuid(String userUuid);
}
