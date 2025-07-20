package com.onenth.OneNth.domain.product.entity.review;

import com.onenth.OneNth.domain.member.entity.Member;

import java.math.BigDecimal;

public interface Review {
    Member getMember();
    String getContent();
    BigDecimal getRate();

    void setContent(String content);
    void setRate(BigDecimal rate);
}