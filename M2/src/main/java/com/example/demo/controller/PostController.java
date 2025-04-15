package com.example.demo.controller;

import com.auth0.jwt.JWT;
import com.example.demo.dto.postdto.PostDTO;
import com.example.demo.errorhandler.PostException;
import com.example.demo.service.PostService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/post")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping("/home")
    public String greet(){
        return "Hello World !";
    }

    @GetMapping("/getAll")
    public ResponseEntity<?> getAllPostsSorted() {
        return ResponseEntity.ok(postService.findAllSortedByDateDesc());
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<PostDTO> getPostById(@PathVariable("id") Long id) throws PostException {
        PostDTO post = postService.findById(id);
        return ResponseEntity.ok(post);
    }

    @GetMapping("/filter/byHashtag/{hashtag}")
    public ResponseEntity<List<PostDTO>> filterByHashtag(@PathVariable("hashtag") String hashtag) {
        List<PostDTO> posts = postService.filterByHashtag(hashtag);
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/filter/byText/{text}")
    public ResponseEntity<List<PostDTO>> filterByText(@PathVariable("text") String text) {
        List<PostDTO> posts = postService.filterByText(text);
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/filter/byAuthor/{authorEmail}")
    public ResponseEntity<List<PostDTO>> filterByAuthor(@PathVariable("authorEmail") String authorEmail) {
        List<PostDTO> posts = postService.filterByAuthor(authorEmail);
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/search")
    public ResponseEntity<List<PostDTO>> searchPosts(
            @RequestParam(required = false) String text,
            @RequestParam(required = false) String hashtag,
            @RequestParam(required = false) String authorEmail) {
        List<PostDTO> posts = postService.search(text, hashtag, authorEmail);
        return ResponseEntity.ok(posts);
    }

    @PostMapping("/create")
    public ResponseEntity<Long> createPost(@RequestBody PostDTO postDTO){
        Long createdPostId = postService.createPost(postDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPostId);
    }


    @PutMapping("/update")
    public ResponseEntity<Long> updatePost(@RequestBody PostDTO postDTO) throws PostException {
        Long updatedPostId = postService.updatePost(postDTO);
        return ResponseEntity.status(HttpStatus.OK).body(updatedPostId);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable("id") Long id) throws PostException {
        postService.deletePost(id);
        return ResponseEntity.ok().build();
    }
}
