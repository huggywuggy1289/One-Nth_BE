package com.onenth.OneNth.domain.entity.user;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@IdClass(BlockId.class)
@Table(name = "block")
public class Block {

    @Id
    @Column(name = "user_id", nullable = false)
    private Long userId; // 회원id1 - 차단한 유저

    @Id
    @Column(name = "user_id2", nullable = false)
    private Long userId2; //회원id2 - 차단 당한 유저
}

