package com.bank.customer.dto;

import com.bank.customer.model.CustomerProfile;
import com.bank.customer.model.CustomerType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDto {

    private String id;

    @NotNull(message = "type is required")
    private CustomerType type;

    private CustomerProfile profile;

    @NotBlank(message = "documentNumber is required")
    private String documentNumber;

    @NotBlank(message = "name is required")
    private String name;

    @Email(message = "email is invalid")
    private String email;

    private String phone;
}
