package com.shop.employeeservice.service.interfaces;

import com.shop.grpc.employee.EmployeeGrpcRequest;
import com.shop.grpc.employee.EmployeeGrpcResponse;

public interface IEmployeeService {

    EmployeeGrpcResponse createEmployee(EmployeeGrpcRequest request);
}
