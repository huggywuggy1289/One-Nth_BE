package com.onenth.OneNth.domain.entity.user;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class UserRegionId implements Serializable {
    private Integer id;
    private Integer region2;
    private Integer id2;
}

