package com.vitordev.diaryofaprogrammer.service.exceptions;

import java.io.Serial;

public class MissingFieldException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    public MissingFieldException(String message) {
        super(message);
    }
}
