package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "comment_reactions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentReaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
            name = "author_email",
            nullable = false
    )
    private String authorEmail;

    @Column(
            name = "comment_id",
            nullable = false
    )
    private Long commentId;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "reaction_type",
            nullable = false
    )
    private ReactionType reactionType;
}
