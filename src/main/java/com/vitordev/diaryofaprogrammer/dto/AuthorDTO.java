package com.vitordev.diaryofaprogrammer.dto;


import com.vitordev.diaryofaprogrammer.domain.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
public class AuthorDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String id;
    private String name;

    public AuthorDTO(User user) {
        this.id = user.getUserId();
        this.name = user.getName();
    }
}
