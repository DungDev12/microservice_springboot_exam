package com.shop.identityemployeeservice.common.interfaces;

public interface ICrudService<T,R,ID> {
    R create(T request);
    R update(ID id, T request);
    void delete(ID id);
}
