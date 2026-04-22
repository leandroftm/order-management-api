package com.leandroftm.ordermanagement.order_management_api.exception.domain;

public class NotFoundException extends RuntimeException {
  public NotFoundException(String message) {
    super(message);
  }
}
