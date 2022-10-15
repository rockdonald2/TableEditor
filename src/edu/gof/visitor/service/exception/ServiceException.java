package edu.gof.visitor.service.exception;

public class ServiceException extends Exception {

    public ServiceException(String errMsg) {
        super(errMsg);
    }

    public ServiceException(String errMsg, Throwable cause) {
        super(errMsg, cause);
    }

}
