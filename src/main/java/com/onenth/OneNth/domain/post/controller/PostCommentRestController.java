package com.onenth.OneNth.domain.post.controller;

import com.onenth.OneNth.domain.post.converter.PostCommentConverter;
import com.onenth.OneNth.domain.post.dto.PostCommentResponseDTO;
import com.onenth.OneNth.domain.post.dto.PostCommentSaveRequestDTO;
import com.onenth.OneNth.domain.post.entity.PostComment;
import com.onenth.OneNth.domain.post.service.PostCommentCommandService;
import com.onenth.OneNth.domain.post.service.PostCommentQueryService;
import com.onenth.OneNth.global.apiPayload.ApiResponse;
import com.onenth.OneNth.global.apiPayload.code.status.SuccessStatus;
import com.onenth.OneNth.global.auth.annotation.AuthUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "게시글 댓글 관련 API", description = "생활꿀팁/할인정보/우리동네맛집 게시글 댓글 API")
@RestController
@RequestMapping("/api/post/{postId}/comment")
@RequiredArgsConstructor
public class PostCommentRestController {

    private final PostCommentCommandService postCommentCommandService;
    private final PostCommentQueryService postCommentQueryService;

    @Operation(
            summary = "게시글 댓글 등록 API",
            description = """
    생활꿀팁/할인정보/우리동네맛집 게시글의 댓글을 등록하는 API입니다.
    - 쿼리 파라미터 'postId'에 '게시글 Id'를 명시합니다.
    - 댓글 내용(content)을 JSON 문자열로 포함해야 합니다.
    """)
    @PostMapping
    public ApiResponse<PostCommentResponseDTO> createComment(
            @PathVariable Long postId,
            @AuthUser Long memberId,
            @RequestBody PostCommentSaveRequestDTO requestDTO
    ) {
        Long commentId = postCommentCommandService.saveComment(postId, memberId, requestDTO);
        PostComment comment = postCommentCommandService.findById(commentId); // 새로 저장된 댓글 조회

        PostCommentResponseDTO responseDto = PostCommentConverter.toResponseDTO(comment);
        return ApiResponse.of(SuccessStatus._OK, responseDto);
    }

    @Operation(summary = "게시글 댓글 삭제 API",
            description = """
    생활꿀팁/할인정보/우리동네맛집 게시글의 댓글을 삭제하는 API입니다.
    - 쿼리 파라미터 'postId'와 'commentId'를 명시합니다.
    - 본인이 작성한 게시글에 대해서만 삭제 권한이 있습니다.
    """)
    @DeleteMapping("/{commentId}")
    public ApiResponse<Void> deleteComment(
            @PathVariable Long postId,
            @PathVariable Long commentId,
            @AuthUser Long memberId
    ) {
        postCommentCommandService.deleteComment(commentId, memberId);
        return ApiResponse.of(SuccessStatus._OK, null);
    }


    @Operation(summary = "게시글 댓글 목록 조회 API",
            description = """
    생활꿀팁/할인정보/우리동네맛집 게시글의 댓글 목록을 조회하는 API입니다.
    - 쿼리 파라미터 'postId'를 명시합니다.
    - 해당 게시글에 달린 댓글의 id, 내용, 작성자 닉네임, 작성자 id, 생성일자가 결과값으로 제공됩니다.
    """)
    @GetMapping("/list")
    public ApiResponse<List<PostCommentResponseDTO>> getComments(
            @PathVariable Long postId
    ) {
        List<PostCommentResponseDTO> comments = postCommentQueryService.getCommentsByPostId(postId);
        return ApiResponse.of(SuccessStatus._OK, comments);
    }
}
