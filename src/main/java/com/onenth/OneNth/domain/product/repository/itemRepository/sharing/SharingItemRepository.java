package com.onenth.OneNth.domain.product.repository.itemRepository.sharing;

import com.onenth.OneNth.domain.member.entity.Member;
import com.onenth.OneNth.domain.product.entity.SharingItem;
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
    WHERE LOWER(s.title) LIKE LOWER(CONCAT('%', :keyword, '%'))
      AND s.region.id IN :regionIds
    """)
    List<SharingItem> searchByTitleAndRegion(@Param("keyword") String keyword, @Param("regionIds") List<Integer> regionIds);

    // 상품명(전체 조회)
    List<SharingItem> findByTitleContainingIgnoreCase(String keyword);
}