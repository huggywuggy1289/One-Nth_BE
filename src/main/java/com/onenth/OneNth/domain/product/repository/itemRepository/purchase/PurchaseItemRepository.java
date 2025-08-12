package com.onenth.OneNth.domain.product.repository.itemRepository.purchase;

import com.onenth.OneNth.domain.member.entity.Member;
import com.onenth.OneNth.domain.product.entity.PurchaseItem;
import com.onenth.OneNth.domain.product.entity.enums.PurchaseMethod;
import com.onenth.OneNth.domain.product.entity.enums.Status;
import com.onenth.OneNth.domain.region.entity.Region;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PurchaseItemRepository  extends JpaRepository<PurchaseItem, Long>, PurchaseItemRepositoryCustom {
    List<PurchaseItem> findByMember(Member member);

    // 상품단건조회
    @Query("""
    SELECT p
    FROM PurchaseItem p
    JOIN FETCH p.region
    WHERE p.id = :id
    """)
    Optional<PurchaseItem> findWithRegionById(@Param("id") Long id);

    List<PurchaseItem> findAllByRegionAndPurchaseMethod(Region region, PurchaseMethod method);

    @EntityGraph(attributePaths = {"itemImages"})
    List<PurchaseItem> findByMemberAndStatus(Member member, Status status);

    @EntityGraph(attributePaths = {"itemImages"})
    Optional<PurchaseItem> findWithItemImagesById(Long id);

    @Query("""
        select p from PurchaseItem p
        where p.member.id = :memberId
        order by p.createdAt desc
    """)
    Page<PurchaseItem> findByMemberId(@Param("memberId") Long memberId, Pageable pageable);

    @Query("select count(p) from PurchaseItem p where p.member.id = :memberId")
    long countByMemberId(@Param("memberId") Long memberId);
}
