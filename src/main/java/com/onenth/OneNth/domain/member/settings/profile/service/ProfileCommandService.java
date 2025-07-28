package com.onenth.OneNth.domain.member.settings.profile.service;

import com.onenth.OneNth.domain.member.settings.profile.dto.ProfileResponseDTO;
import org.springframework.web.multipart.MultipartFile;

public interface ProfileCommandService {

    ProfileResponseDTO.UpdateProfileImageResponseDTO updateProfileImage(Long userId, MultipartFile image);
}
