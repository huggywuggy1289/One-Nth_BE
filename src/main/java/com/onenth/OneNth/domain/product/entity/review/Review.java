package com.onenth.OneNth.domain.product.entity.review;

import com.onenth.OneNth.domain.member.entity.Member;

import java.math.BigDecimal;
import java.util.List;

public interface Review {
    Member getMember();
    String getContent();
    BigDecimal getRate();
    List<ReviewImage> getReviewImages();

    void setContent(String content);
    void setRate(BigDecimal rate);
}