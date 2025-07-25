package com.onenth.OneNth.domain.product.repository.reviewRepository.purchase;

import com.onenth.OneNth.domain.member.entity.Member;
import com.onenth.OneNth.domain.product.entity.review.PurchaseReview;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PurchaseReviewRepository extends JpaRepository<PurchaseReview, Long> {
    List<PurchaseReview> findByMember(Member member);
    Page<PurchaseReview> findByMember(Member member, Pageable pageable);
}
