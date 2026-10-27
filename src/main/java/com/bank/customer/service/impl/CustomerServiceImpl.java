package com.bank.customer.service.impl;

import com.bank.customer.dto.CustomerDto;
import com.bank.customer.exception.BusinessException;
import com.bank.customer.exception.NotFoundException;
import com.bank.customer.mapper.CustomerMapper;
import com.bank.customer.repository.CustomerRepository;
import com.bank.customer.service.CustomerService;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository repository;

    @Override
    public Single<CustomerDto> create(CustomerDto dto) {
        log.info("Creating customer with document {}", dto.getDocumentNumber());
        return Single.fromPublisher(repository.existsByDocumentNumber(dto.getDocumentNumber()))
                .flatMap(exists -> {
                    if (exists) {
                        return Single.<CustomerDto>error(
                                new BusinessException("Customer already exists with that document number"));
                    }
                    dto.setId(null);
                    return Single.fromPublisher(repository.save(CustomerMapper.toEntity(dto)))
                            .map(CustomerMapper::toDto);
                });
    }

    @Override
    public Flowable<CustomerDto> findAll() {
        return Flowable.fromPublisher(repository.findAll()).map(CustomerMapper::toDto);
    }

    @Override
    public Single<CustomerDto> findById(String id) {
        return Flowable.fromPublisher(repository.findById(id))
                .firstOrError()
                .onErrorResumeNext(e -> Single.error(new NotFoundException("Customer not found: " + id)))
                .map(CustomerMapper::toDto);
    }

    @Override
    public Single<CustomerDto> update(String id, CustomerDto dto) {
        log.info("Updating customer {}", id);
        return Flowable.fromPublisher(repository.findById(id))
                .firstOrError()
                .onErrorResumeNext(e -> Single.error(new NotFoundException("Customer not found: " + id)))
                .flatMap(existing -> {
                    dto.setId(existing.getId());
                    return Single.fromPublisher(repository.save(CustomerMapper.toEntity(dto)));
                })
                .map(CustomerMapper::toDto);
    }

    @Override
    public Completable delete(String id) {
        log.info("Deleting customer {}", id);
        return Flowable.fromPublisher(repository.findById(id))
                .firstOrError()
                .onErrorResumeNext(e -> Single.error(new NotFoundException("Customer not found: " + id)))
                .flatMapCompletable(existing -> Completable.fromPublisher(repository.deleteById(id)));
    }
}
