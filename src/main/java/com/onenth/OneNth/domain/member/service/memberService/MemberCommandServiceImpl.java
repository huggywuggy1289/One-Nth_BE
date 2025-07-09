package com.onenth.OneNth.domain.member.service.memberService;

import com.onenth.OneNth.domain.member.converter.MemberConverter;
import com.onenth.OneNth.domain.member.dto.MemberRequestDTO;
import com.onenth.OneNth.domain.member.dto.MemberResponseDTO;
import com.onenth.OneNth.domain.member.entity.Member;
import com.onenth.OneNth.domain.member.repository.memberRepository.MemberRepository;
import com.onenth.OneNth.domain.region.entity.Region;
import com.onenth.OneNth.domain.region.repository.RegionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberCommandServiceImpl implements MemberCommandService {

    private final MemberRepository memberRepository;
    private final RegionRepository regionRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public MemberResponseDTO.SignupResultDTO signupMember(MemberRequestDTO.SignupDTO request) {
        // 1. 지역명으로 Region 조회
        Region region = regionRepository.findByRegionName(request.getRegionName())
                .orElseThrow(() -> new RuntimeException("해당 지역이 존재하지 않습니다."));

        // 2. Member + MemberRegion 생성
        Member member = MemberConverter.toMember(request, region);

        //3. 비밀번호 암호화
        member.encodePassword(passwordEncoder.encode(request.getPassword()));

        // 4. 저장
        return MemberConverter.toSignupResultDTO(memberRepository.save(member));
    }
}
