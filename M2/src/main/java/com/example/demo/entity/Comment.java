package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @ManyToOne
    @JoinColumn(
            name = "post_id",
            nullable = false
    )
    private Post post;
}
