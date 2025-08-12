package com.onenth.OneNth.domain.map.controller;

import com.onenth.OneNth.domain.map.dto.MapResponseDTO;
import com.onenth.OneNth.domain.map.enums.MarkerType;
import com.onenth.OneNth.domain.map.service.*;
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
    private final PostMapService postMapService;
    private final MapRegionService mapRegionService;

    @Operation(
            summary = "같이사요/함께나눠요 지도에 마커 표시 API",
            description = "지도에 해당 지역에 거래 글 마커를 띄워주는 API입니다. \n" +
                    "거래글의 위도/경도를 기준으로 소수점 4자리까지 반올림하여, 같은 위치에 등록된 글들을 하나의 마커로 그룹핑하여 응답합니다.\n" +
                    "마커 클릭 시 표시할 수 있도록, 해당 마커 위치에 포함된 글들의 요약 정보 목록을 함께 반환합니다.\n" +
                    "같이사요/함께나눠요에 따라 MarkerType(purchase-item, sharing-item)를 받으며, 각 값에 따라 해당하는 거래 글의 마커를 띄워줍니다.\n" +
                    "regionId 파라미터를 선택적으로 받으며, regionId가 없는 경우엔 사용자가 메인으로 설정한 지역의 글을 띄워줍니다.\n" +
                    "응답으로 마커의 위도/경도, 포함된 거래글들의 markerType, 글 id, 상품 이름, 거래장소를 반환합니다.\n" +
                    "*MarkerType: 같이사요(purchase-item), 함께나눠요(sharing-item), 할인게시판(discount), 맛집게시판(restaurant)"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 조회 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "MEMBER001", description = "존재하지 않는 사용자입니다.", content = @Content(schema = @Schema(implementation = ErrorReasonDTO.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "REGION001", description = "존재하지 않는 지역입니다.", content = @Content(schema = @Schema(implementation = ErrorReasonDTO.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "MEMBER_REGION005", description = "사용자의 메인 지역 설정이 필요합니다. 우리동네 설정 화면에서 메인 지역을 선택해주세요.", content = @Content(schema = @Schema(implementation = ErrorReasonDTO.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "MAP001", description = "지원하지 않는 MarkerType입니다.", content = @Content(schema = @Schema(implementation = ErrorReasonDTO.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON500", description = "서버 에러, 관리자에게 문의 바랍니다", content = @Content(schema = @Schema(implementation = ErrorReasonDTO.class))),
    })
    @GetMapping("/markers/items")
    public ApiResponse<MapResponseDTO.GetMarkersResponseDTO> getItemMarkers(
            @Parameter(hidden=true) @AuthUser Long userId,
            @RequestParam MarkerType markerType,
            @RequestParam(required = false) Long regionId
    ) {
        MapResponseDTO.GetMarkersResponseDTO response = switch (markerType) {
            case PURCHASEITEM -> purchaseItemMapService.getMarkers(userId, regionId);
            case SHARINGITEM -> sharingItemMapService.getMarkers(userId, regionId);
            default -> throw new GeneralException(ErrorStatus.INVALID_MARKER_TYPE);
        };
        return ApiResponse.onSuccess(response);
    }

    @Operation(
            summary = "할인정보게시판/맛집게시판의 지도에 마커 표시 API",
            description = "지도에 해당 지역에 게시글 마커를 띄워주는 API입니다. \n" +
                    "게시글의 위도/경도를 기준으로 소수점 4자리까지 반올림하여, 같은 위치에 등록된 글들을 하나의 마커로 그룹핑하여 응답합니다.\n" +
                    "마커 클릭 시 표시할 수 있도록, 해당 마커 위치에 포함된 글들의 요약 정보 목록을 함께 반환합니다.\n" +
                    "할인정보/맛집게시판에 따라 MarkerType(discount, restaurant)를 받으며, 각 값에 따라 해당하는 게시글의 마커를 띄워줍니다.\n" +
                    "regionId 파라미터를 선택적으로 받으며, regionId가 없는 경우엔 사용자가 메인으로 설정한 지역의 글을 띄워줍니다.\n" +
                    "응답으로 마커의 위도/경도, 포함된 게시글들의 markerType, 글 id, 제목, 등록한 장소 정보를 반환합니다.\n" +
                    "*MarkerType: 같이사요(purchase-item), 함께나눠요(sharing-item), 할인게시판(discount), 맛집게시판(restaurant)"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 조회 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "MEMBER001", description = "존재하지 않는 사용자입니다.", content = @Content(schema = @Schema(implementation = ErrorReasonDTO.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "REGION001", description = "존재하지 않는 지역입니다.", content = @Content(schema = @Schema(implementation = ErrorReasonDTO.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "MEMBER_REGION005", description = "사용자의 메인 지역 설정이 필요합니다. 우리동네 설정 화면에서 메인 지역을 선택해주세요.", content = @Content(schema = @Schema(implementation = ErrorReasonDTO.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "MAP001", description = "지원하지 않는 MarkerType입니다.", content = @Content(schema = @Schema(implementation = ErrorReasonDTO.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON500", description = "서버 에러, 관리자에게 문의 바랍니다", content = @Content(schema = @Schema(implementation = ErrorReasonDTO.class))),
    })
    @GetMapping("/markers/posts")
    public ApiResponse<MapResponseDTO.GetMarkersResponseDTO> getPostMarkers(
            @Parameter(hidden=true) @AuthUser Long userId,
            @RequestParam MarkerType markerType,
            @RequestParam(required = false) Long regionId
    ) {
        if (markerType == MarkerType.DISCOUNT || markerType == MarkerType.RESTAURANT) {
            return ApiResponse.onSuccess(postMapService.getMarkers(userId, regionId, markerType));
        } else {
            throw new GeneralException(ErrorStatus.INVALID_MARKER_TYPE);
        }
    }

    @Operation(
            summary = "같이사요/함께나눠요에서 마커 클릭 시 화면에 미리보기를 표시하는 API\n",
            description = "지도에 표시된 마커들 중 하나를 클릭했을 때 해당 마커의 거래 글 미리보기 리스트를 보여주는 API입니다.\n" +
                    "응답으로 해당 거래글의 현재 상태(status: DEFAULT/IN_PROGRESS/COMPLETED), 거래 글의 카테고리, 구매 방식(ONLINE/OFFLINE), 스크랩 여부, 이미지 url들, 거래 상품 이름, 거래 상품 가격, 해당 거래글의 위도 및 경도를 반환합니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 조회 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "MEMBER001", description = "존재하지 않는 사용자입니다.", content = @Content(schema = @Schema(implementation = ErrorReasonDTO.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "PURCHASE_ITEM_4001", description = "존재하지 않는 품목입니다.", content = @Content(schema = @Schema(implementation = ErrorReasonDTO.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "SHARING_ITEM_4001", description = "존재하지 않는 품목입니다.", content = @Content(schema = @Schema(implementation = ErrorReasonDTO.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "MAP001", description = "지원하지 않는 MarkerType입니다.", content = @Content(schema = @Schema(implementation = ErrorReasonDTO.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON500", description = "서버 에러, 관리자에게 문의 바랍니다", content = @Content(schema = @Schema(implementation = ErrorReasonDTO.class))),
    })
    @GetMapping("/markers/items/details")
    public ApiResponse<MapResponseDTO.GetItemMarkerDetailsResponseDTO> getItemMarkerDetails(
            @Parameter(hidden=true) @AuthUser Long userId,
            @RequestParam MarkerType markerType,
            @RequestParam List<Long> itemIds
    ) {
        MapResponseDTO.GetItemMarkerDetailsResponseDTO response = switch (markerType) {
            case PURCHASEITEM -> purchaseItemMapService.getMarkerDetails(userId, itemIds);
            case SHARINGITEM -> sharingItemMapService.getMarkerDetails(userId, itemIds);
            default -> throw new GeneralException(ErrorStatus.INVALID_MARKER_TYPE);
        };
        return ApiResponse.onSuccess(response);
    }

    @Operation(
            summary = "할인정보 게시판/우리동네 맛집 게시판에서 마커 클릭 시 화면에 미리보기를 표시하는 API\n",
            description = "지도에 표시된 마커들 중 하나를 클릭했을 때 해당 마커의 게시글 미리보기 리스트를 보여주는 API입니다.\n" +
                    "응답으로 해당 게시글의 장소 이름, 스크랩 여부, 글 제목, 장소 주소, 게시글 생성 시간, 장소 정보(위/경도)를 반환합니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 조회 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "MEMBER001", description = "존재하지 않는 사용자입니다.", content = @Content(schema = @Schema(implementation = ErrorReasonDTO.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "POST_001", description = "게시글을 찾을 수 없습니다.", content = @Content(schema = @Schema(implementation = ErrorReasonDTO.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "MAP001", description = "지원하지 않는 MarkerType입니다.", content = @Content(schema = @Schema(implementation = ErrorReasonDTO.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON500", description = "서버 에러, 관리자에게 문의 바랍니다", content = @Content(schema = @Schema(implementation = ErrorReasonDTO.class))),
    })
    @GetMapping("/markers/posts/details")
    public ApiResponse<MapResponseDTO.GetPostMarkerDetailsResponseDTO> getPostMarkerDetails(
            @Parameter(hidden=true) @AuthUser Long userId,
            @RequestParam MarkerType markerType,
            @RequestParam List<Long> postIds

    ) {
        if (markerType == MarkerType.DISCOUNT || markerType == MarkerType.RESTAURANT) {
            return ApiResponse.onSuccess(postMapService.getMarkerDetails(userId, postIds));
        } else {
            throw new GeneralException(ErrorStatus.INVALID_MARKER_TYPE);
        }
    }

    @Operation(
            summary = "좌표 기반으로 regionId와 regionName을 보내주는 API\n",
            description = "위도/경도 좌표를 요청으로 받아 해당 위도/경도의 서버 내부에 저장된 regionId와 regionName을 응답으로 보내주는 API입니다.\n" +
                    "응답으로 regionId와 regionName을 반환합니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 지역 조회 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "REGION001", description = "존재하지 않는 지역입니다.", content = @Content(schema = @Schema(implementation = ErrorReasonDTO.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON500", description = "서버 에러, 관리자에게 문의 바랍니다", content = @Content(schema = @Schema(implementation = ErrorReasonDTO.class))),
    })
    @GetMapping("/regions")
    public ApiResponse<MapResponseDTO.GetRegionByCoordinatesResponseDTO> getRegionByCoordinates(
            @RequestParam double lat,
            @RequestParam double lng
    ) {
        return ApiResponse.onSuccess(mapRegionService.getRegionByCoordinates(lat, lng));
    }

    @Operation(
            summary = "regionName 기반으로 해당 지역의 대표 좌표를 보내주는 API\n",
            description = "지역 이름를 요청으로 받아 지역의 대표 좌표를 응답으로 보내주는 API입니다.\n" +
                    "응답으로 regionId와 regionName, 위도와 경도를 반환합니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 지역 조회 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "REGION001", description = "존재하지 않는 지역입니다.", content = @Content(schema = @Schema(implementation = ErrorReasonDTO.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON500", description = "서버 에러, 관리자에게 문의 바랍니다", content = @Content(schema = @Schema(implementation = ErrorReasonDTO.class))),
    })
    @GetMapping("/regions/center")
    public ApiResponse<MapResponseDTO.GetCoordinatesByNameResponseDTO> getRegionCoordinatesByName(
            @RequestParam String regionName
    ) {
        return ApiResponse.onSuccess(mapRegionService.getRegionCoordinatesByName(regionName));
    }
}
