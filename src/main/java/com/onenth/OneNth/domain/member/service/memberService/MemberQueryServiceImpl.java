package com.onenth.OneNth.domain.member.service.memberService;

import com.onenth.OneNth.domain.member.converter.MemberConverter;
import com.onenth.OneNth.domain.member.dto.MemberResponseDTO;
import com.onenth.OneNth.domain.member.repository.memberRepository.MemberRepository;
import com.onenth.OneNth.domain.post.entity.Like;
import com.onenth.OneNth.domain.post.entity.Post;
import com.onenth.OneNth.domain.post.entity.Scrap;
import com.onenth.OneNth.domain.post.repository.likeRepository.LikeRepository;
import com.onenth.OneNth.domain.post.repository.postRepository.PostRepository;
import com.onenth.OneNth.domain.post.repository.scrapRepository.ScrapRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.print.Pageable;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberQueryServiceImpl implements MemberQueryService {

    private final MemberRepository memberRepository;
    private final ScrapRepository scrapRepository;
    private final LikeRepository likeRepository;
    private final PostRepository postRepository;

    @Override
    public MemberResponseDTO.PostListDTO getScrappedPosts(Long memberId, Integer page, Integer size) {

        if (memberId == null) {
            return MemberResponseDTO.PostListDTO.builder().build();
        }

        Page<Scrap> scrappedPostPage = scrapRepository.
                findByMemberId(memberId, PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt")));

        MemberResponseDTO.PostListDTO postPreviews = MemberConverter.toScrappedPostPreviewListDTO(scrappedPostPage, page);

        return postPreviews;
    }

    @Override
    public MemberResponseDTO.PostListDTO getLikedPosts(Long memberId, Integer page, Integer size) {
        if (memberId == null) {
            return MemberResponseDTO.PostListDTO.builder().build();
        }

        Page<Like> likedPostPage = likeRepository.
                findByMemberId(memberId, PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt")));

        MemberResponseDTO.PostListDTO postPreviews = MemberConverter.toLikedPostPreviewListDTO(likedPostPage, page);

        return postPreviews;
    }

    @Override
    public MemberResponseDTO.PostListDTO getMyPosts(Long memberId, Integer page, Integer size) {
        if (memberId == null) {
            return MemberResponseDTO.PostListDTO.builder().build();
        }

        Page<Post> myPostPage = postRepository.
                findByMemberId(memberId, PageRequest.of(page -1, size, Sort.by(Sort.Direction.DESC, "createdAt")));

        MemberResponseDTO.PostListDTO postPreviews = MemberConverter.toMyPostPreviewListDTO(myPostPage, page);

        return postPreviews;

    }
}
