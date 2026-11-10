package com.shop.employeeservice.grpc;

import com.shop.employeeservice.service.EmployeeService;
import com.shop.grpc.employee.EmployeeGrpcRequest;
import com.shop.grpc.employee.EmployeeGrpcResponse;
import com.shop.grpc.employee.EmployeeGrpcServiceGrpc;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import org.springframework.grpc.server.service.GrpcService;

@GrpcService
@RequiredArgsConstructor
public class EmployeeGrpcEndpoint extends EmployeeGrpcServiceGrpc.EmployeeGrpcServiceImplBase {

    private final EmployeeService employeeService;

    @Override
    public void createEmployee(
            EmployeeGrpcRequest request,
            StreamObserver<EmployeeGrpcResponse> responseObserver
    ) {
        try {
            EmployeeGrpcResponse response = employeeService.createEmployee(request);
            responseObserver.onNext(response);
            responseObserver.onCompleted();

        } catch (Exception ex) {
            responseObserver.onError(
                    Status.ALREADY_EXISTS
                            .withDescription("Không thể tạo hồ sơ khách")
                            .asRuntimeException()
            );
        }
    }

}
