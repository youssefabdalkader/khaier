package com.example.khaier.service.Impl;

import com.example.khaier.dto.request.PostRequestDto;
import com.example.khaier.dto.response.PostResponseDto;
import com.example.khaier.entity.post.Post;
import com.example.khaier.entity.user.User;
import com.example.khaier.helper.PostHelper;
import com.example.khaier.helper.UserHelper;
import com.example.khaier.mapper.PostRequestDtoToPostMapper;
import com.example.khaier.mapper.PostToPostResponseDtoMapper;
import com.example.khaier.repository.post.PostRepository;
import com.example.khaier.service.PostService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final PostRequestDtoToPostMapper requestDtoToPostMapper;
    private final PostToPostResponseDtoMapper postToPostResponseDtoMapper;
    private final UserHelper userHelper;
    private final PostHelper postHelper;
    @Override
    public List<PostResponseDto> getAllPosts(Long userId) {
        List<Post> posts = postRepository.findAll();
        return posts.stream().map(post->postToPostResponseDtoMapper.apply(post,userId)).toList();
    }

    @Override
    public PostResponseDto addNewPost(PostRequestDto postDto,Long userId) {
        User user=userHelper.checkUserIsExistOrThrowException(userId);
        Post post = requestDtoToPostMapper.apply(postDto);
        post.setUser(user);
        post = postRepository.save(post);
        return postToPostResponseDtoMapper.apply(post,userId);
    }

    @Override
    public PostResponseDto getPostById(Long postId,Long userId) {
        Post post =postHelper.checkPostExistOrThrowException(postId);
        return postToPostResponseDtoMapper.apply(post,userId);
    }
}
