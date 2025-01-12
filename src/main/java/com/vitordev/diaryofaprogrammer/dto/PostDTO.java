package com.vitordev.diaryofaprogrammer.dto;


import com.vitordev.diaryofaprogrammer.domain.Post;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PostDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String postId;
    private String title;
    private String content;
    private Date createdAt;
    private Integer likes;

    private AuthorDTO author;
    private List<CommentDTO> comments;

    public PostDTO(Post post) {
        this.postId = post.getPostId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.createdAt = post.getCreatedAt();
        this.author = post.getAuthor();
        this.comments = post.getComments();
        this.likes = post.getLikedUsers().size();
    }
}
