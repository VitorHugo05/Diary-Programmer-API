package com.vitordev.diaryofaprogrammer.controller;

import com.vitordev.diaryofaprogrammer.domain.post.Post;
import com.vitordev.diaryofaprogrammer.domain.post.PostUtils;
import com.vitordev.diaryofaprogrammer.domain.user.User;
import com.vitordev.diaryofaprogrammer.dto.AuthorDTO;
import com.vitordev.diaryofaprogrammer.dto.CommentDTO;
import com.vitordev.diaryofaprogrammer.dto.PostDTO;
import com.vitordev.diaryofaprogrammer.service.PostService;
import com.vitordev.diaryofaprogrammer.service.UserServices;
import com.vitordev.diaryofaprogrammer.service.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/api/posts")
public class PostController {
    @Autowired
    private PostService postService;
    @Autowired
    private PostUtils postUtils;
    @Autowired
    private UserServices userServices;

    @GetMapping
    public ResponseEntity<List<PostDTO>> findPosts() {
        List<Post> posts = postService.findAll();
        List<PostDTO> postDTO = posts.stream().map(postUtils::toDTO).toList();
        if (posts.isEmpty()) {
            throw new ObjectNotFoundException("Post not found");
        }
        return ResponseEntity.ok().body(postDTO);
    }

    @PostMapping
    public ResponseEntity<Void> addPost(@RequestBody PostDTO postDTO) {
        postUtils.validatePostFields(postDTO);

        Post post = postUtils.fromDTO(postDTO);
        postService.save(post);

        String authorId = post.getAuthor().getId();
        User author = userServices.findById(authorId);
        author.getPosts().add(post);
        userServices.save(author);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{postId}")
                .buildAndExpand(post.getPostId())
                .toUri();
        return ResponseEntity.created(uri).build();
    }

    @GetMapping(value = "/{postId}")
    public ResponseEntity<PostDTO> findPostById(@PathVariable String postId) {
        Post post = postService.findById(postId);
        if (post == null) {
            throw new ObjectNotFoundException("Post not found");
        }
        return ResponseEntity.ok().body(postUtils.toDTO(post));
    }

    @GetMapping(value = "/name")
    public ResponseEntity<List<PostDTO>> findPostByTitle(@RequestParam(value = "name", defaultValue = "") String title) {
        List<Post> posts = postService.findByTitle(title);
        if(posts.isEmpty()) {
            throw new ObjectNotFoundException("Post not found");
        }
        return ResponseEntity.ok().body(posts.stream().map(postUtils::toDTO).toList());
    }

    @GetMapping(value = "/{postId}/comments")
    public ResponseEntity<List<CommentDTO>> findCommentsByPostId(@PathVariable String postId) {
        Post post = postService.findById(postId);
        List<CommentDTO> comments = post.getComments();
        if(comments.isEmpty()) {
            throw new ObjectNotFoundException("Post without comment");
        }
        return ResponseEntity.ok().body(comments);
    }

    @PostMapping(value = "/{postId}/comments")
    public ResponseEntity<Void> addComment(@PathVariable String postId, @RequestBody CommentDTO commentDTO) {
        commentDTO.setCreatedAt(new Date());
        commentDTO.setCommentId(UUID.randomUUID().toString());
        postService.addComment(postId, commentDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping(value = "/{postId}/comments")
    public ResponseEntity<Void> deleteComment(@PathVariable String postId, @RequestBody CommentDTO commentDTO) {
        postUtils.validateCommentDTOFields(commentDTO);
        postService.deleteComment(postId, commentDTO);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(value = "/{postId}/likes")
    public ResponseEntity<PostDTO> likePost(@PathVariable String postId, @RequestParam(value = "userId", defaultValue = "") String userId) {
        Post updatedPost = postService.likeOrDislikePost(postId, userId);
        PostDTO postDTO = new PostDTO(updatedPost);
        return ResponseEntity.ok(postDTO);
    }

    @GetMapping("/{postId}/likes")
    public ResponseEntity<List<AuthorDTO>> getLikedUsers(@PathVariable String postId) {
        List<AuthorDTO> likedUsers = postService.getLikedUsers(postId);
        return ResponseEntity.ok(likedUsers);
    }

    @PutMapping(value = "/{postId}")
    public ResponseEntity<Void> updatePost(@PathVariable String postId, @RequestBody PostDTO postDTO) {
        Post oldPost = postService.findById(postId);
        if (oldPost == null) {
            throw new ObjectNotFoundException("Post not found");
        }

        postUtils.validatePostFields(postDTO);
        Post newPost = postUtils.fromDTO(postDTO);
        postService.update(oldPost, newPost);
        postService.save(newPost);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(value = "/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable String postId, @AuthenticationPrincipal UserDetails userDetails) {
        postService.validateOwnership(postId, userDetails.getUsername());
        postService.delete(postId);
        return ResponseEntity.noContent().build();
    }

}
