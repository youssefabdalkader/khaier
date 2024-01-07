package com.example.khaier.mapper;

import com.example.khaier.dto.request.PostRequestDto;
import com.example.khaier.entity.post.Post;
import com.example.khaier.entity.post.PostImage;
import com.example.khaier.service.Impl.PostImageService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@Component
@AllArgsConstructor
public class PostRequestDtoToPostMapper implements Function<PostRequestDto, Post> {
    private final PostImageService postImageService;

    @Override
    public Post apply(PostRequestDto postRequestDto) {
        List<PostImage> postImageList = new ArrayList<>();

        if (postRequestDto.image() != null) {
            for (MultipartFile image : postRequestDto.image()) {
                try {
                    PostImage postImage = postImageService.uploadImage(image);
                    postImageList.add(postImage);
                } catch (IOException e) {
                    throw new RuntimeException("Image can't be uploaded", e);
                }
            }
        }

        return Post.builder()
                .postContent(postRequestDto.postContent())
                .date(LocalDateTime.now())
                .comments(new ArrayList<>())
                .likes(new ArrayList<>())
                .images(postImageList)
                .build();
    }
}
