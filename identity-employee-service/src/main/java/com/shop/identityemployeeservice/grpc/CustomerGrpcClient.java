package com.shop.identityemployeeservice.grpc;

import com.shop.grpc.employee.EmployeeGrpcRequest;
import com.shop.grpc.employee.EmployeeGrpcResponse;
import com.shop.grpc.employee.EmployeeGrpcServiceGrpc;
import io.grpc.StatusRuntimeException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@Getter
@RequiredArgsConstructor
public class CustomerGrpcClient {

    private final EmployeeGrpcServiceGrpc.EmployeeGrpcServiceBlockingStub employeeGrpcServiceBlockingStub;

    public EmployeeGrpcResponse createEmployee(EmployeeGrpcRequest request){
        try {
            return employeeGrpcServiceBlockingStub
                    .withDeadlineAfter(60, TimeUnit.SECONDS) // set thời gian chết
                    .createEmployee(request);
        } catch (StatusRuntimeException ex) {
            // Demo: ném lại để tầng gọi xử lý.
            // Production: chuyển status thành exception phù hợp.
            throw ex;
        }
    }
}
