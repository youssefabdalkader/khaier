package com.example.khaier.service;

import com.example.khaier.dto.response.LikeResponseDto;

import java.util.List;

public interface LikeService {
    LikeResponseDto addOrRemoveLike(Long postId);
    List<LikeResponseDto>findLikesByPostId(Long postId);
}
