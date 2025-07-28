package com.onenth.OneNth.domain.member.settings.profile.service;

import com.onenth.OneNth.domain.member.entity.Member;
import com.onenth.OneNth.domain.member.repository.memberRepository.MemberRepository;
import com.onenth.OneNth.domain.member.settings.profile.controller.ProfileController;
import com.onenth.OneNth.domain.member.settings.profile.converter.ProfileConverter;
import com.onenth.OneNth.domain.member.settings.profile.dto.ProfileResponseDTO;
import com.onenth.OneNth.global.apiPayload.code.status.ErrorStatus;
import com.onenth.OneNth.global.apiPayload.exception.GeneralException;
import com.onenth.OneNth.global.aws.s3.AmazonS3Manager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class ProfileCommandServiceImpl implements ProfileCommandService {

    private final MemberRepository memberRepository;
    private final AmazonS3Manager amazonS3Manager;

    @Override
    public ProfileResponseDTO.UpdateProfileImageResponseDTO updateProfileImage(Long userId, MultipartFile image) {

        if (image == null || image.isEmpty()) {
            throw new GeneralException(ErrorStatus.INVALID_PROFILE_IMAGE);
        }

        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

        if (member.getProfileImageUrl() != null) {
            amazonS3Manager.deleteFile(member.getProfileImageUrl());
        }

        String uuid = UUID.randomUUID().toString();
        String keyName = amazonS3Manager.generateProfileKeyName(uuid);
        String imageUrl = amazonS3Manager.uploadFile(keyName, image);

        member.updateProfileImage(imageUrl);

        return ProfileConverter.toUpdateProfileImageResponseDTO(imageUrl);
    }
}
