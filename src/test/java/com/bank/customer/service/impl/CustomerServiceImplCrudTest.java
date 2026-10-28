package com.bank.customer.service.impl;

import com.bank.customer.dto.CustomerDto;
import com.bank.customer.exception.NotFoundException;
import com.bank.customer.model.Customer;
import com.bank.customer.model.CustomerProfile;
import com.bank.customer.model.CustomerType;
import com.bank.customer.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/** Additional unit tests for {@link CustomerServiceImpl}: update, delete and queries. */
@ExtendWith(MockitoExtension.class)
class CustomerServiceImplCrudTest {

    @Mock
    private CustomerRepository repository;

    @InjectMocks
    private CustomerServiceImpl service;

    private Customer entity;

    @BeforeEach
    void setUp() {
        entity = Customer.builder()
                .id("abc123")
                .type(CustomerType.PERSONAL)
                .profile(CustomerProfile.STANDARD)
                .documentNumber("12345678")
                .name("Juan Perez")
                .build();
    }

    @Test
    void findAllShouldReturnAllCustomers() {
        when(repository.findAll()).thenReturn(Flux.just(entity, entity));

        service.findAll().test().assertValueCount(2).assertComplete();
    }

    @Test
    void updateShouldKeepIdAndDocumentNumber() {
        when(repository.findByDocumentNumber("12345678")).thenReturn(Mono.just(entity));
        when(repository.save(any(Customer.class)))
                .thenAnswer(inv -> Mono.just(inv.getArgument(0)));

        CustomerDto dto = CustomerDto.builder()
                .type(CustomerType.PERSONAL)
                .documentNumber("99999999") // must be ignored
                .name("Juan Updated")
                .build();

        service.update("12345678", dto)
                .test()
                .assertValue(r -> "abc123".equals(r.getId())
                        && "12345678".equals(r.getDocumentNumber())
                        && "Juan Updated".equals(r.getName()))
                .assertComplete();
    }

    @Test
    void updateShouldFailWhenCustomerMissing() {
        when(repository.findByDocumentNumber("000")).thenReturn(Mono.empty());

        service.update("000", CustomerDto.builder().name("x").build())
                .test()
                .assertError(NotFoundException.class);

        verify(repository, never()).save(any());
    }

    @Test
    void deleteShouldFailWhenCustomerMissing() {
        when(repository.findByDocumentNumber("000")).thenReturn(Mono.empty());

        service.delete("000").test().assertError(NotFoundException.class);

        verify(repository, never()).delete(any());
    }
}