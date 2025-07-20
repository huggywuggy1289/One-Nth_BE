package com.onenth.OneNth.domain.post.service;

import com.onenth.OneNth.domain.post.dto.PostSaveRequestDTO;

public interface PostCommandService {
    Long save(PostSaveRequestDTO requestDto);
}
