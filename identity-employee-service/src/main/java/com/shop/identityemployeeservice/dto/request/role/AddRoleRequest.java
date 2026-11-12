package com.shop.identityemployeeservice.dto.request.role;

import java.util.List;

public record AddRoleRequest(
        List<Long> permissionIds
) {
}
