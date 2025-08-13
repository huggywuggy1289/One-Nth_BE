package com.onenth.OneNth.domain.product.repository.itemRepository.sharing;

import com.onenth.OneNth.domain.member.entity.Member;
import com.onenth.OneNth.domain.product.entity.PurchaseItem;
import com.onenth.OneNth.domain.product.entity.SharingItem;
import com.onenth.OneNth.domain.product.entity.enums.PurchaseMethod;
import com.onenth.OneNth.domain.region.entity.Region;
import com.onenth.OneNth.domain.product.entity.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SharingItemRepository extends JpaRepository<SharingItem, Long>, SharingItemRepositoryCustom {
    Optional<SharingItem> findById(Long id);
    List<SharingItem> findByMember(Member member);

    // 상품명(지역 필터링)
    @Query("""
    SELECT s
    FROM SharingItem s
    JOIN FETCH s.region
    WHERE LOWER(s.title) LIKE LOWER(CONCAT('%', :keyword, '%'))
      AND s.region.id IN :regionIds
      AND s.status IN :statuses
    """)
    List<SharingItem> searchByTitleAndRegion(
            @Param("keyword") String keyword,
            @Param("regionIds") List<Integer> regionIds,
            @Param("statuses") List<Status> statuses
    );

    @Query("""
    SELECT s
    FROM SharingItem s
    JOIN FETCH s.region
    WHERE s.id = :id
    """)
    Optional<SharingItem> findWithRegionById(@Param("id") Long id);

    List<SharingItem> findAllByRegionAndPurchaseMethod(Region region, PurchaseMethod method);

    @EntityGraph(attributePaths = {"itemImages"})
    List<SharingItem> findByMemberAndStatus(Member member, Status status);

    @EntityGraph(attributePaths = {"itemImages"})
    Optional<SharingItem> findWithItemImagesById(Long id);

    @Query("""
        select s from SharingItem s
        left join fetch s.itemImages
        where s.member.id = :memberId
        order by s.createdAt desc
    """)
    Page<SharingItem> findByMemberId(@Param("memberId") Long memberId, Pageable pageable);

    @Query("select count(s) from SharingItem s where s.member.id = :memberId")
    long countByMemberId(@Param("memberId") Long memberId);
}