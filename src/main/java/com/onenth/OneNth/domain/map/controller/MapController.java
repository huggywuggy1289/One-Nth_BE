package com.onenth.OneNth.domain.map.controller;

import com.onenth.OneNth.domain.map.dto.MapResponseDTO;
import com.onenth.OneNth.domain.map.service.DiscountMapService;
import com.onenth.OneNth.domain.map.service.PurchaseItemMapService;
import com.onenth.OneNth.domain.map.service.RestaurantMapService;
import com.onenth.OneNth.domain.map.service.SharingItemMapService;
import com.onenth.OneNth.global.apiPayload.ApiResponse;
import com.onenth.OneNth.global.apiPayload.code.ErrorReasonDTO;
import com.onenth.OneNth.global.apiPayload.code.status.ErrorStatus;
import com.onenth.OneNth.global.apiPayload.exception.GeneralException;
import com.onenth.OneNth.global.auth.annotation.AuthUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "지도 마커 표시 관련 API", description = "같이사요, 함께나눠요, 할인정보게시판, 맛집게시판 지도 마커 표시 및 특정 마커 상세 표시 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/map")
public class MapController {

    private final PurchaseItemMapService purchaseItemMapService;
    private final SharingItemMapService sharingItemMapService;
    private final DiscountMapService discountMapService;
    private final RestaurantMapService restaurantMapService;

    @Operation(
            summary = "지도에 마커 표시 API",
            description = "지도에 해당 지역에 해당하는 글 마커를 띄워주는 API입니다. " +
                    "같이사요/함께나눠요/할인정보게시판/맛집게시판에 따라 MarkerType(purchase-item, sharing-item, discount, restaurant)를 받으며, 각 값에 따라 해당하는 거래 글 / 게시글의 마커를 띄워줍니다." +
                    "regionId 파라미터를 선택적으로 받으며, regionId가 없는 경우엔 사용자가 메인으로 설정한 지역의 글을 띄워줍니다." +
                    "응답으로 markerType(purchase-item, sharing-item, discount, restaurant), 글 id, 글 제목, 거래장소 혹은 정보의 주소(위도, 경도)를 반환합니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 지역 등록 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "MEMBER001", description = "존재하지 않는 사용자입니다.", content = @Content(schema = @Schema(implementation = ErrorReasonDTO.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "REGION001", description = "존재하지 않는 지역입니다.", content = @Content(schema = @Schema(implementation = ErrorReasonDTO.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "MEMBER_REGION005", description = "사용자의 메인 지역 설정이 필요합니다. 우리동네 설정 화면에서 메인 지역을 선택해주세요.", content = @Content(schema = @Schema(implementation = ErrorReasonDTO.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "MAP001", description = "지원하지 않는 MarkerType입니다.", content = @Content(schema = @Schema(implementation = ErrorReasonDTO.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON500", description = "서버 에러, 관리자에게 문의 바랍니다", content = @Content(schema = @Schema(implementation = ErrorReasonDTO.class))),
    })
    @GetMapping("/markers")
    public ApiResponse<MapResponseDTO.GetMarkersResponseDTO> getMarkers(
            @Parameter(hidden=true) @AuthUser Long userId,
            @RequestParam String markerType,
            @RequestParam(required = false) Long regionId
    ) {
        MapResponseDTO.GetMarkersResponseDTO response = switch (markerType) {
            case "purchase-item" -> purchaseItemMapService.getMarkers(userId, regionId);
            case "sharing-item" -> sharingItemMapService.getMarkers(userId, regionId);
            case "discount" -> discountMapService.getMarkers(userId, regionId);
            case "restaurant" -> restaurantMapService.getMarkers(userId, regionId);
            default -> throw new GeneralException(ErrorStatus.INVALID_MARKER_TYPE);
        };
        return ApiResponse.onSuccess(response);
    }

    @Operation(
            summary = "마커 클릭 시 화면에 미리보기를 표시하는 API",
            description = ""
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 지역 등록 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "MEMBER001", description = "존재하지 않는 사용자입니다.", content = @Content(schema = @Schema(implementation = ErrorReasonDTO.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "PURCHASE_ITEM_4001", description = "존재하지 않는 품목입니다.", content = @Content(schema = @Schema(implementation = ErrorReasonDTO.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "SHARING_ITEM_4001", description = "존재하지 않는 품목입니다.", content = @Content(schema = @Schema(implementation = ErrorReasonDTO.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON500", description = "서버 에러, 관리자에게 문의 바랍니다", content = @Content(schema = @Schema(implementation = ErrorReasonDTO.class))),
    })
    @GetMapping("/markers/items/details")
    public ApiResponse<MapResponseDTO.GetItemMarkerDetailsResponseDTO> getItemMarkerDetails(
            @Parameter(hidden=true) @AuthUser Long userId,
            @RequestParam String markerType,
            @RequestParam List<Long> itemIds
    ) {
        MapResponseDTO.GetItemMarkerDetailsResponseDTO response = switch (markerType) {
            case "purchase-item" -> purchaseItemMapService.getMarkerDetails(userId, itemIds);
            case "sharing-item" -> sharingItemMapService.getMarkerDetails(userId, itemIds);
            default -> throw new GeneralException(ErrorStatus.INVALID_MARKER_TYPE);
        };
        return ApiResponse.onSuccess(response);
    }

    //@GetMapping("/markers/posts/details")
}
