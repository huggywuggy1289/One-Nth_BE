package com.onenth.OneNth.domain.product.repository.scrapRepository;

import com.onenth.OneNth.domain.member.entity.Member;
import com.onenth.OneNth.domain.product.entity.SharingItem;
import com.onenth.OneNth.domain.product.entity.scrap.SharingItemScrap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface SharingItemScrapRepository extends JpaRepository<SharingItemScrap, Long> {

    boolean existsByMemberAndSharingItem(Member member, SharingItem item);

    List<SharingItemScrap> findByMemberId(Long userId);

    Optional<SharingItemScrap> findByMemberIdAndSharingItemId(Long userId, Long itemId);
}
