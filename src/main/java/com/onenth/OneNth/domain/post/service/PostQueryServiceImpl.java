package com.onenth.OneNth.domain.post.service;

import com.onenth.OneNth.domain.post.dto.PostDetailResponseDTO;
import com.onenth.OneNth.domain.post.dto.PostListResponseDTO;
import com.onenth.OneNth.domain.post.entity.Post;
import com.onenth.OneNth.domain.post.entity.enums.PostType;
import com.onenth.OneNth.domain.post.repository.*;
import com.onenth.OneNth.domain.post.repository.commentRepository.CommentRepository;
import com.onenth.OneNth.domain.post.repository.imageRepository.ImageRepository;
import com.onenth.OneNth.domain.post.repository.likeRepository.LikeRepository;
import com.onenth.OneNth.domain.post.repository.scrapRepository.ScrapRepository;
import com.onenth.OneNth.global.apiPayload.code.status.ErrorStatus;
import com.onenth.OneNth.global.apiPayload.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostQueryServiceImpl implements PostQueryService {

    private final PostRepository postRepository;
    private final ImageRepository imageRepository;
    private final CommentRepository CommentRepository;
    private final LikeRepository LikeRepository;
    private final ScrapRepository ScrapRepository;

    @Override
    public Page<PostListResponseDTO> getPostList(
            Long memberId,
            PostType postType,
            String regionName,
            String keyword,
            Pageable pageable
    ) {
        // LIFE_TIP은 지역 없이, 나머지는 조건에 따라 regionName 처리
        Page<Post> posts;

        if (postType == PostType.LIFE_TIP) {
            posts = postRepository.findByPostTypeAndKeyword(postType, keyword, pageable);
        } else {
            if (regionName != null && !regionName.isBlank()) {
                posts = postRepository.findByPostTypeAndRegionNameAndKeyword(
                        postType, regionName, keyword, pageable);
            } else {
                posts = postRepository.findByPostTypeAndKeyword(postType, keyword, pageable);
            }
        }

        return posts.map(post -> toPostListDTO(post, memberId));
    }

    private PostListResponseDTO toPostListDTO(Post post, Long memberId) {
        boolean scrapStatus = ScrapRepository.existsByPostIdAndMemberId(post.getId(), memberId);

        return PostListResponseDTO.builder()
                .postId(post.getId())
                .title(post.getTitle())
                .contentPreview(createPreview(post.getContent()))
                .commentCount(post.getPostComment().size())
                .likeCount(post.getLike().size())
                .viewCount(post.getViewCount())
                .scrapStatus(scrapStatus)
                .createdAt(post.getCreatedAt().toString())
                .build();
    }

    private String createPreview(String content) {
        return content.length() > 50 ? content.substring(0, 50) + "..." : content;
    }

    public PostDetailResponseDTO getPostDetail(Long postId, Long memberId) {
        Post post = postRepository.findByIdWithMemberAndRegion(postId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.NOT_FOUND_POST));

        int commentCount = (int) CommentRepository.countByPost(post);
        int likeCount = (int) LikeRepository.countByPost(post);
        boolean scrapStatus = ScrapRepository.existsByPostIdAndMemberId(postId, memberId);
        List<String> imageUrls = imageRepository.findUrlsByPost(post);

        return PostDetailResponseDTO.builder()
                .postId(post.getId().toString())
                .nickname(post.getMember().getNickname())
                .regionName(post.getRegion() != null ? post.getRegion().getRegionName() : null)
                .title(post.getTitle())
                .content(post.getContent())
                .imageUrls(imageUrls)
                .commentCount(commentCount)
                .likeCount(likeCount)
                .scrapStatus(scrapStatus)
                .viewCount(post.getViewCount())
                .createdAt(post.getCreatedAt())
                .build();
    }
}
