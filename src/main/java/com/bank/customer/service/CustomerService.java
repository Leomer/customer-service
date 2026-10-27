package com.bank.customer.service;

import com.bank.customer.dto.CustomerDto;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

public interface CustomerService {

    Single<CustomerDto> create(CustomerDto dto);
    Flowable<CustomerDto> findAll();
    Single<CustomerDto> findById(String id);
    Single<CustomerDto> update(String id, CustomerDto dto);
    Completable delete(String id);
}
