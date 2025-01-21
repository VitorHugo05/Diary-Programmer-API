package com.vitordev.diaryofaprogrammer.dto;

import com.vitordev.diaryofaprogrammer.domain.post.Post;
import com.vitordev.diaryofaprogrammer.domain.user.User;
import com.vitordev.diaryofaprogrammer.domain.user.enums.UserRole;
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

    private String userId;
    private String name;
    private String email;
    private Date createdAt;
    private Date birthdate;
    private UserRole role;

    private List<String> posts = new ArrayList<>();

    public UserDTO(User user) {
        this.userId = user.getUserId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.createdAt = user.getCreatedAt();
        this.birthdate = user.getBirthdate();
        this.posts = user.getPosts().stream()
                .map(Post::getPostId)
                .collect(Collectors.toList());
        this.role = user.getRole();
    }
}
