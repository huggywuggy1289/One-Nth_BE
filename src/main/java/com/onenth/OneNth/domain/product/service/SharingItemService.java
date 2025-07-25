package com.onenth.OneNth.domain.product.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.onenth.OneNth.domain.member.entity.Member;
import com.onenth.OneNth.domain.member.repository.memberRepository.MemberRegionRepository;
import com.onenth.OneNth.domain.product.dto.SharingItemRequestDTO;
import com.onenth.OneNth.domain.product.entity.ItemImage;
import com.onenth.OneNth.domain.product.entity.SharingItem;
import com.onenth.OneNth.domain.product.entity.enums.ItemCategory;
import com.onenth.OneNth.domain.product.entity.enums.Status;
import com.onenth.OneNth.domain.product.repository.itemRepository.ItemImageRepository;
import com.onenth.OneNth.domain.product.repository.itemRepository.TagRepository;
import com.onenth.OneNth.domain.product.repository.itemRepository.sharing.SharingItemRepository;
import com.onenth.OneNth.domain.region.entity.Region;
import lombok.RequiredArgsConstructor;
import com.onenth.OneNth.domain.product.entity.Tag;
import com.onenth.OneNth.domain.product.entity.enums.ItemType;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class SharingItemService {
    private final SharingItemRepository sharingItemRepository;
    private final ItemImageRepository itemImageRepository;
    private final MemberRegionRepository memberRegionRepository;
    private final TagRepository tagRepository;
    private final AmazonS3 amazonS3;

    @Value("${AWS_S3_BUCKET}")
    private String bucketName;

    @Transactional
    public Long registerItem(SharingItemRequestDTO dto, List<MultipartFile> imageFiles, Long userId){

        // 소비기한 유효성 검사
        if (dto.getItemCategory() == ItemCategory.FOOD) {
            if (dto.getExpirationDate() == null) {
                throw new IllegalArgumentException("식품 카테고리는 소비기한을 입력해야 합니다.");
            }
        } else {
            if (dto.getExpirationDate() != null) {
                throw new IllegalArgumentException("식품 외 카테고리는 소비기한을 입력할 수 없습니다.");
            }
        }

        // 회원연동
        Member member = Member.builder().id(userId).build();
        // 이어서 회원가입시 등록된 대표지역도 주입
        Region region = memberRegionRepository.findByMemberId(userId)
                .stream()
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("회원의 대표지역이 설정되지 않았습니다."))
                .getRegion();

        // 태그 유효성 검사
        List<Tag> tagEntities = dto.getTags().stream()
                .peek(tag -> {
                    if (!tag.startsWith("#")) {
                        throw new IllegalArgumentException("태그는 반드시 #으로 시작해야 합니다: " + tag);
                    }
                })
                .map(tag -> tagRepository.findByName(tag)
                        .orElseGet(() -> tagRepository.save(Tag.builder().name(tag).build())))
                .toList();

        // 이미지 유효성 검사
        if (imageFiles == null || imageFiles.isEmpty()) {
            throw new IllegalArgumentException("상품 이미지는 최소 1장 이상 첨부해야 합니다.");
        }

        // 유효한 파일만 개수 체크
        long validFileCount = Optional.ofNullable(imageFiles)
                .orElse(Collections.emptyList())
                .stream()
                .filter(f -> f != null && !f.isEmpty())
                .count();

        if (validFileCount < 1) {
            throw new IllegalArgumentException("상품 이미지는 최소 1장 이상 첨부해야 합니다.");
        }

        if (validFileCount > 3) {
            throw new IllegalArgumentException("이미지는 최대 3장까지 업로드할 수 있습니다.");
        }

        SharingItem sharingItem = SharingItem.builder()
                .title(dto.getTitle())
                .quantity(dto.getQuantity())
                .price(dto.getPrice())
                .itemCategory(dto.getItemCategory())
                .expirationDate(dto.getExpirationDate())
                .isAvailable(dto.getIsAvailable())
                .purchaseMethod(dto.getPurchaseMethod())
                .member(member)
                .region(region)
                .status(Status.DEFAULT)
                .tags(new ArrayList<>())
                .build();

        sharingItemRepository.save(sharingItem);

        // 이미지 업로드 처리

        imageFiles.stream()
                .filter(f -> f != null && !f.isEmpty())
                .forEach(file -> {
                    try {
                        String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
                        String s3Key = "sharing/" + filename;

                        ObjectMetadata metadata = new ObjectMetadata();
                        metadata.setContentLength(file.getSize());
                        metadata.setContentType(file.getContentType());

                        amazonS3.putObject(bucketName, s3Key, file.getInputStream(), metadata);

                        String s3Url = amazonS3.getUrl(bucketName, s3Key).toString();

                        // 디버깅
                        boolean exists = amazonS3.doesObjectExist(bucketName, s3Key);
                        if (!exists) {
                            throw new RuntimeException("S3에 파일이 존재하지 않습니다: " + s3Key);
                        } else {
                            System.out.println(s3Key + " : 존재합니다");
                        }

                        // DB저장
                        ItemImage image = ItemImage.builder()
                                .sharingItem(sharingItem)
                                .url(s3Url) // S3 URL을 저장
                                .itemType(ItemType.SHARE)
                                .build();

                        itemImageRepository.save(image);
                    } catch (IOException e) {
                        throw new RuntimeException("S3 파일 업로드 실패: " + e.getMessage());
                    }
                });

        return sharingItem.getId();
    }
}
