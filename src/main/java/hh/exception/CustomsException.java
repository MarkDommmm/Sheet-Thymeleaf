package hh.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RestControllerAdvice;


public class CustomsException extends Exception {
    public CustomsException(String message) {
        super(message);
    }
}
