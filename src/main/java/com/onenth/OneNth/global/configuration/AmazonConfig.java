package com.onenth.OneNth.global.configuration;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class AmazonConfig {

    private AWSCredentials awsCredentials;

    @Value("${AWS_ACCESS_KEY_ID}")
    private String accessKey;

    @Value("${AWS_SECRET_ACCESS_KEY}")
    private String secretKey;

    @Value("${AWS_REGION}")
    private String region;

    @Value("${AWS_S3_BUCKET}")
    private String bucket;

    @Value("${cloud.aws.s3.path.chat}")
    private String chat;

    @Value("${cloud.aws.s3.path.discount}")
    private String discount;

    @Value("${cloud.aws.s3.path.life-tip}")
    private String lifeTip;

    @Value("${cloud.aws.s3.path.local-food}")
    private String localFood;

    @Value("${cloud.aws.s3.path.profile}")
    private String profile;

    @Value("${cloud.aws.s3.path.review}")
    private String review;

    @Value("${cloud.aws.s3.path.purchase}")
    private String purchase;

    @Value("${cloud.aws.s3.path.share}")
    private String share;

    @PostConstruct
    public void init() {
        this.awsCredentials = new BasicAWSCredentials(accessKey, secretKey);
    }

    @Bean
    public AmazonS3 amazonS3() {
        AWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretKey);
        return AmazonS3ClientBuilder.standard()
                .withRegion(region)
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .build();
    }

    @Bean
    public AWSCredentialsProvider awsCredentialsProvider() {
        return new AWSStaticCredentialsProvider(awsCredentials);
    }
}