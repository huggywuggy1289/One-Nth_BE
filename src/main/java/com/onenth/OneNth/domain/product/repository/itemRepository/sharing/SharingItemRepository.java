package com.onenth.OneNth.domain.product.repository.itemRepository.sharing;

import com.onenth.OneNth.domain.member.entity.Member;
import com.onenth.OneNth.domain.product.entity.SharingItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SharingItemRepository extends JpaRepository<SharingItem, Long> {
    Optional<SharingItem> findById(Long id);
    List<SharingItem> findByMember(Member member);
}