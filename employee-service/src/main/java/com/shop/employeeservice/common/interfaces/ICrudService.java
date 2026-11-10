package com.shop.employeeservice.common.interfaces;

public interface ICrudService<T,R,ID> {
    R create(String uuid, T request);
    R update(String uuid, ID id, T request);
    void delete(ID id);
}
