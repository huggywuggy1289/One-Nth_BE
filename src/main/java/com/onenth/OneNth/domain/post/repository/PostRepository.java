package com.onenth.OneNth.domain.post.repository;

import com.onenth.OneNth.domain.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.onenth.OneNth.domain.post.entity.enums.PostType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("SELECT p FROM Post p WHERE p.postType = :postType " +
            "AND (:keyword IS NULL OR LOWER(p.title) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(p.content) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<Post> findByPostTypeAndKeyword(
            @Param("postType") PostType postType,
            @Param("keyword") String keyword,
            Pageable pageable
    );

    @Query("SELECT p FROM Post p WHERE p.postType = :postType " +
            "AND (:regionName IS NULL OR p.regionName LIKE %:regionName%) "  +
            "AND (:keyword IS NULL OR LOWER(p.title) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(p.content) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<Post> findByPostTypeAndRegionNameAndKeyword(
            @Param("postType") PostType postType,
            @Param("regionName") String regionName,
            @Param("keyword") String keyword,
            Pageable pageable
    );

    @Query("SELECT p FROM Post p " +
            "JOIN FETCH p.member " +
            "LEFT JOIN FETCH p.region " +
            "WHERE p.id = :postId")
    Optional<Post> findByIdWithMemberAndRegion(@Param("postId") Long postId);

    @Query("SELECT DISTINCT p FROM Post p " +
            "JOIN p.postTag pt " +
            "JOIN pt.tag t " +
            "WHERE p.postType = :postType " +
            "AND LOWER(t.name) LIKE LOWER(CONCAT('%', :tagName, '%'))")
    Page<Post> findByPostTypeAndTagName(
            @Param("postType") PostType postType,
            @Param("tagName") String tagName,
            Pageable pageable);

    @Query("SELECT DISTINCT p FROM Post p " +
            "JOIN p.postTag pt " +
            "JOIN pt.tag t " +
            "WHERE p.postType = :postType " +
            "AND p.regionName LIKE %:regionName% " +
            "AND LOWER(t.name) LIKE LOWER(CONCAT('%', :tagName, '%'))")
    Page<Post> findByPostTypeAndRegionNameAndTagName(
            @Param("postType") PostType postType,
            @Param("regionName") String regionName,
            @Param("tagName") String tagName,
            Pageable pageable);

    //마이페이지 - 내가 쓴 글 조회
    Page<Post> findByMemberId(Long memberId, Pageable pageable);
}
