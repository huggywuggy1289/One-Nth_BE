package com.onenth.OneNth.domain.member.repository.emailVerificationTokenRepository;

import aj.org.objectweb.asm.commons.Remapper;
import com.onenth.OneNth.domain.member.entity.EmailVerificationCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmailVerificationCodeRepository extends JpaRepository<EmailVerificationCode, Long> {

    Optional<EmailVerificationCode> findTopByEmailOrderByExpiresAtDesc(String email);

    boolean existsByEmail(String email);

    Optional<EmailVerificationCode> findTopByEmailAndPurposeOrderByExpiresAtDesc(String email, String purpose);
}
