package com.shop.employeeservice.service.interfaces;

import com.shop.employeeservice.dto.request.UpdateInfoRequest;
import com.shop.employeeservice.dto.response.EmployeeResponse;
import com.shop.employeeservice.entity.Employee;
import com.shop.grpc.employee.EmployeeGrpcRequest;
import com.shop.grpc.employee.EmployeeGrpcResponse;

public interface IEmployeeService {

    EmployeeGrpcResponse createEmployee(EmployeeGrpcRequest request);

    EmployeeResponse getMe(String uuid);

    EmployeeResponse updateMeInfo(String uuid, UpdateInfoRequest request);

    Employee findByUuid(String uuid);
}
