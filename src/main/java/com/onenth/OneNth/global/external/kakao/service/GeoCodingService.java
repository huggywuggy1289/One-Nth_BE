package com.onenth.OneNth.global.external.kakao.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.onenth.OneNth.global.external.kakao.dto.GeoCodingResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
public class GeoCodingService {

    @Value("${kakao.api.key}")
    private String kakaoApiKey;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public GeoCodingResult getCoordinatesFromAddress(String address) {

        try {
            String apiUrl = "https://dapi.kakao.com/v2/local/search/address.json";
            String encodedAddress = URLEncoder.encode(address, StandardCharsets.UTF_8);

            URL url = new URL(apiUrl + "?query=" + encodedAddress);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            System.out.println("Kakao API Key: " + kakaoApiKey);
            conn.setRequestProperty("Authorization", "KakaoAK " + kakaoApiKey);
            conn.setRequestProperty("Content-Type", "application/json");

            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                response.append(line);
            }
            rd.close();

            // Jackson으로 JSON 파싱
            JsonNode root = objectMapper.readTree(response.toString());
            JsonNode documents = root.path("documents");

            if (documents.isArray() && !documents.isEmpty()) {
                JsonNode firstDoc = documents.get(0);
                double longitude = Double.parseDouble(firstDoc.path("x").asText());
                double latitude = Double.parseDouble(firstDoc.path("y").asText());
                JsonNode addressNode = firstDoc.path("address");
                String region1 = addressNode.path("region_1depth_name").asText();
                String region2 = addressNode.path("region_2depth_name").asText();
                String region3 = addressNode.path("region_3depth_name").asText();
                String fullRegionName = region1 + " " + region2 + " " + region3;

                return new GeoCodingResult(latitude, longitude, fullRegionName);

            } else {
                System.err.println("⚠️ 주소 결과 없음: " + address);
            }

        } catch (Exception e) {
            System.err.println("❌ Geocoding 예외 발생: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }
}
