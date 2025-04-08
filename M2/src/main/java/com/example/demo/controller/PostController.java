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
    public ResponseEntity<?> getPostById(@PathVariable("id") @NonNull Long id,
                                         @RequestHeader("Authorization") String authHeader) throws PostException {
        String jwt = authHeader.replace("Bearer ", "");
        String requesterEmail = JWT.decode(jwt).getSubject();

        return ResponseEntity.ok(postService.findByIdIfVisible(id, requesterEmail, jwt));
    }

    @GetMapping("/filter/byHashtag/{hashtag}")
    public ResponseEntity<?> filterByHashtag(@PathVariable("hashtag") String hashtag,
                                             @RequestHeader("Authorization") String authHeader) {
        String jwt = authHeader.replace("Bearer ", "");
        String email = JWT.decode(jwt).getSubject();

        return ResponseEntity.ok(postService.filterByHashtag(hashtag, email, jwt));
    }

    @GetMapping("/filter/byText/{text}")
    public ResponseEntity<?> filterByText(@PathVariable("text") String text,
                                          @RequestHeader("Authorization") String authHeader) {
        String jwt = authHeader.replace("Bearer ", "");
        String email = JWT.decode(jwt).getSubject();

        return ResponseEntity.ok(postService.filterByText(text, email, jwt));
    }

    @GetMapping("/filter/byAuthor/{authorEmail}")
    public ResponseEntity<?> filterByAuthor(@PathVariable("authorEmail") String authorEmail,
                                            @RequestHeader("Authorization") String authHeader) {
        String jwt = authHeader.replace("Bearer ", "");
        String requesterEmail = JWT.decode(jwt).getSubject();

        return ResponseEntity.ok(postService.filterByAuthor(authorEmail, requesterEmail, jwt));
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchPosts(
            @RequestParam(required = false) String text,
            @RequestParam(required = false) String hashtag,
            @RequestParam(required = false) String authorEmail,
            @RequestHeader("Authorization") String authHeader
    ) {
        String jwt = authHeader.replace("Bearer ", "");
        String requesterEmail = JWT.decode(jwt).getSubject();

        return ResponseEntity.ok(postService.search(text, hashtag, authorEmail, requesterEmail, jwt));
    }

    @PostMapping("/create")
    public ResponseEntity<?> createPost(@RequestBody PostDTO postDTO,
                                        @RequestHeader("Authorization") String authHeader){

        String jwt = authHeader.replace("Bearer ", "");
        String email = JWT.decode(jwt).getSubject();
        return ResponseEntity.status(HttpStatus.CREATED).body(postService.createPost(postDTO, email));
    }

    @PutMapping("/update")
    public ResponseEntity<?> updatePost(@RequestBody PostDTO postDTO,
                                        @RequestHeader("Authorization") String authHeader) throws PostException {

        String jwt = authHeader.replace("Bearer ", "");
        String email = JWT.decode(jwt).getSubject();
        return ResponseEntity.ok(postService.updatePost(postDTO, email));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deletePost(@PathVariable("id") Long id,
                                        @RequestHeader("Authorization") String authHeader) throws PostException {
        String jwt = authHeader.replace("Bearer ", "");
        String email = JWT.decode(jwt).getSubject();
        postService.deletePost(id, email);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/getVisible")
    public ResponseEntity<List<PostDTO>> getVisiblePosts(@RequestHeader("Authorization") String authHeader) {
        String jwtToken = authHeader.replace("Bearer ", "");
        String userEmail = JWT.decode(jwtToken).getSubject();

        List<PostDTO> posts = postService.findVisiblePostsForUser(userEmail, jwtToken);
        return ResponseEntity.ok(posts);
    }
}
