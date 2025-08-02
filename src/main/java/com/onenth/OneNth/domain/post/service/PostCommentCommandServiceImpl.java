package com.onenth.OneNth.domain.post.service;

import com.onenth.OneNth.domain.member.entity.Member;
import com.onenth.OneNth.domain.member.repository.memberRepository.MemberRepository;
import com.onenth.OneNth.domain.post.dto.PostCommentSaveRequestDTO;
import com.onenth.OneNth.domain.post.entity.Post;
import com.onenth.OneNth.domain.post.entity.PostComment;
import com.onenth.OneNth.domain.post.repository.PostRepository;
import com.onenth.OneNth.domain.post.repository.commentRepository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostCommentCommandServiceImpl implements PostCommentCommandService{

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public Long saveComment(Long postId, Long memberId, PostCommentSaveRequestDTO requestDto) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        PostComment comment = PostComment.builder()
                .post(post)
                .member(member)
                .content(requestDto.getContent())
                .build();

        commentRepository.saveAndFlush(comment);

        return comment.getId();
    }

    @Override
    @Transactional
    public void deleteComment(Long commentId, Long memberId) {
        PostComment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 댓글입니다."));

        if (!comment.getMember().getId().equals(memberId)) {
            throw new SecurityException("본인의 댓글만 삭제할 수 있습니다.");
        }

        commentRepository.delete(comment);
    }

    @Override
    public PostComment findById(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 댓글입니다."));
    }
}
