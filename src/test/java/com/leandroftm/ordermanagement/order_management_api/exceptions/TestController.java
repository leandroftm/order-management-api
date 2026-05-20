package com.leandroftm.ordermanagement.order_management_api.exceptions;

import com.leandroftm.ordermanagement.order_management_api.exception.domain.DataIntegrityViolationException;
import com.leandroftm.ordermanagement.order_management_api.exception.domain.DomainException;
import com.leandroftm.ordermanagement.order_management_api.exception.domain.NotFoundException;
import com.leandroftm.ordermanagement.order_management_api.exception.domain.category.CategoryNotEmptyException;
import com.leandroftm.ordermanagement.order_management_api.exception.enums.ErrorCode;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/tests")
public class TestController {

    @GetMapping("/bad-request")
    public void badRequest() {
        throw new CategoryNotEmptyException();
    }

    @GetMapping("/validation")
    public void validation() {
        throw new DomainException("Validation Exception", ErrorCode.VALIDATION_EXCEPTION);
    }

    @GetMapping("/not-found")
    public void notFound() {
        throw new NotFoundException("Not Found Exception", ErrorCode.NOT_FOUND);
    }

    @GetMapping("/conflict")
    public void conflict() {
        throw new DataIntegrityViolationException("Conflict Exception", ErrorCode.CONFLICT);
    }

    @GetMapping("/authorization-denied")
    public void authorizationDenied() {
        throw new AuthorizationDeniedException("Authorization Denied");
    }

    @GetMapping("/access-denied")
    public void accessDenied() {
        throw new AccessDeniedException("Access Denied");
    }

    @GetMapping("/generic")
    public void generic() {
        throw new RuntimeException("Unexpected error");
    }
}
