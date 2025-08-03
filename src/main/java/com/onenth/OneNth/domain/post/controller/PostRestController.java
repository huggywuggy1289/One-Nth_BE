package com.onenth.OneNth.domain.post.controller;

import com.onenth.OneNth.domain.member.dto.MemberResponseDTO;
import com.onenth.OneNth.domain.member.entity.Member;
import com.onenth.OneNth.domain.member.service.memberService.MemberCommandService;
import com.onenth.OneNth.domain.region.entity.Region;
import com.onenth.OneNth.domain.post.dto.PostDetailResponseDTO;
import com.onenth.OneNth.domain.post.dto.PostListResponseDTO;
import com.onenth.OneNth.domain.post.dto.PostSaveRequestDTO;
import com.onenth.OneNth.domain.post.dto.PostSaveResponseDTO;
import com.onenth.OneNth.domain.post.entity.enums.PostType;
import com.onenth.OneNth.domain.post.service.PostCommandService;
import com.onenth.OneNth.domain.post.service.PostQueryService;
import com.onenth.OneNth.domain.region.repository.RegionRepository;
import com.onenth.OneNth.global.apiPayload.ApiResponse;
import com.onenth.OneNth.global.apiPayload.code.status.SuccessStatus;
import com.onenth.OneNth.global.auth.annotation.AuthUser;
import com.onenth.OneNth.global.external.kakao.dto.GeoCodingResult;
import com.onenth.OneNth.global.external.kakao.service.GeoCodingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Tag(name = "게시글 관련 API", description = "생활꿀팁/할인정보/우리동네맛집 게시글 API")
@RestController
@RequestMapping("/api/post")
@RequiredArgsConstructor
public class PostRestController {

    private final PostCommandService postCommandService;
    private final GeoCodingService geoCodingService;
    private final RegionRepository regionRepository;
    private final PostQueryService postQueryService;
    private final MemberCommandService memberCommandService;

    @Operation(
            summary = "게시글 등록 API",
            description = """
    생활꿀팁/할인정보/우리동네맛집 게시판의 게시글을 등록하는 API입니다.
    - 쿼리 파라미터 'postType'에 '게시글 종류'를 명시합니다.('DISCOUNT'(할인정보),'RESTAURANT'(우리동네맛집),'LIFE_TIP'(생활꿀팁))
    - 생활꿀팁의 경우, 'post' 필드에 제목, 내용, 링크 정보를 JSON 문자열로 포함해야 합니다.
    - 할인정보/우리동네맛집의 경우, 'post' 필드에 제목, 내용, 주소, 장소명을 JSON 문자열로 포함해야 합니다.
    - 해당 게시글에 필요한 정보가 아닌 경우, 모두 자동 NULL 값 처리됩니다.
    - 'images' 필드에는 최대 5장까지 게시글 이미지를 선택적으로 첨부할 수 있습니다.
    """
    )
    // multipart/form-data 요청 처리 (이미지 첨부 가능)
    @PostMapping(value = "/{postType}", consumes = "multipart/form-data")
    public ApiResponse<PostSaveResponseDTO> saveMultipart(
            @PathVariable PostType postType,
            @AuthUser Long memberId,
            @RequestPart("post") PostSaveRequestDTO requestDto,
            @RequestPart(value = "images", required = false) List<MultipartFile> images
    ) {
        return saveCommon(postType, memberId, requestDto, images);
    }

    // application/json 요청 처리 (이미지 없이 JSON 데이터만)
    @PostMapping(value = "/{postType}", consumes = "application/json")
    public ApiResponse<PostSaveResponseDTO> saveJson(
            @PathVariable PostType postType,
            @AuthUser Long memberId,
            @RequestBody PostSaveRequestDTO requestDto
    ) {
        return saveCommon(postType, memberId, requestDto, null);
    }

    // 공통 로직 분리
    private ApiResponse<PostSaveResponseDTO> saveCommon(
            PostType postType,
            Long memberId,
            PostSaveRequestDTO requestDto,
            List<MultipartFile> images
    ) {
        // 사진 최대 5장 제한
        if (images != null && images.size() > 5) {
            throw new IllegalArgumentException("이미지는 최대 5장까지 업로드할 수 있습니다.");
        }

        // postType이 LIFE_TIP일 경우 좌표값, 주소, 장소명 제거
        if (postType == PostType.LIFE_TIP) {
            requestDto.setLatitude(null);
            requestDto.setLongitude(null);
            requestDto.setAddress(null);
            requestDto.setPlaceName(null);
        }

        // 주소가 있고, postType이 LIFE_TIP이 아닌 경우만 좌표, 장소명, 지역 ID 조회
        if (requestDto.getAddress() != null &&
                postType != PostType.LIFE_TIP) {

            try {
                GeoCodingResult result = geoCodingService.getCoordinatesFromAddress(requestDto.getAddress());
                requestDto.setLatitude(result.getLatitude());
                requestDto.setLongitude(result.getLongitude());
                requestDto.setRegionName(result.getRegionName());

                // 기존 ifPresent → 다수 결과 예외 방지
                List<Region> regions = regionRepository.findByRegionNameContaining(result.getRegionName());
                if (!regions.isEmpty()) {
                    requestDto.setRegionId(regions.get(0).getId()); // 가장 첫 번째 결과를 사용
                }

            } catch (Exception e) {
                throw new RuntimeException("좌표 조회 실패: " + e.getMessage(), e);
            }

        } else {
            requestDto.setLatitude(null);
            requestDto.setLongitude(null);
        }

        // postType이 RESTAURANT이거나 DISCOUNT일 경우 링크값 제거
        if (postType == PostType.RESTAURANT || postType == PostType.DISCOUNT) {
            requestDto.setLink(null);
        }

        Long postId = postCommandService.save(requestDto, postType, memberId, images);

        PostSaveResponseDTO response = PostSaveResponseDTO.builder()
                .postId(postId)
                .createdAt(LocalDateTime.now())
                .build();

        return ApiResponse.of(SuccessStatus._OK, response);
    }


    @Operation(
            summary = "게시글 목록 조회 API",
            description = """
    게시판 종류(postType), 지역명(regionName), 키워드(keyword)를 이용한 게시글 목록 조회 API입니다.
    응답으로 '게시글Id', '제목', '본문 미리보기(최대 50자)', '댓글수', '좋아요수', '조회수', '스크랩 상태', '생성 시간'을 제공합니다.
    
    - postType은 필수입니다: 'LIFE_TIP', 'DISCOUNT', 'RESTAURANT'
    - regionName와 keyword는 선택입니다. (단 지역명의 경우, LIFE_TIP에서는 무시됩니다)
    - 지역명과 키워드를 함께 입력할 시, 모두 필터링된 게시글로 목록이 조회됩니다.
    - page의 기본값은 0, size의 기본값은 10입니다. 변경하고자 할 경우에만 입력해주세요.
    """
    )
    @GetMapping("/search")
    public ApiResponse<List<PostListResponseDTO>> searchPosts(
            @AuthUser Long memberId,
            @RequestParam PostType postType,
            @RequestParam(required = false) String regionName,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        // LIFE_TIP은 지역명 무시
        if (postType == PostType.LIFE_TIP) {
            regionName = null;
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<PostListResponseDTO> pageResult = postQueryService.getPostList(memberId, postType, regionName, keyword, pageable);
        List<PostListResponseDTO> contentList = pageResult.getContent();

        return ApiResponse.onSuccess(contentList);
    }


    @Operation(
            summary = "게시글 상세 조회 API",
            description = """
    postId를 이용한 게시글의 상세 정보 조회 API입니다. 응답으로 'postId', '작성자 닉네임', '지역명'(LIFE_TIP의 경우, null), '제목', '내용', '이미지 url', '스크랩 상태', '공감,댓글,조회수', '생성시간'을 제공합니다.
    
    - 쿼리파라미터 postId에 게시글 ID를 전달합니다.
    """
    )
    @GetMapping("/{postId}")
    public ApiResponse<PostDetailResponseDTO> getPostDetail(
            @AuthUser Long memberId,
            @PathVariable("postId") Long postId
            ) {
        postCommandService.increaseViewCount(postId);
        PostDetailResponseDTO response = postQueryService.getPostDetail(postId, memberId);
        return ApiResponse.onSuccess(response);
    }

    @Operation(
            summary = "게시글 수정 API",
            description = """
    postId를 이용한 게시글 수정 API입니다. 본인이 작성한 게시글에 대해서만 수정 권한이 있습니다. 수정이 가능한 항목은 '제목, '내용', '주소', '장소명', '링크', '이미지'입니다.
    
    - 쿼리파라미터 postId에 게시글 ID를 전달합니다.
    - 생활꿀팁의 경우, 'post' 필드에 변경하고자하는 제목, 내용, 링크 정보를 JSON 문자열로 포함해야 합니다.
    - 할인정보/우리동네맛집의 경우, 'post' 필드에 변경하고자하는 제목, 내용, 주소, 장소명을 JSON 문자열로 포함해야 합니다.
      (변경을 원하지 않는 항목의 경우, 꼭 "" 빈문자열로 전달해주세요!!! "string"으로 전달할 시 그대로 반영됩니다ㅠㅠ)
    - 해당 게시글에 필요한 정보가 아닌 경우, 모두 자동 NULL 값 처리됩니다.
    - 'images' 필드에는 최대 5장까지 게시글 이미지를 선택적으로 재업로드할 수 있습니다.
 
    """
    )
    @PatchMapping(value = "/{postId}", consumes = "multipart/form-data")
    public ApiResponse<PostSaveResponseDTO> updatePostWithImages(
            @PathVariable Long postId,
            @AuthUser Long memberId,
            @RequestPart("post") PostSaveRequestDTO requestDto,
            @RequestPart(value = "images", required = false) List<MultipartFile> images
    ) {
        Long updatedPostId = postCommandService.update(postId, requestDto, memberId, images);
        return ApiResponse.of(SuccessStatus._OK, PostSaveResponseDTO.builder()

                .postId(updatedPostId)
                .createdAt(LocalDateTime.now())
                .build());
    }


    @Operation(
            summary = "게시글 삭제 API",
            description = """
    postId를 이용한 게시글 삭제 API입니다. 본인이 작성한 게시글에 대해서만 삭제 권한이 있습니다.
    
    - 쿼리파라미터 postId에 게시글 ID를 전달합니다.
    """
    )
    @DeleteMapping("/{postId}")
    public ApiResponse<Void> deletePost(
            @PathVariable Long postId,
            @AuthUser Long memberId
    ) {
        postCommandService.delete(postId, memberId);
        return ApiResponse.of(SuccessStatus._OK, null);
    }

    @Operation(
            summary = "게시글 공감 등록 API",
            description = """
    postId를 이용한 게시글 공감 등록 API입니다.
    
    - 쿼리파라미터 postId에 게시글 ID를 전달합니다.
    - 중복 공감은 되지 않습니다.
    """
    )
    @PostMapping("/{postId}/like")
    public ApiResponse<MemberResponseDTO.AddScrapOrLikeResponseDTO> addLike(
            @PathVariable Long postId,
            @AuthUser Long memberId
    ) {
        MemberResponseDTO.AddScrapOrLikeResponseDTO response = memberCommandService.addLike(memberId, postId);
        return ApiResponse.onSuccess(response);
    }

    @Operation(
            summary = "게시글 공감 삭제 API",
            description = """
    postId를 이용한 게시글 공감 삭제 API입니다.
    
    - 쿼리파라미터 postId에 게시글 ID를 전달합니다.
    """
    )
    @DeleteMapping("/{postId}/like")
    public ApiResponse<MemberResponseDTO.CancelScrapOrLikeResponseDTO> cancelLike(
            @PathVariable Long postId,
            @AuthUser Long memberId
    ) {
        MemberResponseDTO.CancelScrapOrLikeResponseDTO response = memberCommandService.cancelLike(memberId, postId);
        return ApiResponse.onSuccess(response);
    }

    @Operation(
            summary = "게시글 스크랩 등록 API",
            description = """
    postId를 이용한 게시글 스크랩 등록 API입니다.
    
    - 쿼리파라미터 postId에 게시글 ID를 전달합니다.
    - 중복 스크랩은 되지 않습니다.
    """
    )
    @PostMapping("/{postId}/scrap")
    public ApiResponse<MemberResponseDTO.AddScrapOrLikeResponseDTO> addScrap(
            @PathVariable Long postId,
            @AuthUser Long memberId
    ) {
        MemberResponseDTO.AddScrapOrLikeResponseDTO response = memberCommandService.addScrap(memberId, postId);
        return ApiResponse.onSuccess(response);
    }

    @Operation(
            summary = "게시글 스크랩 삭제 API",
            description = """
    postId를 이용한 게시글 스크랩 삭제 API입니다.
    
    - 쿼리파라미터 postId에 게시글 ID를 전달합니다.
    """
    )
    @DeleteMapping("/{postId}/scrap")
    public ApiResponse<MemberResponseDTO.CancelScrapOrLikeResponseDTO> cancelScrap(
            @PathVariable Long postId,
            @AuthUser Long memberId
    ) {
        MemberResponseDTO.CancelScrapOrLikeResponseDTO response = memberCommandService.cancelScrap(memberId, postId);
        return ApiResponse.onSuccess(response);
    }
}
