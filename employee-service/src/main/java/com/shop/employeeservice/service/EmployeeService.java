package com.shop.employeeservice.service;

import com.shop.employeeservice.entity.Employee;
import com.shop.employeeservice.repository.EmployeeRepository;
import com.shop.employeeservice.service.interfaces.IEmployeeService;
import com.shop.grpc.employee.EmployeeGrpcRequest;
import com.shop.grpc.employee.EmployeeGrpcResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmployeeService implements IEmployeeService {

    private final EmployeeRepository employeeRepository;

    @Override
    public EmployeeGrpcResponse createEmployee(EmployeeGrpcRequest request) {

        Employee employee = Employee.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .address(request.getAddress())
                .accountId(UUID.fromString(request.getUuid()))
                .dateOfBirth(LocalDate.parse(request.getDateOfBirth()))
                .build();

        employee.assignEmployeeCode(employeeRepository.findMaxEmployeeNumber());
        employeeRepository.save(employee);
        return EmployeeGrpcResponse.newBuilder()
                .setSuccess(true)
                .setMessage("Tạo thành công")
                .build();
    }
}
