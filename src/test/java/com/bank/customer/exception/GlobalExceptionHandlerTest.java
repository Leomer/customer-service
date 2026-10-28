package com.bank.customer.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

/** Unit tests for {@link GlobalExceptionHandler}. */
class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void notFoundShouldReturn404() {
        ResponseEntity<Map<String, String>> r = handler.handleNotFound(new NotFoundException("nf"));
        assertEquals(404, r.getStatusCode().value());
        assertEquals("nf", r.getBody().get("error"));
    }

    @Test
    void businessShouldReturn400() {
        ResponseEntity<Map<String, String>> r = handler.handleBusiness(new BusinessException("bad"));
        assertEquals(400, r.getStatusCode().value());
    }

    @Test
    void genericShouldReturn500() {
        ResponseEntity<Map<String, String>> r = handler.handleGeneric(new RuntimeException("boom"));
        assertEquals(500, r.getStatusCode().value());
        assertEquals("Internal server error", r.getBody().get("error"));
    }
}