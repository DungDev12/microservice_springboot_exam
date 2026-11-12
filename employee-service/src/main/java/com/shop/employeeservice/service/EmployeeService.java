package com.shop.employeeservice.service;

import com.shop.employeeservice.dto.request.UpdateInfoRequest;
import com.shop.employeeservice.dto.response.EmployeeResponse;
import com.shop.employeeservice.entity.Employee;
import com.shop.employeeservice.exception.NotFoundException;
import com.shop.employeeservice.mapper.EmployeeMapper;
import com.shop.employeeservice.repository.EmployeeRepository;
import com.shop.employeeservice.service.interfaces.IEmployeeService;
import com.shop.grpc.employee.EmployeeGrpcRequest;
import com.shop.grpc.employee.EmployeeGrpcResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmployeeService implements IEmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;

    @Transactional
    @Override
    public EmployeeGrpcResponse createEmployee(EmployeeGrpcRequest request) {

        Employee employee = Employee.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .address(request.getAddress())
                .phoneNumber(request.getPhoneNumber())
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

    @Override
    public EmployeeResponse getMe(String uuid) {
        return employeeMapper.toResponse(this.findByUuid(uuid));
    }

    @Override
    public EmployeeResponse updateMeInfo(String uuid, UpdateInfoRequest request) {
        Employee employee = this.findByUuid(uuid);
        employeeMapper.update(request, employee);
        return employeeMapper.toResponse(employeeRepository.save(employee));
    }


    @Override
    public Employee findByUuid(String uuid) {
        return employeeRepository.findById(UUID.fromString(uuid))
                .orElseThrow(() -> new NotFoundException("Không tìm thấy nhân viên"));
    }
}
