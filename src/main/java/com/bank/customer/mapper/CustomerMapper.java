package com.bank.customer.mapper;

import com.bank.customer.dto.CustomerDto;
import com.bank.customer.model.Customer;
import com.bank.customer.model.CustomerProfile;

public final class CustomerMapper {

    private CustomerMapper() {
    }

    /**
     * Converts an entity to a DTO.
     */
    public static CustomerDto toDto(Customer entity) {
        return CustomerDto.builder()
                .id(entity.getId())
                .type(entity.getType())
                .profile(entity.getProfile())
                .documentNumber(entity.getDocumentNumber())
                .name(entity.getName())
                .email(entity.getEmail())
                .phone(entity.getPhone())
                .build();
    }

    /**
     * Converts a DTO to an entity. Profile defaults to STANDARD.
     */
    public static Customer toEntity(CustomerDto dto) {
        return Customer.builder()
                .id(dto.getId())
                .type(dto.getType())
                .profile(dto.getProfile() != null ? dto.getProfile() : CustomerProfile.STANDARD)
                .documentNumber(dto.getDocumentNumber())
                .name(dto.getName())
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .build();
    }
}
