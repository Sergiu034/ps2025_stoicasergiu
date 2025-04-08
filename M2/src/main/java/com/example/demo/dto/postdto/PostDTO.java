package com.example.demo.dto.postdto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostDTO {

    private Long id;

    private String text;

    private byte[] imageData;

    private String  authorEmail;

    private LocalDateTime createdAt;

    private List<String> hashtags;

    private String visibility;

}