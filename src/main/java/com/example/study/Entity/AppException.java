package com.example.study.Entity;

public class AppException extends RuntimeException {
  private ErrorCode errorCode;

  public AppException(ErrorCode error) {
    super(error.getMessage());
    this.errorCode = error;
  }

  public ErrorCode getError() {
    return errorCode;
  }

  public void setError(ErrorCode error) {
    this.errorCode = error;
  }
}
