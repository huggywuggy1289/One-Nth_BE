package com.onenth.OneNth.domain.entity.user;

import com.onenth.OneNth.domain.entity.region.Region;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "user_region")
@IdClass(UserRegionId.class) // 복합키 클래스
public class UserRegion {

    @Id
    private Integer id; // 유저지역 id

    @Id
    private Integer region2; // 지역아이디 PK

    @Id
    private Integer id2; // 회원아이디 PK

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_id", referencedColumnName = "region", nullable = true)
    private Region region; // 지역ID FK

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = true)
    private User user; // 회원ID FK
}
