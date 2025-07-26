package com.onenth.OneNth.domain.post.service;

import com.onenth.OneNth.domain.post.dto.PostSaveRequestDTO;
import com.onenth.OneNth.domain.post.entity.enums.PostType;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PostCommandService {
    Long save(PostSaveRequestDTO requestDto, PostType postType, Long memberId, List<MultipartFile> images);
}
