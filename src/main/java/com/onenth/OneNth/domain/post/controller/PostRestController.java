package com.onenth.OneNth.domain.post.controller;

import com.onenth.OneNth.domain.post.dto.PostSaveRequestDTO;
import com.onenth.OneNth.domain.post.dto.PostSaveResponseDTO;
import com.onenth.OneNth.domain.post.service.PostCommandService;
import com.onenth.OneNth.global.apiPayload.ApiResponse;
import com.onenth.OneNth.global.apiPayload.code.status.SuccessStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

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

    @PostMapping("/api/post")
    public ApiResponse<PostSaveResponseDTO> save(@RequestBody PostSaveRequestDTO requestDto){
        Long postId = postCommandService.save(requestDto);

        PostSaveResponseDTO response = PostSaveResponseDTO.builder()
                .postId(postId)
                .createdAt(LocalDateTime.now())
                .build();

        return ApiResponse.of(SuccessStatus._OK, response);}
}
