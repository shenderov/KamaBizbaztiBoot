package com.kamabizbazti.model.exceptions;

import com.kamabizbazti.model.interfaces.IErrorCode;

public class RecordDoesNotExistException extends RuntimeException {

    private IErrorCode errorCode;

    public RecordDoesNotExistException(String message) {
        super(message);
    }

    public RecordDoesNotExistException(IErrorCode errorCode) {
        super();
        this.errorCode = errorCode;
    }

    public RecordDoesNotExistException(IErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public IErrorCode getErrorCode() {
        return errorCode;
    }
}
