package com.prepcv.pms.exception;

public class InvalidApplicationStatusTransitionException
        extends RuntimeException {

    public InvalidApplicationStatusTransitionException(
            String message
    ) {
        super(message);
    }
}