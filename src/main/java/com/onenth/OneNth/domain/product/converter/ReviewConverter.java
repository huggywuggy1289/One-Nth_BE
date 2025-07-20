package com.onenth.OneNth.domain.product.converter;

import com.onenth.OneNth.domain.member.entity.Member;
import com.onenth.OneNth.domain.product.dto.ReviewRequestDTO;
import com.onenth.OneNth.domain.product.entity.SharingItem;
import com.onenth.OneNth.domain.product.entity.SharingReview;

public class ReviewConverter {

    public static SharingReview toSharingReview(ReviewRequestDTO.createSharingReview request,Member member,SharingItem sharingItem) {
        return SharingReview.builder()
                .rate(request.getRate())
                .content(request.getContent())
                .member(member)
                .sharingItem(sharingItem)
                .build();
    }
}