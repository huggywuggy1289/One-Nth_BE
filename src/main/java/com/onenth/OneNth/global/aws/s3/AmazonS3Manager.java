package com.onenth.OneNth.global.aws.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.onenth.OneNth.global.configuration.AmazonConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class AmazonS3Manager {

    private final AmazonS3 amazonS3;
    private final AmazonConfig amazonConfig;

    public String uploadFile(String keyName, MultipartFile file) { // 파일 업로드
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(file.getSize());
        objectMetadata.setContentType(file.getContentType());
        try {
            amazonS3.putObject(
                    new PutObjectRequest(amazonConfig.getBucket(), keyName, file.getInputStream(), objectMetadata));
        } catch (IOException e) {
            log.error("error at AmazonS3Manager uploadFile : {}", (Object) e.getStackTrace());
        }
        return amazonS3.getUrl(amazonConfig.getBucket(), keyName).toString();
    }

    public String deleteFile(String uploadFilePath) { // 파일 삭제
        String result = "success";
        try {
            String bucketUrl = "https://" + amazonConfig.getBucket() + ".s3." + amazonConfig.getRegion() + ".amazonaws.com/";
            String keyName = uploadFilePath.replace(bucketUrl, "");
            boolean isObjectExist = amazonS3.doesObjectExist(amazonConfig.getBucket(), keyName);
            if (isObjectExist) {
                amazonS3.deleteObject(amazonConfig.getBucket(), keyName);
            } else {
                result = "file not found";
            }
        } catch (Exception e) {
            log.debug("Delete File failed", e);
        }
        return result;
    }

    // 채팅 사진 저장은 해당 url 을 사용하여 저장
    public String generateChatKeyName(String uuid) {
        return amazonConfig.getChat() + '/' + uuid;
    }
    // 할인 정보 사진 저장은 해당 url 을 사용하여 저장
    public String generateDiscountKeyName(String uuid) {
        return amazonConfig.getDiscount() + '/' + uuid;
    }
    // 샐활 꿀팁 사진 저장은 해당 url 을 사용하여 저장
    public String generateLifeTipKeyName(String uuid) {
        return amazonConfig.getLifeTip() + '/' + uuid;
    }
    // 우리 동네 맛집 저장은 해당 url 을 사용하여 저장
    public String generateLocalFoodKeyName(String uuid) {
        return amazonConfig.getLocalFood() + '/' + uuid;
    }
    // 프로필 저장은 해당 url 을 사용하여 저장
    public String generateProfileKeyName(String uuid) {
        return amazonConfig.getProfile() + '/' + uuid;
    }
    // 리뷰 사진 저장은 해당 url 을 사용하여 저장
    public String generateReviewKeyName(String uuid) {
        return amazonConfig.getReview() + '/' + uuid;
    }
    // 함께 사요 사진 저장은 해당 url 을 사용하여 저장
    public String generatePurchaseKeyName(String uuid) {
        return amazonConfig.getPurchase() + '/' + uuid;
    }
    // 함께 나눠요 사진 저장은 해당 url 을 사용하여 저장
    public String generateShareKeyName(String uuid) {
        return amazonConfig.getShare() + '/' + uuid;
    }
}