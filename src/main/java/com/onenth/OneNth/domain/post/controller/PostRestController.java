package com.onenth.OneNth.domain.post.controller;

import com.onenth.OneNth.domain.post.dto.PostSaveRequestDTO;
import com.onenth.OneNth.domain.post.dto.PostSaveResponseDTO;
import com.onenth.OneNth.domain.post.entity.enums.PostType;
import com.onenth.OneNth.domain.post.service.PostCommandService;
import com.onenth.OneNth.global.apiPayload.ApiResponse;
import com.onenth.OneNth.global.apiPayload.code.status.SuccessStatus;
import com.onenth.OneNth.global.auth.annotation.AuthUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class PostRestController {

    private final PostCommandService postCommandService;

    @Operation(
            summary = "게시글 등록을 위한 API",
            description = "새 게시글을 등록합니다."
    )

    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "COMMON200",
                    description = "OK, 성공",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "COMMON400",
                    description = "잘못된 요청입니다.",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            )
    })

    @PostMapping(value = "/api/post/{postType}", consumes = "multipart/form-data")
    public ApiResponse<PostSaveResponseDTO> save(
            @PathVariable PostType postType,
            @Parameter(hidden = true) @AuthUser Long memberId,
            @RequestPart("post") PostSaveRequestDTO requestDto,
            @RequestPart(value = "images", required = false) List<MultipartFile> images
            ){

        // 사진 최대 5장 제한
        if (images != null && images.size() > 5) {
            throw new IllegalArgumentException("이미지는 최대 5장까지 업로드할 수 있습니다.");
        }

        // postType이 LIFE_TIP일 경우 좌표값 제거
        if (postType == PostType.LIFE_TIP) {
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

        return ApiResponse.of(SuccessStatus._OK, response);}
}
