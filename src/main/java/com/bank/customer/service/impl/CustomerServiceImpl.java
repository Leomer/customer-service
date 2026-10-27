package com.bank.customer.service.impl;

import com.bank.customer.dto.CustomerDto;
import com.bank.customer.exception.BusinessException;
import com.bank.customer.exception.NotFoundException;
import com.bank.customer.mapper.CustomerMapper;
import com.bank.customer.model.Customer;
import com.bank.customer.repository.CustomerRepository;
import com.bank.customer.service.CustomerService;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Maybe;
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
    public Single<CustomerDto> findByDocumentNumber(String documentNumber) {
        return findEntity(documentNumber).map(CustomerMapper::toDto);
    }

    @Override
    public Single<CustomerDto> update(String documentNumber, CustomerDto dto) {
        log.info("Updating customer with document {}", documentNumber);
        return findEntity(documentNumber)
                .flatMap(existing -> {
                    dto.setId(existing.getId());
                    dto.setDocumentNumber(documentNumber); // the document number cannot be changed
                    return Single.fromPublisher(repository.save(CustomerMapper.toEntity(dto)));
                })
                .map(CustomerMapper::toDto);
    }

    @Override
    public Completable delete(String documentNumber) {
        log.info("Deleting customer with document {}", documentNumber);
        return findEntity(documentNumber)
                .flatMapCompletable(existing -> Completable.fromPublisher(repository.delete(existing)));
    }

    private Single<Customer> findEntity(String documentNumber) {
        return Maybe.fromPublisher(repository.findByDocumentNumber(documentNumber))
                .switchIfEmpty(Single.error(
                        new NotFoundException("Customer not found with document: " + documentNumber)));
    }
}
