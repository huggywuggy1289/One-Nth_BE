package com.onenth.OneNth.domain.deal.controller;

import com.onenth.OneNth.domain.deal.dto.DealRequestDTO;
import com.onenth.OneNth.domain.deal.dto.DealResponseDTO;
import com.onenth.OneNth.domain.deal.service.DealCommandService;
import com.onenth.OneNth.domain.deal.service.DealQueryService;
import com.onenth.OneNth.global.apiPayload.ApiResponse;
import com.onenth.OneNth.global.auth.annotation.AuthUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "거래 관련 API", description = " API")
@RestController
@RequestMapping("/api/deals")
@RequiredArgsConstructor
public class DealController {

    private final DealCommandService dealCommandService;
    private final DealQueryService dealQueryService;

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
            @RequestParam("roomName") String roomName,
            @RequestBody DealRequestDTO.DealConfirmationRequestDTO request
            ) {
        dealCommandService.createDealConfirmation(memberId, request, roomName);
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
            @RequestParam("roomName") String roomName,
            @RequestBody DealRequestDTO.DealCompletionRequestDTO request
    ) {
        dealCommandService.createDealCompletion(memberId, request, roomName);
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
            @RequestParam String roomName,
            @RequestBody DealRequestDTO.DealCancelRequestDTO request
            ) {
        dealCommandService.cancelDeal(request);
        return ApiResponse.onSuccess("거래취소가 완료되었습니다.");
    }

    @Operation(
            summary = "거래 확정 폼 조회 API",
            description = """
    상대 사용자와의 거래에서 발행된 거래확정 폼을 조회합니다.
    - 거래완료 폼 작성 시 필요한 거래확정 폼의 Id 및 상품 정보를 얻기 위한 의도로 만들어졌습니다.(드롭박스)
    - 현재 사용자가 거래완료 폼 작성이 가능한 거래확정 폼들의 리스트 및 상품정보를 조회합니다.
    - roomName에 현재 거래가 진행 중인 채팅방 roomName을 넣어야합니다.
    """
    )
    @GetMapping("/confirmation/{roomName}")
    public ApiResponse<List<DealResponseDTO.GetDealConfirmationDTO>> cancelDeal(
            @AuthUser Long memberId,
            @PathVariable("roomName") String roomName
    ) {
        List<DealResponseDTO.GetDealConfirmationDTO> result
                = dealQueryService.getDealConfirmations(memberId,roomName);
        return ApiResponse.onSuccess(result);
    }

    @Operation(
            summary = "거래가 가능한 상품(판매자) 조회 API",
            description = """
    거래가 가능한 상품 목록을 조회합니다.
    - 거래확정 폼 작성 시, 내 상품 중 상대방과 거래 시작이 가능한 상품 정보를 얻기 위한 의도로 만들어졌습니다.(드롭박스)
    - 현재 사용자가 거래확정 폼 작성이 가능한 상품들의 목록들을 조회합니다.
    """
    )
    @GetMapping("/available-products")
    public ApiResponse<List<DealResponseDTO.getAvailableProductDTO>> getAvailableProducts(
            @AuthUser Long memberId
    ) {
            List<DealResponseDTO.getAvailableProductDTO> result = dealQueryService.getAvailableProducts(memberId);
            return ApiResponse.onSuccess(result);
    }

    @Operation(
            summary = "내 거래 내역 조회 API",
            description = """
    N원 아꼈어요 페이지의 내 거래 내역 정보를 조회합니다.
    - N분의 1 거래 횟수, 리뷰 총점 및 평균
    - 같이사요, 함께 나눠요 거래 수 및 금액을 조회합니다
    """
    )
    @GetMapping("/my-history")
    public ApiResponse<DealResponseDTO.GetMyDealHistoryDTO> getMyDealHistory(
            @AuthUser Long memberId
    ) {
        DealResponseDTO.GetMyDealHistoryDTO result
                = dealQueryService.getMyDealHistory(memberId);
        return ApiResponse.onSuccess(result);
    }
}