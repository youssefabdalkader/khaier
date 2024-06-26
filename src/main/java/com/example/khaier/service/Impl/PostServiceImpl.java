package com.example.khaier.service.Impl;

import com.example.khaier.dto.request.PostRequestDto;
import com.example.khaier.dto.response.PostResponseDto;
import com.example.khaier.entity.Post;
import com.example.khaier.entity.User;
import com.example.khaier.helper.PostHelper;
import com.example.khaier.helper.UserHelper;
import com.example.khaier.mapper.PostRequestDtoToPostMapper;
import com.example.khaier.mapper.PostToPostResponseDtoMapper;
import com.example.khaier.repository.PostRepository;
import com.example.khaier.service.PostService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
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
    public List<PostResponseDto> getAllPosts(Pageable pageable) {
        User user = (User)  SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Page<Post> posts = postRepository.findAll(pageable);
        return posts.getContent()
                .stream()
                .map(post -> postToPostResponseDtoMapper.apply(post, user.getUserId()))
                .toList();
    }

    @Override
    public PostResponseDto addNewPost(PostRequestDto postDto) {
        User authUser=  (User)  SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user=userHelper.findUserByIdOrThrowNotFoundException(authUser.getUserId());
        Post post = requestDtoToPostMapper.apply(postDto);
        post.setUser(user);
        post = postRepository.save(post);
        return postToPostResponseDtoMapper.apply(post,user.getUserId());
    }

    @Override
    public PostResponseDto getPostById(Long postId) {
        User authUser=  (User)  SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Post post =postHelper.findPostByIdOrThrowNotFound(postId);
        return postToPostResponseDtoMapper.apply(post,authUser.getUserId());
    }
}
