package com.vitordev.diaryofaprogrammer.service.exceptions;

import java.io.Serial;

public class MissingFieldException extends RuntimeException {


    public MissingFieldException(String message) {
        super(message);
    }
}
