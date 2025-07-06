package com.onenth.OneNth.domain.entity.user;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class BlockId implements Serializable {

    private Long userId; // 회원id1 - 차단한 유저
    private Long userId2; //회원id2 - 차단 당한 유저
}
