package com.onenth.OneNth.domain.member.converter;

import com.onenth.OneNth.domain.auth.dto.KakaoRequestDTO;
import com.onenth.OneNth.domain.member.dto.MemberRequestDTO;
import com.onenth.OneNth.domain.member.dto.MemberResponseDTO;
import com.onenth.OneNth.domain.member.entity.Member;
import com.onenth.OneNth.domain.member.entity.MemberRegion;
import com.onenth.OneNth.domain.member.entity.enums.LoginType;
import com.onenth.OneNth.domain.member.entity.enums.MemberStatus;
import com.onenth.OneNth.domain.post.entity.Like;
import com.onenth.OneNth.domain.post.entity.Post;
import com.onenth.OneNth.domain.post.entity.Scrap;
import com.onenth.OneNth.domain.product.entity.PurchaseItem;
import com.onenth.OneNth.domain.product.entity.SharingItem;
import com.onenth.OneNth.domain.product.entity.scrap.PurchaseItemScrap;
import com.onenth.OneNth.domain.product.entity.scrap.SharingItemScrap;
import com.onenth.OneNth.domain.region.entity.Region;
import org.springframework.data.domain.Page;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class MemberConverter {

    //회원가입 요청 dto 를 Member 엔티티로 변환
    public static Member toMember(MemberRequestDTO.SignupDTO request, Region region) {
        Member member = Member.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(request.getPassword())
                .nickname(request.getNickname())
                .loginType(LoginType.NORMAL)
                .status(MemberStatus.ACTIVE)
                .memberRegions(new ArrayList<>())
                .marketingAgree(request.getMarketingAgree())
                .build();

        //회원가입시에는 지역 한개만 매핑
        MemberRegion memberRegion = MemberRegion.builder()
                .member(member)
                .region(region)
                .isMain(true)
                .build();

        member.getMemberRegions().add(memberRegion);

        return member;
    }

    //카카오 회원가입 요청 dto 를 Member 엔티티로 변환
    public static Member toMember(KakaoRequestDTO.KakaoSignupRequestDTO request, Region region) {
        Member member = Member.builder()
                .email(request.getEmail())
                .name(request.getName())
                .socialId(request.getSocialId())
                .nickname(request.getNickname())
                .loginType(LoginType.KAKAO)
                .status(MemberStatus.ACTIVE)
                .memberRegions(new ArrayList<>())
                .marketingAgree(request.getMarketingAgree())
                .build();

        //회원가입시에는 지역 한개만 매핑
        MemberRegion memberRegion = MemberRegion.of(member, region);
        member.getMemberRegions().add(memberRegion);

        return member;
    }

    // Member 엔티티를 회원가입 응답 dto 로 변환
    public static MemberResponseDTO.SignupResultDTO toSignupResultDTO(Member member) {
        return MemberResponseDTO.SignupResultDTO.builder()
                .memberId(member.getId())
                .createdAt(LocalDateTime.now())
                .build();
    }

    //로그인 결과 dto로 변환
    public static MemberResponseDTO.LoginResultDTO toLoginResultDTO(Long memberId, String accessToken, String refreshToken) {
       return MemberResponseDTO.LoginResultDTO.builder()
               .memberId(memberId)
               .accessToken(accessToken)
               .refreshToken(refreshToken)
               .build();
    }


    // 스크랩한 Page 객체를 게시글 미리보기 객체를 리스트로 제공
    public static MemberResponseDTO.PostListDTO toScrappedPostPreviewListDTO(Page<Scrap> postList, int page) {
        List<MemberResponseDTO.PostPreviewDTO> postPreviewList = postList.stream()
                .map(MemberConverter::toScrappedPostPreviewDTO).toList();

        return MemberResponseDTO.PostListDTO.builder()
                .totalPage(postList.getTotalPages())
                .totalElements(postList.getTotalElements())
                .currentPage(page)
                .listSize(postPreviewList.size())
                .isFirst(postList.isFirst())
                .isLast(postList.isLast())
                .postList(postPreviewList)
                .build();
    }

    // 스크랩한 게시글 조회에서 필요한 미리보기dto로 전환
    public static MemberResponseDTO.PostPreviewDTO toScrappedPostPreviewDTO(Scrap scrappedPost) {

        return MemberResponseDTO.PostPreviewDTO.builder()
                .postId(scrappedPost.getPost().getId())
                .postType(scrappedPost.getPost().getPostType().toString())
                .postTitle(scrappedPost.getPost().getTitle())
                .placeName(scrappedPost.getPost().getPlaceName())
                .latitude(scrappedPost.getPost().getLatitude())
                .longitude(scrappedPost.getPost().getLongitude())
                .likeCount(scrappedPost.getPost().getLike().size())
                .viewCount(0)
                .createdTime(formatRelativeTime(scrappedPost.getPost().getCreatedAt()))
                .build();
    }

    // 좋아요한 Page 객체를 게시글 미리보기 객체를 리스트로 제공
    public static MemberResponseDTO.PostListDTO toLikedPostPreviewListDTO(Page<Like> postList, int page) {
        List<MemberResponseDTO.PostPreviewDTO> postPreviewList = postList.stream()
                .map(MemberConverter::toLikedPostPreviewDTO).toList();

        return MemberResponseDTO.PostListDTO.builder()
                .totalPage(postList.getTotalPages())
                .totalElements(postList.getTotalElements())
                .currentPage(page)
                .listSize(postPreviewList.size())
                .isFirst(postList.isFirst())
                .isLast(postList.isLast())
                .postList(postPreviewList)
                .build();
    }

    // 좋아요한 게시글 조회에서 필요한 미리보기dto로 전환
    public static MemberResponseDTO.PostPreviewDTO toLikedPostPreviewDTO(Like likedPost) {

        return MemberResponseDTO.PostPreviewDTO.builder()
                .postId(likedPost.getPost().getId())
                .postType(likedPost.getPost().getPostType().toString())
                .postTitle(likedPost.getPost().getTitle())
                .placeName(likedPost.getPost().getPlaceName())
                .latitude(likedPost.getPost().getLatitude())
                .longitude(likedPost.getPost().getLongitude())
                .commentCount(likedPost.getPost().getPostComment().size())
                .likeCount(likedPost.getPost().getLike().size())
                .viewCount(0)
                .createdTime(formatRelativeTime(likedPost.getPost().getCreatedAt()))
                .build();
    }

    // 게시글 등록 날짜 변환
    private static String formatRelativeTime(LocalDateTime createdAt) {
        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.between(createdAt, now);

        long minutes = duration.toMinutes();
        long hours = duration.toHours();
        long days = duration.toDays();

        if (minutes < 60) {
            return minutes + "분 전";
        } else if (hours < 24) {
            return hours + "시간 전";
        } else if (days == 1) {
            return "어제";
        } else {
            return createdAt.format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));
        }
    }

    // 내가 쓴 게시글을 한번에 보기
    public static MemberResponseDTO.PostListDTO toMyPostPreviewListDTO(Page<Post> postList, int page ) {
        List<MemberResponseDTO.PostPreviewDTO> postPreviewList = postList.stream()
                .map(MemberConverter::toPostPreviewDTO).toList();

        return MemberResponseDTO.PostListDTO.builder()
                .totalPage(postList.getTotalPages())
                .totalElements(postList.getTotalElements())
                .currentPage(page)
                .listSize(postPreviewList.size())
                .isFirst(postList.isFirst())
                .isLast(postList.isLast())
                .postList(postPreviewList)
                .build();
    }

    // 내가 쓴 게시글 조회에서 필요한 미리보기dto로 전환
    public static MemberResponseDTO.PostPreviewDTO toPostPreviewDTO(Post myPost) {

        return MemberResponseDTO.PostPreviewDTO.builder()
                .postId(myPost.getId())
                .postType(myPost.getPostType().toString())
                .postTitle(myPost.getTitle())
                .placeName(myPost.getPlaceName())
                .latitude(myPost.getLatitude())
                .longitude(myPost.getLongitude())
                .commentCount(myPost.getPostComment().size())
                .likeCount(myPost.getLike().size())
                .viewCount(0)
                .createdTime(formatRelativeTime(myPost.getCreatedAt()))
                .build();
    }

    public static MemberResponseDTO.ItemPreviewDTO fromPurchase(PurchaseItem pi) {
        return MemberResponseDTO.ItemPreviewDTO.builder()
                .itemId(pi.getId())
                .itemType("같이 사요")
                .productName(pi.getProductName()) // = name
                .price(pi.getPrice())
                .quantity(1)                // Purchase에는 별도 수량 필드 없음
                .originalPrice(pi.getPrice())     // 정의에 따라
                .createdTime(formatRelativeTime(pi.getCreatedAt()))
                .createdAt(pi.getCreatedAt())
                .build();
    }

    public static MemberResponseDTO.ItemPreviewDTO fromSharing(SharingItem si) {
        return MemberResponseDTO.ItemPreviewDTO.builder()
                .itemId(si.getId())
                .itemType("함께 나눠요") // or "함께나눠요"
                .productName(si.getProductName()) // = title
                .price(si.getPrice())
                .quantity(si.getQuantity())       // Sharing에는 수량 존재
                .originalPrice(si.getPrice())   // 정의에 따라 세팅
                .createdTime(formatRelativeTime(si.getCreatedAt()))
                .createdAt(si.getCreatedAt())
                .build();
    }


    public static MemberResponseDTO.ItemPreviewDTO fromPurchaseScrap(PurchaseItemScrap scrap) {
        PurchaseItem pi = scrap.getPurchaseItem();
        return MemberResponseDTO.ItemPreviewDTO.builder()
                .itemId(pi.getId())
                .itemType("같이 사요")
                .productName(pi.getProductName())
                .price(pi.getPrice())
                .quantity(1)
                .originalPrice(pi.getPrice()) // 정의에 따라 세팅
                .createdTime(formatRelativeTime(pi.getCreatedAt()))
                .createdAt(pi.getCreatedAt())
                .scrappedAt(scrap.getCreatedAt())
                .build();
    }

    public static MemberResponseDTO.ItemPreviewDTO fromSharingScrap(SharingItemScrap scrap) {
        SharingItem si = scrap.getSharingItem();
        return MemberResponseDTO.ItemPreviewDTO.builder()
                .itemId(si.getId())
                .itemType("함께 나눠요")
                .productName(si.getProductName())
                .price(si.getPrice())
                .quantity(si.getQuantity())
                .originalPrice(si.getPrice()) // 정의에 따라 세팅
                .createdTime(formatRelativeTime(si.getCreatedAt()))
                .createdAt(si.getCreatedAt())
                .scrappedAt(scrap.getCreatedAt())
                .build();
    }
}
