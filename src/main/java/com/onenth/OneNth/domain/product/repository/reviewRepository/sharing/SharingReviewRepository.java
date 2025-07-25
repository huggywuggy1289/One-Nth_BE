package com.onenth.OneNth.domain.product.repository.reviewRepository.sharing;

import com.onenth.OneNth.domain.member.entity.Member;
import com.onenth.OneNth.domain.product.entity.review.SharingReview;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SharingReviewRepository extends JpaRepository<SharingReview, Long> {
    List<SharingReview> findByMember(Member member);
    Page<SharingReview> findByMember(Member member, Pageable pageable);
}
