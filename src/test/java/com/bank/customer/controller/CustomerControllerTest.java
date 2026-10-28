package com.bank.customer.controller;

import com.bank.customer.dto.CustomerDto;
import com.bank.customer.service.CustomerService;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/** Unit tests for {@link CustomerController}. */
@ExtendWith(MockitoExtension.class)
class CustomerControllerTest {

    @Mock
    private CustomerService customerService;

    @InjectMocks
    private CustomerController controller;

    private final CustomerDto dto = CustomerDto.builder().documentNumber("123").name("Ana").build();

    @Test
    void createShouldDelegateToService() {
        when(customerService.create(dto)).thenReturn(Single.just(dto));
        controller.create(dto).test().assertValue(dto);
    }

    @Test
    void findAllShouldDelegateToService() {
        when(customerService.findAll()).thenReturn(Flowable.just(dto));
        controller.findAll().test().assertValueCount(1);
    }

    @Test
    void findByDocumentNumberShouldDelegateToService() {
        when(customerService.findByDocumentNumber("123")).thenReturn(Single.just(dto));
        controller.findByDocumentNumber("123").test().assertValue(dto);
    }

    @Test
    void updateShouldDelegateToService() {
        when(customerService.update("123", dto)).thenReturn(Single.just(dto));
        controller.update("123", dto).test().assertValue(dto);
    }

    @Test
    void deleteShouldDelegateToService() {
        when(customerService.delete("123")).thenReturn(Completable.complete());
        controller.delete("123").test().assertComplete();
        verify(customerService).delete("123");
    }
}