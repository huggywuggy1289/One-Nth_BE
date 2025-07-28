package com.onenth.OneNth.domain.member.entity;

import com.onenth.OneNth.domain.alert.entity.Alert;
import com.onenth.OneNth.domain.chat.entity.ChatRoomMember;
import com.onenth.OneNth.domain.common.BaseEntity;
import com.onenth.OneNth.domain.member.entity.enums.LoginType;
import com.onenth.OneNth.domain.post.entity.Post;
import com.onenth.OneNth.domain.post.entity.PostComment;
import com.onenth.OneNth.domain.product.entity.PurchaseItem;
import com.onenth.OneNth.domain.product.entity.review.PurchaseReview;
import com.onenth.OneNth.domain.product.entity.SharingItem;
import com.onenth.OneNth.domain.product.entity.review.SharingReview;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length=10, nullable=false)
    private String name;

    @Column(length=254, nullable=false, unique = true)
    private String email;

    //@Column(length=300, nullable=false) 소셜로그인은 NULL 가능
    private String password;

    @Column(length=10, nullable=false)
    private String nickname;

//    @Column(nullable=false)
//    private LocalDate birthday;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LoginType loginType;

    private String socialId;

    private LocalDate inactiveDate;

    @Column(nullable=false)
    private boolean marketingAgree = false;

    //비밀번호 salt 암호화 메서드
    public void encodePassword(String password) {
        this.password = password;
    }

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MemberRegion> memberRegions = new ArrayList<>();

    public void addMemberRegion(MemberRegion memberRegion) {
        this.memberRegions.add(memberRegion);
        memberRegion.setMember(this);
    }

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Alert> alerts = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostComment> postComments = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PurchaseReview> purchaseReviews = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SharingReview> sharingReviews = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SharingItem> sharingItems = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PurchaseItem> purchaseItems = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChatRoomMember> chatRoomMembers = new ArrayList<>();
}