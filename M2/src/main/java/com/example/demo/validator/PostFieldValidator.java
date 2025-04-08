package com.example.demo.validator;

import com.example.demo.dto.postdto.PostDTO;

import java.util.ArrayList;
import java.util.List;

public class PostFieldValidator {

    public static List<String> validateInsertOrUpdate(PostDTO postDTO) {
        List<String> errors = new ArrayList<>();

        if (postDTO == null) {
            errors.add("postDTO is null");
            return errors;
        }

        if (postDTO.getText() == null || postDTO.getText().isBlank()) {
            errors.add("Post text cannot be null or empty");
        }

        if (postDTO.getAuthorEmail() == null) {
            errors.add("Author email is required");
        }

        if (postDTO.getHashtags() == null || postDTO.getHashtags().isEmpty()) {
            errors.add("At least one hashtag must be provided");
        }

        return errors;
    }
}
