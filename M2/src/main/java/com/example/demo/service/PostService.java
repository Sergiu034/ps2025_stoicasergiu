package com.example.demo.service;

import com.example.demo.builder.postbuilder.PostBuilder;
import com.example.demo.dto.postdto.PostDTO;
import com.example.demo.entity.Hashtag;
import com.example.demo.entity.Post;
import com.example.demo.entity.Visibility;
import com.example.demo.errorhandler.PostException;
import com.example.demo.repository.HashtagRepository;
import com.example.demo.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final HashtagRepository hashtagRepository;

    public List<PostDTO> findAllSortedByDateDesc() {
        return postRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(PostBuilder::generateDTOFromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public Long createPost(PostDTO postDTO) {
        Set<Hashtag> hashtags = getOrCreateHashtags(postDTO.getHashtags());
        Post post = PostBuilder.generateEntityFromDTO(postDTO, hashtags);
        return postRepository.save(post).getId();
    }

    @Transactional
    public Long updatePost(PostDTO postDTO) throws PostException {
        Optional<Post> optionalPost = postRepository.findById(postDTO.getId());
        if (optionalPost.isEmpty()) {
            throw new PostException("Post not found with id: " + postDTO.getId());
        }
        Post post = optionalPost.get();

        Set<Hashtag> hashtags = getOrCreateHashtags(postDTO.getHashtags());
        post.setText(postDTO.getText());
        post.setImageData(postDTO.getImageData());
        post.setHashtags(hashtags);
        post.setVisibility(Visibility.valueOf(postDTO.getVisibility().toUpperCase()));

        return postRepository.save(post).getId();
    }


    public void deletePost(Long id) throws PostException {
        Optional<Post> optionalPost = postRepository.findById(id);
        if (optionalPost.isEmpty()) {
            throw new PostException("Post not found with id: " + id);
        }
        postRepository.deleteById(id);
    }

    public PostDTO findById(Long id) throws PostException {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostException("Post not found with id: " + id));
        return PostBuilder.generateDTOFromEntity(post);
    }

    public List<PostDTO> search(String text, String hashtag, String authorEmail) {
        List<Post> posts = postRepository.findAll();
        return posts.stream()
                .filter(p -> text == null || p.getText().toLowerCase().contains(text.toLowerCase()))
                .filter(p -> hashtag == null ||
                        p.getHashtags().stream().anyMatch(h -> h.getName().equalsIgnoreCase(hashtag.trim())))
                .filter(p -> authorEmail == null || p.getAuthorEmail().equalsIgnoreCase(authorEmail.trim()))
                .sorted(Comparator.comparing(Post::getCreatedAt).reversed())
                .map(PostBuilder::generateDTOFromEntity)
                .collect(Collectors.toList());
    }

    public List<PostDTO> filterByText(String text) {
        return postRepository.findByTextContainingIgnoreCaseOrderByCreatedAtDesc(text)
                .stream()
                .map(PostBuilder::generateDTOFromEntity)
                .collect(Collectors.toList());
    }

    public List<PostDTO> filterByAuthor(String authorEmail) {
        return postRepository.findByAuthorEmailOrderByCreatedAtDesc(authorEmail)
                .stream()
                .map(PostBuilder::generateDTOFromEntity)
                .collect(Collectors.toList());
    }

    public List<PostDTO> filterByHashtag(String hashtag) {
        Optional<Hashtag> optionalHashtag = hashtagRepository.findByName(hashtag.toLowerCase());
        if (optionalHashtag.isEmpty()) {
            return Collections.emptyList();
        }

        List<Post> posts = postRepository.findByHashtagsContainingOrderByCreatedAtDesc(optionalHashtag.get());
        return posts.stream()
                .map(PostBuilder::generateDTOFromEntity)
                .collect(Collectors.toList());
    }


    private Set<Hashtag> getOrCreateHashtags(List<String> hashtagNames) {
        if (hashtagNames == null || hashtagNames.isEmpty()) return new HashSet<>();

        return hashtagNames.stream()
                .map(name -> name.replace("#", "").toLowerCase())
                .map(name -> hashtagRepository.findByName(name)
                        .orElseGet(() -> hashtagRepository.save(Hashtag.builder().name(name).build())))
                .collect(Collectors.toSet());
    }

}
