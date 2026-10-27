package com.bank.customer.controller;

import com.bank.customer.dto.CustomerDto;
import com.bank.customer.service.CustomerService;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Single<CustomerDto> create(@Valid @RequestBody CustomerDto dto) {
        return customerService.create(dto);
    }

    @GetMapping
    public Flowable<CustomerDto> findAll() {
        return customerService.findAll();
    }

    @GetMapping("/{documentNumber}")
    public Single<CustomerDto> findByDocumentNumber(@PathVariable String documentNumber) {
        return customerService.findByDocumentNumber(documentNumber);
    }

    @PutMapping("/{documentNumber}")
    public Single<CustomerDto> update(@PathVariable String documentNumber, @Valid @RequestBody CustomerDto dto) {
        return customerService.update(documentNumber, dto);
    }

    @DeleteMapping("/{documentNumber}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Completable delete(@PathVariable String documentNumber) {
        return customerService.delete(documentNumber);
    }
}
