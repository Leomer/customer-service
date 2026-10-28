package com.bank.customer.service.impl;

import com.bank.customer.dto.CustomerDto;
import com.bank.customer.exception.BusinessException;
import com.bank.customer.exception.NotFoundException;
import com.bank.customer.model.Customer;
import com.bank.customer.model.CustomerType;
import com.bank.customer.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/** Unit tests for {@link CustomerServiceImpl}. */
@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {

    @Mock
    private CustomerRepository repository;

    @InjectMocks
    private CustomerServiceImpl service;

    private CustomerDto dto;
    private Customer entity;

    @BeforeEach
    void setUp() {
        dto = CustomerDto.builder()
                .type(CustomerType.PERSONAL)
                .documentNumber("12345678")
                .name("Juan Perez")
                .email("juan@mail.com")
                .build();
        entity = Customer.builder()
                .id("abc123")
                .type(CustomerType.PERSONAL)
                .documentNumber("12345678")
                .name("Juan Perez")
                .email("juan@mail.com")
                .build();
    }

    @Test
    void createShouldSaveWhenDocumentIsNew() {
        when(repository.existsByDocumentNumber("12345678")).thenReturn(Mono.just(false));
        when(repository.save(any(Customer.class))).thenReturn(Mono.just(entity));

        service.create(dto)
                .test()
                .assertValue(result -> "abc123".equals(result.getId()))
                .assertComplete();
    }

    @Test
    void createShouldFailWhenDocumentExists() {
        when(repository.existsByDocumentNumber("12345678")).thenReturn(Mono.just(true));

        service.create(dto)
                .test()
                .assertError(BusinessException.class);

        verify(repository, never()).save(any());
    }

    @Test
    void findByDocumentNumberShouldReturnCustomer() {
        when(repository.findByDocumentNumber("12345678")).thenReturn(Mono.just(entity));

        service.findByDocumentNumber("12345678")
                .test()
                .assertValue(result -> "Juan Perez".equals(result.getName()))
                .assertComplete();
    }

    @Test
    void findByDocumentNumberShouldFailWhenMissing() {
        when(repository.findByDocumentNumber("000")).thenReturn(Mono.empty());

        service.findByDocumentNumber("000")
                .test()
                .assertError(NotFoundException.class);
    }

    @Test
    void deleteShouldRemoveExistingCustomer() {
        when(repository.findByDocumentNumber("12345678")).thenReturn(Mono.just(entity));
        when(repository.delete(entity)).thenReturn(Mono.empty());

        service.delete("12345678")
                .test()
                .assertComplete();

        verify(repository).delete(entity);
    }
}