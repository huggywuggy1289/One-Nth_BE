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
import com.onenth.OneNth.domain.post.repository.tagRepository.PostTagRepository;
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
    private final PostTagRepository PostTagRepository;

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

        boolean isTagSearch = keyword != null && keyword.startsWith("#");
        String searchKeyword = isTagSearch ? keyword.substring(1) : keyword;

        if (postType == PostType.LIFE_TIP) {
            if(isTagSearch) {
                posts = postRepository.findByPostTypeAndTagName(postType, searchKeyword, pageable);
            } else {
                posts = postRepository.findByPostTypeAndKeyword(postType, keyword, pageable);
            }
        } else {
            if (regionName != null && !regionName.isBlank()) {
                if (isTagSearch) {
                    // 태그 + 지역명 검색
                    posts = postRepository.findByPostTypeAndRegionNameAndTagName(postType, regionName, searchKeyword, pageable);
                } else {
                    // 제목/내용 + 지역명 검색
                    posts = postRepository.findByPostTypeAndRegionNameAndKeyword(postType, regionName, searchKeyword, pageable);
                }
            } else {
                if (isTagSearch) {
                    // 태그 검색 (지역 무관)
                    posts = postRepository.findByPostTypeAndTagName(postType, searchKeyword, pageable);
                } else {
                    // 제목/내용 검색 (지역 무관)
                    posts = postRepository.findByPostTypeAndKeyword(postType, searchKeyword, pageable);
                }
            }
        }

        return posts.map(post -> toPostListDTO(post, memberId));
    }

    private PostListResponseDTO toPostListDTO(Post post, Long memberId) {
        boolean scrapStatus = ScrapRepository.existsByPostIdAndMemberId(post.getId(), memberId);
        List<String> imageUrls = imageRepository.findUrlsByPost(post);

        return PostListResponseDTO.builder()
                .postId(post.getId())
                .title(post.getTitle())
                .contentPreview(createPreview(post.getContent()))
                .commentCount(post.getPostComment().size())
                .likeCount(post.getLike().size())
                .viewCount(post.getViewCount())
                .scrapStatus(scrapStatus)
                .imageUrls(imageUrls)
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
        String profileImageUrl = post.getMember().getProfileImageUrl();
        List<String> tags = PostTagRepository.findTagNamesByPostId(post.getId());

        PostDetailResponseDTO.PostDetailResponseDTOBuilder builder = PostDetailResponseDTO.builder()
                .postId(post.getId().toString())
                .nickname(post.getMember().getNickname())
                .profileImageUrl(profileImageUrl)
                .regionName(post.getRegion() != null ? post.getRegion().getRegionName() : null)
                .title(post.getTitle())
                .content(post.getContent())
                .imageUrls(imageUrls)
                .commentCount(commentCount)
                .likeCount(likeCount)
                .scrapStatus(scrapStatus)
                .viewCount(post.getViewCount())
                .createdAt(post.getCreatedAt())
                .tags(tags);

        // 타입별 추가 정보
        if (post.getPostType() == PostType.DISCOUNT || post.getPostType() == PostType.RESTAURANT) {
            builder.address(post.getAddress())
                    .placeName(post.getPlaceName());
        } else if (post.getPostType() == PostType.LIFE_TIP) {
            builder.link(post.getLink());
        }

        return builder.build();
    }
}
