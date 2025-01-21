package com.vitordev.diaryofaprogrammer.service.exceptions;

import java.io.Serial;

public class ObjectNotFoundException extends RuntimeException {


    public ObjectNotFoundException(String msg){
        super(msg);
    }
}
