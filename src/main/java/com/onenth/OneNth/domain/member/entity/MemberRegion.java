package com.onenth.OneNth.domain.member.entity;

import com.onenth.OneNth.domain.common.BaseEntity;
import com.onenth.OneNth.domain.region.entity.Region;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
public class MemberRegion extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_id", nullable = false)
    private Region region;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    public static MemberRegion of(Member member, Region region) {
        return MemberRegion.builder()
                .member(member)
                .region(region)
                .build();
    }
}