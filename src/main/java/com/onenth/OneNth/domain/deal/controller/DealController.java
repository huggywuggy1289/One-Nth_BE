package com.onenth.OneNth.domain.deal.controller;

import com.onenth.OneNth.domain.deal.dto.DealRequestDTO;
import com.onenth.OneNth.domain.deal.service.DealCommandService;
import com.onenth.OneNth.global.apiPayload.ApiResponse;
import com.onenth.OneNth.global.auth.annotation.AuthUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "거래 관련 API", description = " API")
@RestController
@RequestMapping("/api/deals")
@RequiredArgsConstructor
public class DealController {

    private final DealCommandService dealCommandService;

    @Operation(
            summary = "거래 확정 폼 발행 API",
            description = """
    거래 확정 폼을 발행하는 API입니다. 채팅 거래 프로세스에서 이용됩니다.
    - 특정 상품에 대해 거래를 확정하려 할 때 거래확정 폼 데이터를 저장합니다.
    - 해당 상품의 상태롤 거래확정(IN_PROGRESS)으로 전환합니다.
    """
    )
    @PostMapping("/confirmation")
    public ApiResponse<String> createDealConfirmationForm(
            @AuthUser Long memberId,
            @RequestBody DealRequestDTO.DealConfirmationRequestDTO request
            ) {
        dealCommandService.createDealConfirmation(memberId, request);
        return ApiResponse.onSuccess("거래확정폼 발행이 완료되었습니다.");
    }

    @Operation(
            summary = "거래 완료 폼 발행 API",
            description = """
    거래 완료 폼을 발행하는 API입니다. 채팅 거래 프로세스에서 이용됩니다.
    - 특정 상품에 대해 거래를 완료하려 할 때 거래완료 폼 데이터를 저장합니다.
    - 해당 상품의 상태롤 거래완료(COMPLETED)로 전환합니다.
    """
    )
    @PostMapping("/completion")
    public ApiResponse<String> createDealCompletionForm(
            @AuthUser Long memberId,
            @RequestBody DealRequestDTO.DealCompletionRequestDTO request
    ) {
        dealCommandService.createDealCompletion(memberId, request);
        return ApiResponse.onSuccess("거래완료폼 발행이 완료되었습니다.");
    }

    @Operation(
            summary = "거래 취소 API",
            description = """
    발행된 거래확정 폼이 있을때, 해당 거래를 취소하고 폼을 삭제하는 API 입니다. 채팅 거래 프로세스에서 이용됩니다.
    - 해당 상품에 연결된 거래확정 폼을 삭제합니다.
    - 상품의 상태를 거래 가능 상태로 되돌립니다 (DEFAULT).
    - 거래가 이미 완료된 경우에는 취소할 수 없습니다.
    """
    )
    @DeleteMapping("/cancellation")
    public ApiResponse<String> cancelDeal(
            @RequestBody DealRequestDTO.DealCancelRequestDTO request
            ) {
        dealCommandService.cancelDeal(request);
        return ApiResponse.onSuccess("거래취소가 완료되었습니다.");
    }
}