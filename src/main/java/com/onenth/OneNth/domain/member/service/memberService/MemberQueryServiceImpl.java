package com.onenth.OneNth.domain.member.service.memberService;

import com.onenth.OneNth.domain.member.converter.MemberConverter;
import com.onenth.OneNth.domain.member.dto.MemberResponseDTO;
import com.onenth.OneNth.domain.member.entity.Member;
import com.onenth.OneNth.domain.member.repository.memberRepository.MemberRepository;
import com.onenth.OneNth.domain.post.entity.Like;
import com.onenth.OneNth.domain.post.entity.Post;
import com.onenth.OneNth.domain.post.entity.Scrap;
import com.onenth.OneNth.domain.post.repository.likeRepository.LikeRepository;
import com.onenth.OneNth.domain.post.repository.PostRepository;
import com.onenth.OneNth.domain.post.repository.scrapRepository.ScrapRepository;
import com.onenth.OneNth.domain.product.entity.PurchaseItem;
import com.onenth.OneNth.domain.product.entity.SharingItem;
import com.onenth.OneNth.domain.product.entity.scrap.PurchaseItemScrap;
import com.onenth.OneNth.domain.product.entity.scrap.SharingItemScrap;
import com.onenth.OneNth.domain.product.repository.itemRepository.purchase.PurchaseItemRepository;
import com.onenth.OneNth.domain.product.repository.itemRepository.sharing.SharingItemRepository;
import com.onenth.OneNth.domain.product.repository.scrapRepository.PurchaseItemScrapRepository;
import com.onenth.OneNth.domain.product.repository.scrapRepository.SharingItemScrapRepository;
import com.onenth.OneNth.global.apiPayload.code.status.ErrorStatus;
import com.onenth.OneNth.global.apiPayload.exception.handler.MemberHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberQueryServiceImpl implements MemberQueryService {

    private final MemberRepository memberRepository;
    private final ScrapRepository scrapRepository;
    private final LikeRepository likeRepository;
    private final PostRepository postRepository;
    private final PurchaseItemRepository purchaseItemRepository;
    private final SharingItemRepository sharingItemRepository;
    private final SharingItemScrapRepository sharingItemScrapRepository;
    private final PurchaseItemScrapRepository purchaseItemScrapRepository;


    @Override
    public MemberResponseDTO.PostListDTO getScrappedPosts(Long memberId, Integer page, Integer size) {

        if (memberId == null) {
            return MemberResponseDTO.PostListDTO.builder().build();
        }

        Page<Scrap> scrappedPostPage = scrapRepository.
                findByMemberId(memberId, PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt")));

        MemberResponseDTO.PostListDTO postPreviews = MemberConverter.toScrappedPostPreviewListDTO(scrappedPostPage, page);

        return postPreviews;
    }

    @Override
    public MemberResponseDTO.PostListDTO getLikedPosts(Long memberId, Integer page, Integer size) {
        if (memberId == null) {
            return MemberResponseDTO.PostListDTO.builder().build();
        }

        Page<Like> likedPostPage = likeRepository.
                findByMemberId(memberId, PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt")));

        MemberResponseDTO.PostListDTO postPreviews = MemberConverter.toLikedPostPreviewListDTO(likedPostPage, page);

        return postPreviews;
    }

    @Override
    public MemberResponseDTO.PostListDTO getMyPosts(Long memberId, Integer page, Integer size) {
        if (memberId == null) {
            return MemberResponseDTO.PostListDTO.builder().build();
        }

        Page<Post> myPostPage = postRepository.
                findByMemberId(memberId, PageRequest.of(page -1, size, Sort.by(Sort.Direction.DESC, "createdAt")));

        MemberResponseDTO.PostListDTO postPreviews = MemberConverter.toMyPostPreviewListDTO(myPostPage, page);

        return postPreviews;

    }

    @Override
    public MemberResponseDTO.MemberProfilePreviewDTO getMemberProfilePreview(Long memberId) {
        Member member =  memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));
        return new MemberResponseDTO.MemberProfilePreviewDTO(member.getId(), member.getNickname(), member.getProfileImageUrl());
    }

    public MemberResponseDTO.ItemPreviewListDTO getMyAllItems(Long memberId, Integer page, Integer size) {
        if (memberId == null) {
            return MemberResponseDTO.ItemPreviewListDTO.builder()
                    .items(Collections.emptyList())
                    .page(1).size(size == null ? 10 : size)
                    .totalCount(0L).hasNext(false)
                    .build();
        }

        int pg = (page == null || page < 1) ? 1 : page;
        int sz = (size == null || size < 1) ? 10 : size;
        PageRequest pageable = PageRequest.of(pg - 1, sz, Sort.by(Sort.Direction.DESC, "createdAt"));

        // 핵심: 두 타입을 각각 가져와서 DTO 변환 → 합치고 createdAt desc로 정렬 → page 사이즈만큼 잘라서 반환
        Page<PurchaseItem> piPage = purchaseItemRepository.findByMemberId(memberId, pageable);
        Page<SharingItem>  siPage = sharingItemRepository.findByMemberId(memberId, pageable);

        List<MemberResponseDTO.ItemPreviewDTO> merged = new ArrayList<>(sz * 2);

        for (PurchaseItem p : piPage.getContent()) {
            merged.add(MemberConverter.fromPurchase(p));
        }
        for (SharingItem s : siPage.getContent()) {
            merged.add(MemberConverter.fromSharing(s));
        }

        // 병합 후 정렬 (createdAt desc)
        merged.sort(Comparator.comparing(MemberResponseDTO.ItemPreviewDTO::getCreatedAt).reversed());

        // 현재 페이지 크기만큼 자르기 (두 페이지를 각각 size만큼만 가져왔기 때문에 충분치 않을 수 있음)
        // 안정적으로 하려면 각 repo에서 더 넉넉히 끌어오거나 keyset 방식을 써도 됩니다.
        List<MemberResponseDTO.ItemPreviewDTO> pageSlice = merged.stream()
                .limit(sz)
                .collect(Collectors.toList());

        // 총합(대략)과 다음 페이지 여부 계산
        long total = purchaseItemRepository.countByMemberId(memberId)
                + sharingItemRepository.countByMemberId(memberId);

        boolean hasNext = (long) (pg * sz) < total;

        return MemberResponseDTO.ItemPreviewListDTO.builder()
                .items(pageSlice)
                .page(pg)
                .size(sz)
                .totalCount(total)
                .hasNext(hasNext)
                .build();
    }

    public MemberResponseDTO.ItemPreviewListDTO getMyScrappedItems(Long memberId, Integer page, Integer size) {
        if (memberId == null) {
            return MemberResponseDTO.ItemPreviewListDTO.builder()
                    .items(Collections.emptyList())
                    .page(1)
                    .size(size == null ? 10 : size)
                    .totalCount(0L)
                    .hasNext(false)
                    .build();
        }

        int pg = (page == null || page < 1) ? 1 : page;
        int sz = (size == null || size < 1) ? 10 : size;

        // 스크랩 기준 최신순
        PageRequest pageable = PageRequest.of(pg - 1, sz, Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<PurchaseItemScrap> pis = purchaseItemScrapRepository.findByMemberId(memberId, pageable);
        Page<SharingItemScrap>  sis = sharingItemScrapRepository.findByMemberId(memberId, pageable);

        List<MemberResponseDTO.ItemPreviewDTO> merged = new ArrayList<>(sz * 2);

        for (PurchaseItemScrap s : pis.getContent()) merged.add(MemberConverter.fromPurchaseScrap(s));
        for (SharingItemScrap  s : sis.getContent()) merged.add(MemberConverter.fromSharingScrap(s));

        // 스크랩 시각 기준 최신순으로 병합 정렬
        merged.sort(Comparator.comparing(MemberResponseDTO.ItemPreviewDTO::getScrappedAt, Comparator.nullsLast(Comparator.naturalOrder())).reversed());

        // 현재 페이지 사이즈만큼 자르기
        List<MemberResponseDTO.ItemPreviewDTO> pageSlice = merged.stream().limit(sz).toList();

        long total = purchaseItemScrapRepository.countByMemberId(memberId)
                + sharingItemScrapRepository.countByMemberId(memberId);

        boolean hasNext = (long) (pg * sz) < total;

        return MemberResponseDTO.ItemPreviewListDTO.builder()
                .items(pageSlice)
                .page(pg)
                .size(sz)
                .totalCount(total)
                .hasNext(hasNext)
                .build();
    }
}
