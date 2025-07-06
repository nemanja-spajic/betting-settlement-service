package com.sporty.betting.settlement.common.exception;

import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ApiError {

  private Integer status;
  private String reason;
  private String message;
  private ErrorCode errorCode;
  private Set<ApiFieldError> details = new HashSet<>();
  private final ZonedDateTime timestamp = ZonedDateTime.now();

  public ApiError(Integer status, String reason, String message, ErrorCode errorCode) {
    this.status = status;
    this.reason = reason;
    this.message = message;
    this.errorCode = errorCode;
  }

  public void addFieldError(String field, String message) {
    details.add(new ApiFieldError(field, message));
  }
}
