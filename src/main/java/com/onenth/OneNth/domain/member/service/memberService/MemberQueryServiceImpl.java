package com.onenth.OneNth.domain.member.service.memberService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class MemberQueryServiceImpl implements MemberQueryService {
}
