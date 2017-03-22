package com.kamabizbazti.model.exceptions;

import com.kamabizbazti.model.interfaces.IErrorCode;

public class CustomCategoryAlreadyExistException extends RuntimeException {

    private IErrorCode errorCode;

    public CustomCategoryAlreadyExistException(String message) {
        super(message);
    }

    public CustomCategoryAlreadyExistException(IErrorCode errorCode) {
        super();
        this.errorCode = errorCode;
    }

    public CustomCategoryAlreadyExistException(IErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public IErrorCode getErrorCode() {
        return errorCode;
    }
}
