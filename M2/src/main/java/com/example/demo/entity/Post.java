package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "posts")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(
            name = "id",
            updatable = false
    )
    private Long id;

    @Column(
            name = "text",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String text;

    @Lob
    @Column(
            name = "image_data",
            columnDefinition = "LONGBLOB"
    )
    private byte[] imageData;

    @JsonFormat(pattern = "MM-dd-yyyy HH:mm:ss")
    @DateTimeFormat(pattern = "MM-dd-yyyy HH:mm:ss")
    @Column(
            name = "created_at",
            nullable = false
    )
    private LocalDateTime createdAt;

    @Column(
            name = "author_email",
            nullable = false
    )
    private String authorEmail;

    @ManyToMany(cascade = {CascadeType.MERGE})
    @JoinTable(
            name = "post_hashtags",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "hashtag_id")
    )
    private Set<Hashtag> hashtags;

    @Enumerated(EnumType.STRING)
    @Column(name = "visibility", nullable = false)
    private Visibility visibility;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Comment> comments;
}
