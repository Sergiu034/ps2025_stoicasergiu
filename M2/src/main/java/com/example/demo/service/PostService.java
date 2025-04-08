package com.example.demo.service;

import com.example.demo.builder.postbuilder.PostBuilder;
import com.example.demo.client.FriendshipClient;
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

    private final FriendshipClient friendshipClient;

    public List<PostDTO> findAllSortedByDateDesc() {
        return postRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(PostBuilder::generateDTOFromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public Long createPost(PostDTO postDTO, String authorEmail) {
        Set<Hashtag> hashtags = getOrCreateHashtags(postDTO.getHashtags());
        Post post = PostBuilder.generateEntityFromDTO(postDTO, hashtags);
        post.setAuthorEmail(authorEmail);
        return postRepository.save(post).getId();
    }

    @Transactional
    public Long updatePost(PostDTO postDTO, String requesterEmail) throws PostException {
        Optional<Post> optionalPost = postRepository.findById(postDTO.getId());
        if (optionalPost.isEmpty()) {
            throw new PostException("Post not found with id: " + postDTO.getId());
        }

        Post post = optionalPost.get();

        // Author can only edit their own post
        if (!Objects.equals(post.getAuthorEmail(), requesterEmail)) {
            throw new PostException("You can only edit your own posts.");
        }

        Set<Hashtag> hashtags = getOrCreateHashtags(postDTO.getHashtags());

        post.setText(postDTO.getText());
        post.setImageData(postDTO.getImageData());
        post.setHashtags(hashtags);
        post.setVisibility(Visibility.valueOf(postDTO.getVisibility().toUpperCase()));

        return postRepository.save(post).getId();
    }

    public void deletePost(Long id, String requesterEmail) throws PostException {
        Optional<Post> optionalPost = postRepository.findById(id);
        if (optionalPost.isEmpty()) {
            throw new PostException("Post not found with id: " + id);
        }

        Post post = optionalPost.get();

        if (!Objects.equals(post.getAuthorEmail(), requesterEmail)) {
            throw new PostException("You can only delete your own posts.");
        }

        postRepository.deleteById(id);
    }

    private boolean isVisibleTo(Post post, String requesterEmail, String jwtToken) {
        return post.getVisibility() == Visibility.PUBLIC
                || post.getAuthorEmail().equals(requesterEmail)
                || (post.getVisibility() == Visibility.FRIENDS_ONLY && isFriend(requesterEmail, post.getAuthorEmail(), jwtToken));
    }

    public List<PostDTO> filterByHashtag(String name, String requesterEmail, String jwtToken) {
        return hashtagRepository.findByName(name.toLowerCase())
                .map(hashtag -> postRepository.findByHashtagsContainingOrderByCreatedAtDesc(hashtag).stream()
                        .filter(post -> isVisibleTo(post, requesterEmail, jwtToken))
                        .map(PostBuilder::generateDTOFromEntity)
                        .toList())
                .orElse(Collections.emptyList());
    }

    public List<PostDTO> filterByText(String text, String requesterEmail, String jwtToken) {
        return postRepository.findByTextContainingIgnoreCaseOrderByCreatedAtDesc(text).stream()
                .filter(post -> isVisibleTo(post, requesterEmail, jwtToken))
                .map(PostBuilder::generateDTOFromEntity)
                .collect(Collectors.toList());
    }

    public List<PostDTO> filterByAuthor(String authorEmail, String requesterEmail, String jwtToken) {
        return postRepository.findByAuthorEmailOrderByCreatedAtDesc(authorEmail).stream()
                .filter(post -> isVisibleTo(post, requesterEmail, jwtToken))
                .map(PostBuilder::generateDTOFromEntity)
                .collect(Collectors.toList());
    }

    public PostDTO findByIdIfVisible(Long id, String requesterEmail, String jwtToken) throws PostException {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostException("Post not found with id: " + id));

        if (!isVisibleTo(post, requesterEmail, jwtToken)) {
            throw new PostException("You are not authorized to view this post.");
        }

        return PostBuilder.generateDTOFromEntity(post);
    }

    public List<PostDTO> search(String text, String hashtag, String authorEmail, String requesterEmail, String jwtToken) {
        List<Post> posts = postRepository.findAll();

        return posts.stream()
                .filter(p -> (text == null || p.getText().toLowerCase().contains(text.toLowerCase())))
                .filter(p -> (hashtag == null || p.getHashtags().stream().anyMatch(h -> h.getName().equalsIgnoreCase(hashtag))))
                .filter(p -> (authorEmail == null || Objects.equals(p.getAuthorEmail(), authorEmail)))
                .filter(p -> isVisibleTo(p, requesterEmail, jwtToken)) // visibility check
                .sorted(Comparator.comparing(Post::getCreatedAt).reversed())
                .map(PostBuilder::generateDTOFromEntity)
                .collect(Collectors.toList());
    }

    public List<PostDTO> findVisiblePostsForUser(String userEmail, String jwtToken) {
        List<Post> posts = postRepository.findAll();

        return posts.stream()
                .filter(post ->
                        post.getVisibility() == Visibility.PUBLIC
                                || (post.getVisibility() == Visibility.FRIENDS_ONLY &&
                                isFriend(userEmail, post.getAuthorEmail(), jwtToken))
                                || post.getAuthorEmail().equals(userEmail)
                )
                .sorted(Comparator.comparing(Post::getCreatedAt).reversed())
                .map(PostBuilder::generateDTOFromEntity)
                .collect(Collectors.toList());
    }

    private boolean isFriend(String userEmail, String authorEmail, String jwtToken) {
        try {
            List<String> friendEmails = friendshipClient.getFriendEmails(jwtToken);
            System.out.println("🔗 Friends of " + userEmail + ": " + friendEmails);
            return friendEmails.contains(authorEmail);
        } catch (Exception e) {
            System.out.println("❌ Error in isFriend: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
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
