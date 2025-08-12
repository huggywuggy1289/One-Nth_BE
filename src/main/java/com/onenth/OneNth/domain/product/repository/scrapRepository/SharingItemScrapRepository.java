package com.onenth.OneNth.domain.product.repository.scrapRepository;

import com.onenth.OneNth.domain.member.entity.Member;
import com.onenth.OneNth.domain.product.entity.SharingItem;
import com.onenth.OneNth.domain.product.entity.scrap.SharingItemScrap;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface SharingItemScrapRepository extends JpaRepository<SharingItemScrap, Long> {

    boolean existsByMemberAndSharingItem(Member member, SharingItem item);

    @Query("SELECT s FROM SharingItemScrap s WHERE s.member.id = :userId")
    List<SharingItemScrap> findByUserId(Long userId);

    Optional<SharingItemScrap> findByMemberIdAndSharingItemId(Long userId, Long itemId);

    @Query("""
       select s from SharingItemScrap s
       join fetch s.sharingItem si
       where s.member.id = :memberId
       order by s.createdAt desc
    """)
    Page<SharingItemScrap> findByMemberId(@Param("memberId") Long memberId, Pageable pageable);

    @Query("select count(s) from SharingItemScrap s where s.member.id = :memberId")
    long countByMemberId(@Param("memberId") Long memberId);
}
