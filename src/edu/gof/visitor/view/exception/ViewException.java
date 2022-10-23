package edu.gof.visitor.view.exception;

public class ViewException extends RuntimeException {

    public ViewException(String errMsg) {
        super(errMsg);
    }

    public ViewException(String errMsg, Throwable cause) {
        super(errMsg, cause);
    }

}
