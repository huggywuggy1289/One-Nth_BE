package com.onenth.OneNth.domain.member.controller;

import com.onenth.OneNth.domain.member.dto.MemberRequestDTO;
import com.onenth.OneNth.domain.member.dto.MemberResponseDTO;
import com.onenth.OneNth.domain.member.service.EmailVerificationService.EmailVerificationService;
import com.onenth.OneNth.domain.member.service.memberService.MemberCommandService;
import com.onenth.OneNth.domain.member.service.memberService.MemberQueryService;
import com.onenth.OneNth.global.apiPayload.ApiResponse;
import com.onenth.OneNth.global.auth.annotation.AuthUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@Tag(name = "회원 계정 관련 API",
        description = "일반 로그인/회원가입 계정 찾기 등 계정관리 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberRestController {

    private final MemberCommandService memberCommandService;
    private final EmailVerificationService emailVerificationService;
    private final MemberQueryService memberQueryService;

    /**
     * 일반 회원가입 API 구현
     */
    @Operation(
            summary = "일반 회원가입 API",
            description = "일반 회원가입을 진행합니다. 이메일 인증 후 회원 정보를 보내주세요."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON400", description = "잘못된 요청입니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @PostMapping("/signup")
    public ApiResponse<MemberResponseDTO.SignupResultDTO> signup(@RequestBody @Valid MemberRequestDTO.SignupDTO request) {
        return ApiResponse.onSuccess(memberCommandService.signupMember(request));
    }

    /**
     * 일반 로그인 API 구현
     */
    @Operation(
            summary = "일반 로그인 API",
            description = "일반 로그인을 진행합니다. 응답으로 JWT 토큰이 발급됩니다. 헤더에 담아서 인가에 사용하세요."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON400", description = "잘못된 요청입니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @PostMapping("/login")
    public ApiResponse<MemberResponseDTO.LoginResultDTO> login(@RequestBody @Valid MemberRequestDTO.LoginRequestDTO request) {
        return ApiResponse.onSuccess(memberCommandService.loginMember(request));
    }

    /**
     *  이메일 인증 후 비밀번호 재설정 API
     */
    @Operation(
            summary = "비밀번호 재설정 API",
            description = "비밀번호를 재설정합니다. 비밀번호를 재설정하기 위해서는 이메일 인증을 우선 진행해주세요."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON400", description = "잘못된 요청입니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @PostMapping("/password/reset")
    public ApiResponse<MemberResponseDTO.PasswordResetResultDTO> resetPassword(@RequestBody MemberRequestDTO.ResetPasswordRequestDTO request) {
        return ApiResponse.onSuccess(memberCommandService.resetPassword(request));
    }

    /**
     * 마이페이지 - 내가 스크랩한 글 조회 API 구현 (1최신순 0개씩 페이징)
     */
    @Operation(
            summary = "마이페이지- 내가 스크랩한 글 조회 API",
            description = "사용자가 스크랩한 글 조회 API 입니다. 현재 페이지 번호를 쿼리스트링으로 보내주세요."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON400", description = "잘못된 요청입니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @Parameters({
            @Parameter(name = "page", description = "페이지 번호(1부터 시작)", example = "1", required = true),
            @Parameter(name = "size", description = "페이지 당 미리보기 개수", example = "10", required = true)
    })
    @GetMapping("/mypage/scraps")
    public ApiResponse<MemberResponseDTO.PostListDTO> getScrappedPosts(
            @Parameter(hidden = true) @AuthUser Long memberId,
            @RequestParam(name="page", defaultValue = "1") Integer page,
            @RequestParam(name="size", defaultValue = "10") Integer size
    ) {
        return ApiResponse.onSuccess(memberQueryService.getScrappedPosts(memberId, page, size));
    }


    /**
     * 마이페이지 - 내가 공감한 글 조회 API 구현 (최신순 10개씩 페이징)
     */
    @Operation(
            summary = "마이페이지- 내가 공감한 글 조회 API",
            description = "사용자가 스크랩한 글 조회 API 입니다. 현재 페이지 번호를 쿼리스트링으로 보내주세요."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON400", description = "잘못된 요청입니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @Parameters({
            @Parameter(name = "page", description = "페이지 번호(1부터 시작)", example = "1", required = true),
            @Parameter(name = "size", description = "페이지 당 미리보기 개수", example = "10", required = true)
    })
    @GetMapping("/mypage/likes/")
    public ApiResponse<MemberResponseDTO.PostListDTO> getLikedPosts(
            @Parameter(hidden = true) @AuthUser Long memberId,
            @RequestParam(name="page", defaultValue = "1") Integer page,
            @RequestParam(name="size", defaultValue = "10") Integer size
    ) {
        return ApiResponse.onSuccess(memberQueryService.getLikedPosts(memberId, page, size));
    }

    /**
     * 마이페이지 - 내가 스크랩한 글 취소 API 구현
     */
    @Operation(
            summary = "마이페이지- 내가 스크랩한 글 취소 API",
            description = "사용자가 스크랩한 글 취소 API 입니다. postId를 PathVariable 로 보내주세요."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON400", description = "잘못된 요청입니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @Parameters({
            @Parameter(name = "postId", in = ParameterIn.PATH, description = "해당 게시글의 ID", example = "1", required = true)
    })
    @DeleteMapping("/mypage/scraps/{postId}")
    public ApiResponse<MemberResponseDTO.CancelScrapOrLikeResponseDTO> cancelScrap(
            @Parameter(hidden = true) @AuthUser Long memberId,
            @PathVariable(name = "postId") Long postId
    ) {
        return ApiResponse.onSuccess(memberCommandService.cancelScrap(memberId, postId));
    }
    /**
     * 마이페이지 - 내가 공감한 글 취소 API 구현
     */
    @Operation(
            summary = "마이페이지- 내가 공감한 글 취소 API",
            description = "사용자가 공감한 글 취소 API 입니다. postId를 PathVariable 로 보내주세요."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON400", description = "잘못된 요청입니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @Parameters({
            @Parameter(name = "postId", in = ParameterIn.PATH, description = "해당 게시글의 ID", example = "1", required = true)
    })
    @DeleteMapping("/mypage/likes/{postId}")
    public ApiResponse<MemberResponseDTO.CancelScrapOrLikeResponseDTO> cancelLike(
            @Parameter(hidden = true) @AuthUser Long memberId,
            @PathVariable(name = "postId") Long postId
    ) {
        return ApiResponse.onSuccess(memberCommandService.cancelLike(memberId, postId));
    }

}
