package com.onenth.OneNth.domain.product.entity.review;

import com.onenth.OneNth.domain.member.entity.Member;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface Review {
    Member getMember();
    Long getId();
    String getContent();
    BigDecimal getRate();
    List<ReviewImage> getReviewImages();
    LocalDateTime getCreatedAt();

    void setContent(String content);
    void setRate(BigDecimal rate);
}