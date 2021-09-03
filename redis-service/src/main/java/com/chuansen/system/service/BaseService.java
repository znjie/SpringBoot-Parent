package com.chuansen.system.service;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * @Author:chuansen.zhan
 * @Date: 2021/8/2 17:56
 */

public abstract class BaseService<T, ID> {

    public abstract JpaRepository<T, ID> getJpaRepository();

    public List<T> saveAll(List<T> entitys) {
        return getJpaRepository().saveAll(entitys);
    }

    public List<T> findAll() {
        return getJpaRepository().findAll();
    }

    public Optional<T> get(ID id) {
        return getJpaRepository().findById(id);
    }
}
