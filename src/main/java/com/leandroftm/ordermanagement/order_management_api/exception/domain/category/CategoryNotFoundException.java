package com.leandroftm.ordermanagement.order_management_api.exception.domain.category;

public class CategoryNotFoundException extends RuntimeException {
  public CategoryNotFoundException(String message) {
    super(message);
  }
}
