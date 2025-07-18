package com.onenth.OneNth.domain.auth.service;

import com.onenth.OneNth.domain.auth.dto.KakaoRequestDTO;
import com.onenth.OneNth.domain.auth.dto.KakaoResponseDTO;
import com.onenth.OneNth.domain.auth.dto.KakaoUserInfo;
import com.onenth.OneNth.domain.member.converter.MemberConverter;
import com.onenth.OneNth.domain.member.entity.Member;
import com.onenth.OneNth.domain.member.entity.enums.LoginType;
import com.onenth.OneNth.domain.member.repository.memberRepository.MemberRepository;
import com.onenth.OneNth.domain.region.entity.Region;
import com.onenth.OneNth.domain.region.repository.RegionRepository;
import com.onenth.OneNth.global.configuration.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class KakaoAuthService {

    @Value("${kakao.redirect_uri}")
    private String redirectUri;
    @Value("${kakao.client_id}")
    private String clientId;

    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final RegionRepository regionRepository;


    // 카카오 로그인 요청 처리 서비스
    public KakaoResponseDTO.KakaoLoginResponseDTO processKakaoLogin (KakaoRequestDTO.KakaoLoginRequestDTO request) {
        String accessToken = getKakaoAccessToken(request.getCode());
        KakaoUserInfo userInfo = getUserInfo(accessToken);

        Optional<Member> member = memberRepository.findBySocialIdAndLoginType(userInfo.getId(), LoginType.KAKAO);

        //member 에 객체가 들어있는지 확인
        if (member.isPresent()) {
            //이미 가입한적 있다면 로그인 완료
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    member.get().getId(),      // Principal (User Identifier: subject에 들어감)
                    null,
                    Collections.emptyList()
            );
            String token = jwtTokenProvider.generateToken(authentication);

            return KakaoResponseDTO.KakaoLoginResponseDTO.builder()
                    .access_token(token)
                    .isNew(false)
                    .build();
        }
        //기존 가입 정보가 없는 경우 추가 정보 필요
        else {
            return KakaoResponseDTO.KakaoLoginResponseDTO.builder()
                    .email(userInfo.getEmail())
                    .name(userInfo.getNickname())
                    .serialId(userInfo.getId())
                    .isNew(true)
                    .build();
        }
    }
    // 카카오 회원가입 서비스
    public KakaoResponseDTO.KakaoLoginResponseDTO signupKakaoMember(KakaoRequestDTO.KakaoSignupRequestDTO request){
        Region region = regionRepository.findByRegionName(request.getRegionName())
                .orElseThrow(() -> new RuntimeException("존재하지 않는 지역 이름 입니다."));

        if (memberRepository.existsByEmailAndLoginType(request.getEmail(), LoginType.NORMAL)) {
            throw new RuntimeException("이미 일반 회원가입을 진행한 사용자입니다.");
        }

        Member member = MemberConverter.toMember(request, region);


        memberRepository.save(member);

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                member.getId(),
                null,
                Collections.emptyList()
        );

        String token = jwtTokenProvider.generateToken(authentication);

        return KakaoResponseDTO.KakaoLoginResponseDTO.builder()
                .access_token(token)
                .isNew(false)
                .build();
    }

    //프론트에서 받은 인가 코드로 access token 요청하는 메서드 구현
    private String getKakaoAccessToken(String authorizationCode) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", clientId);
        params.add("redirect_uri", redirectUri);
        params.add("code", authorizationCode);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
        ResponseEntity<Map> response = restTemplate.postForEntity(
                "https://kauth.kakao.com/oauth/token",
                request,
                Map.class
        );

        return (String) response.getBody().get("access_token");
    }

    // 카카오에서 발급받은 access 토큰을 이용해서 사용자 정보 요청하는 메서드
    private KakaoUserInfo getUserInfo(String accessToken) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        HttpEntity<String> request = new HttpEntity<>(headers);
        ResponseEntity<Map> response = restTemplate.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.GET,
                request,
                Map.class
        );

        Map<String, Object> body = response.getBody();
        Map<String, Object> kakaoAccount = (Map<String, Object>) body.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");

        return new KakaoUserInfo(
                (String) kakaoAccount.get("email"),
                (String) profile.get("nickname"),
                String.valueOf(body.get("id"))
        );
    }
}
