package com.shop.identityemployeeservice.config.db;

import com.shop.identityemployeeservice.entity.EmployeeAccount;
import com.shop.identityemployeeservice.entity.Permission;
import com.shop.identityemployeeservice.entity.Role;
import com.shop.identityemployeeservice.repository.EmployeeAccountRepository;
import com.shop.identityemployeeservice.repository.PermissionRepository;
import com.shop.identityemployeeservice.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationRunner {

    private  static final UUID SYSTEM_USER_ID =
            new UUID(0,0L);

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final EmployeeAccountRepository employeeAccountRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if(roleRepository.count()==0){
            this.generateRole();
        }
        if(permissionRepository.count()==0){
            this.generatePermission();
        }

        if(employeeAccountRepository.existsByUsername("admin")){
            return;
        }
        this.assignPermissionsToAdminRole();
        this.generateAdmin();
    }

    private void generateRole() {
        List<Role> roles = new ArrayList<>();
        roles.add(Role.builder()
                .name("ADMIN")
                .description("Quyền quản trị viên")
                .createdBy(SYSTEM_USER_ID)
                .build());

        roles.add(Role.builder()
                .name("MANAGER")
                .description("Quyền quản lý")
                .createdBy(SYSTEM_USER_ID)
                .build());

        roles.add(Role.builder()
                .name("EMPLOYEE")
                .description("Nhân viên")
                .createdBy(SYSTEM_USER_ID)
                .build());

        roleRepository.saveAll(roles);
    }

    private void generatePermission() {
        List<Permission> permissions = new ArrayList<>();
        permissions.add(Permission.builder()
                .name("CREATE_EMPLOYEE")
                .description("Tạo nhân viên")
                .createdBy(SYSTEM_USER_ID)
                .build());

        permissions.add(Permission.builder()
                .name("UPDATE_EMPLOYEE")
                .description("Sửa nhân viên")
                .createdBy(SYSTEM_USER_ID)
                .build());

        permissions.add(Permission.builder()
                .name("DELETE_EMPLOYEE")
                .description("Xoá nhân viên")
                .createdBy(SYSTEM_USER_ID)
                .build());

        permissions.add(Permission.builder()
                .name("CREATE_ROLE")
                .description("Tạo vai trò")
                .createdBy(SYSTEM_USER_ID)
                .build());

        permissions.add(Permission.builder()
                .name("UPDATE_ROLE")
                .description("Sửa vai trò")
                .createdBy(SYSTEM_USER_ID)
                .build());

        permissions.add(Permission.builder()
                .name("DELETE_ROLE")
                .description("Xoá vai trò")
                .createdBy(SYSTEM_USER_ID)
                .build());

        permissions.add(Permission.builder()
                .name("ADD_ROLE")
                .description("Thêm vai trò vào tài khoản")
                .createdBy(SYSTEM_USER_ID)
                .build());

        permissions.add(Permission.builder()
                .name("CREATE_PERMISSION")
                .description("Tạo quyền")
                .createdBy(SYSTEM_USER_ID)
                .build());

        permissions.add(Permission.builder()
                .name("UPDATE_PERMISSION")
                .description("Sửa quyền")
                .createdBy(SYSTEM_USER_ID)
                .build());

        permissions.add(Permission.builder()
                .name("DELETE_PERMISSION")
                .description("Xoá quyền")
                .createdBy(SYSTEM_USER_ID)
                .build());

        permissions.add(Permission.builder()
                .name("ADD_PERMISSION")
                .description("Thêm quyền vào vai trò")
                .createdBy(SYSTEM_USER_ID)
                .build());

        permissionRepository.saveAll(permissions);
    }

    private void assignPermissionsToAdminRole() {

        Role adminRole = roleRepository.findByName("ADMIN")
                .orElseThrow(() ->
                        new RuntimeException("ADMIN role not found")
                );

        List<Permission> permissions = permissionRepository.findAll();
        adminRole.setPermissions(new HashSet<>(permissions));
        roleRepository.save(adminRole);
    }

    private void generateAdmin(){
        Role adminRole = roleRepository.findByName("ADMIN")
                .orElseThrow(() ->
                        new IllegalStateException("Quyền ADMIN không tồn tại")
                );

        EmployeeAccount account = EmployeeAccount.builder()
                .username("admin")
                .passwordHash(passwordEncoder.encode("admin"))
                .roles(new HashSet<>(Set.of(adminRole)))
                .build();

        employeeAccountRepository.save(account);
    }
}
