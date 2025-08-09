package com.onenth.OneNth.domain.post.service;

import com.onenth.OneNth.domain.member.entity.Member;
import com.onenth.OneNth.domain.member.repository.memberRepository.MemberRepository;
import com.onenth.OneNth.domain.post.converter.PostConverter;
import com.onenth.OneNth.domain.post.dto.PostSaveRequestDTO;
import com.onenth.OneNth.domain.post.entity.Post;
import com.onenth.OneNth.domain.post.entity.PostImage;
import com.onenth.OneNth.domain.post.entity.PostTag;
import com.onenth.OneNth.domain.post.entity.Tag;
import com.onenth.OneNth.domain.post.entity.enums.PostType;
import com.onenth.OneNth.domain.post.repository.imageRepository.ImageRepository;
import com.onenth.OneNth.domain.post.repository.PostRepository;
import com.onenth.OneNth.domain.post.repository.tagRepository.PostTagRepositoryCustom;
import com.onenth.OneNth.domain.post.repository.tagRepository.PostTagRepository;
import com.onenth.OneNth.domain.region.entity.Region;
import com.onenth.OneNth.domain.region.repository.RegionRepository;
import com.onenth.OneNth.global.apiPayload.code.status.ErrorStatus;
import com.onenth.OneNth.global.apiPayload.exception.GeneralException;
import com.onenth.OneNth.global.aws.s3.AmazonS3Manager;
import com.onenth.OneNth.global.external.kakao.dto.GeoCodingResult;
import com.onenth.OneNth.global.external.kakao.service.GeoCodingService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class PostCommandServiceImpl implements PostCommandService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final RegionRepository regionRepository;
    private final ImageRepository imageRepository;
    private final PostTagRepository postTagRepository;
    private final PostTagRepositoryCustom postTagRepositoryCustom;
    private final AmazonS3Manager amazonS3Manager;
    private final GeoCodingService geoCodingService;

    @Override
    @Transactional
    public Long save(PostSaveRequestDTO requestDto, PostType postType, Long memberId, List<MultipartFile> images) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        Region region = null;
        if (postType != PostType.LIFE_TIP) {
            if (requestDto.getRegionId() == null) {
                throw new IllegalArgumentException("지역 정보(regionId)가 없습니다.");
            }
            region = regionRepository.findById(Long.valueOf(requestDto.getRegionId()))
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 지역입니다."));
        }

        Post savedPost = postRepository.save(PostConverter.toEntity(requestDto, postType, member, region));

        // 이미지 저장
        if (images != null && !images.isEmpty()) {
            for (MultipartFile image : images) {
                String imageUrl = amazonS3Manager.uploadFile("post/" + UUID.randomUUID(), image);
                imageRepository.save(PostImage.builder()
                        .post(savedPost)
                        .imageUrl(imageUrl)
                        .build());
            }
        }

        // 태그 저장
        if (requestDto.getTags() != null && !requestDto.getTags().isEmpty()) {
            for (String tagName : requestDto.getTags()) {
                Tag tag = postTagRepository.findByName(tagName)
                        .orElseGet(() -> postTagRepository.save(Tag.builder()
                                .name(tagName)
                                .build()));

                PostTag postTag = PostTag.builder()
                        .post(savedPost)
                        .tag(tag)
                        .build();

                savedPost.getPostTag().add(postTag);
            }
        }

        return savedPost.getId();
    }

    // 조회수 증가 메서드
    @Transactional
    public void increaseViewCount(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.NOT_FOUND_POST));
        post.increaseViewCount();
    }

    @Transactional
    @Override
    public Long update(Long postId, PostSaveRequestDTO requestDto, Long memberId, List<MultipartFile> images) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));

        if (!post.getMember().getId().equals(memberId)) {
            throw new SecurityException("게시글 작성자만 수정할 수 있습니다.");
        }

        PostType postType = post.getPostType();

        if (postType != PostType.LIFE_TIP) {
            String address = requestDto.getAddress();
            if (address != null && !address.trim().isEmpty()) {
                GeoCodingResult result = geoCodingService.getCoordinatesFromAddress(address);
                if (result != null) {
                    requestDto.setLatitude(result.getLatitude());
                    requestDto.setLongitude(result.getLongitude());
                    requestDto.setRegionName(result.getRegionName());

                    // 기존 ifPresent → 예외 가능성 있음
                    List<Region> matchedRegions = regionRepository.findByRegionNameContaining(result.getRegionName());
                    if (!matchedRegions.isEmpty()) {
                        requestDto.setRegionId(matchedRegions.get(0).getId());
                    }
                }
            }
        }

        String title = isNullOrEmpty(requestDto.getTitle()) ? post.getTitle() : requestDto.getTitle();
        String content = isNullOrEmpty(requestDto.getContent()) ? post.getContent() : requestDto.getContent();

        String link;
        String address;
        String placeName;

        if (postType == PostType.LIFE_TIP) {
            // LIFE_TIP은 주소, 장소명 무조건 null, 링크는 들어온 값 or 기존 값 유지
            link = isNullOrEmpty(requestDto.getLink()) ? post.getLink() : requestDto.getLink();
            address = null;
            placeName = null;
        } else {
            // RESTAURANT, DISCOUNT는 link 무조건 null, 장소명 및 주소 들어온 값 or 기존 값 유지
            link = null;
            address = isNullOrEmpty(requestDto.getAddress()) ? post.getAddress() : requestDto.getAddress();
            placeName = isNullOrEmpty(requestDto.getPlaceName()) ? post.getPlaceName() : requestDto.getPlaceName();
        }

        post.update(title, content, link, address, placeName, requestDto.getLatitude(), requestDto.getLongitude(), requestDto.getRegionName());

        imageRepository.deleteAllByPost(post);
        if (images != null) {
            for (MultipartFile image : images) {
                String imageUrl = amazonS3Manager.uploadFile("post/" + UUID.randomUUID(), image);
                imageRepository.save(PostImage.builder().post(post).imageUrl(imageUrl).build());
            }
        }

        postTagRepositoryCustom.deleteAllByPost(post);

        if (requestDto.getTags() != null && !requestDto.getTags().isEmpty()) {
            for (String tagName : requestDto.getTags()) {
                Tag tag = postTagRepository.findByName(tagName)
                        .orElseGet(() -> postTagRepository.save(Tag.builder()
                                .name(tagName)
                                .build()));

                PostTag postTag = PostTag.builder()
                        .post(post)
                        .tag(tag)
                        .build();

                postTagRepositoryCustom.save(postTag);
                post.getPostTag().add(postTag);
            }
        }

        return post.getId();
    }

    private boolean isNullOrEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    @Transactional
    @Override
    public void delete(Long postId, Long memberId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));

        if (!post.getMember().getId().equals(memberId)) {
            throw new SecurityException("게시글 작성자만 삭제할 수 있습니다.");
        }

        postRepository.delete(post); // 연관 이미지, 댓글 등 cascade 또는 별도 삭제 필요
    }
}
