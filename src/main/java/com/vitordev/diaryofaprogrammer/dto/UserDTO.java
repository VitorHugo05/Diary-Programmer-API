package com.vitordev.diaryofaprogrammer.dto;

import com.vitordev.diaryofaprogrammer.domain.Post;
import com.vitordev.diaryofaprogrammer.domain.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
public class UserDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String id;
    private String name;
    private String email;
    private Date createdAt;
    private Date birthdate;
    private Integer likes;

    private List<String> posts = new ArrayList<>();

    public UserDTO(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.createdAt = user.getCreatedAt();
        this.birthdate = user.getBirthdate();
        this.likes = user.getLikes();
        this.posts = user.getPosts().stream()
                .map(Post::getId)
                .collect(Collectors.toList());
    }

}
