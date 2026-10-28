package com.bank.customer.mapper;

import com.bank.customer.dto.CustomerDto;
import com.bank.customer.model.Customer;
import com.bank.customer.model.CustomerProfile;
import com.bank.customer.model.CustomerType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/** Unit tests for {@link CustomerMapper}. */
class CustomerMapperTest {

    @Test
    void toEntityShouldDefaultProfileToStandard() {
        CustomerDto dto = CustomerDto.builder()
                .type(CustomerType.BUSINESS).documentNumber("20123456789").name("ACME").build();

        Customer entity = CustomerMapper.toEntity(dto);

        assertEquals(CustomerProfile.STANDARD, entity.getProfile());
        assertEquals("20123456789", entity.getDocumentNumber());
    }

    @Test
    void toDtoShouldCopyAllFields() {
        Customer entity = Customer.builder().id("1").type(CustomerType.PERSONAL)
                .profile(CustomerProfile.VIP).documentNumber("123").name("Ana")
                .email("a@mail.com").phone("999").build();

        CustomerDto dto = CustomerMapper.toDto(entity);

        assertEquals("1", dto.getId());
        assertEquals(CustomerProfile.VIP, dto.getProfile());
        assertEquals("a@mail.com", dto.getEmail());
        assertEquals("999", dto.getPhone());
    }
}