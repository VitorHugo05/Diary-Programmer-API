package com.vitordev.diaryofaprogrammer.service.exceptions;

import java.io.Serial;

public class DataAlreadyExistsException extends RuntimeException  {


    public DataAlreadyExistsException(String message) {
        super(message);
    }
}
