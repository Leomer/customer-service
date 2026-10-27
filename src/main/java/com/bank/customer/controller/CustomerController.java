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

    @GetMapping("/{id}")
    public Single<CustomerDto> findById(@PathVariable String id) {
        return customerService.findById(id);
    }

    @PutMapping("/{id}")
    public Single<CustomerDto> update(@PathVariable String id, @Valid @RequestBody CustomerDto dto) {
        return customerService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Completable delete(@PathVariable String id) {
        return customerService.delete(id);
    }
}
