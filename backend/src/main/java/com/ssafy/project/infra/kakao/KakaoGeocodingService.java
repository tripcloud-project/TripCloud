package com.ssafy.project.domain.gallery.service;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.ssafy.project.domain.gallery.dto.internal.AddressDto;

import lombok.Getter;
import lombok.Setter;

@Service
public class KakaoGeocodingServiceImpl implements KakaoGeocodingService {
    @Value("${kakao.rest-api-key}")
    private String kakaoRestApiKey;
    
    private final OkHttpClient client = new OkHttpClient();

    @Override
	public AddressDto reverseGeocode(double lat, double lng) {
        String url = String.format(
            "https://dapi.kakao.com/v2/local/geo/coord2regioncode.json?x=%f&y=%f",
            lng, lat  // ⚠️ 순서 주의! (x: longitude, y: latitude)
        );

        Request request = new Request.Builder()
            .url(url)
            .addHeader("Authorization", kakaoRestApiKey)
            .build();

        try (Response response = client.newCall(request).execute()) {
        	if (!response.isSuccessful()) {
                System.err.println("❌ 요청 실패! 응답 코드: " + response.code());
                System.err.println("❌ 응답 메시지: " + response.message());
                System.err.println("❌ 응답 본문: " + response.body().string());
                return null;
            }
            String body = response.body().string();
            JSONObject json = new JSONObject(body);
            JSONArray documents = json.getJSONArray("documents");

            if (documents.length() > 0) {
                JSONObject region = documents.getJSONObject(0);
                return new AddressDto(
                    region.getString("region_1depth_name"), // 시도
                    region.getString("region_2depth_name"), // 시군구
                    region.getString("region_3depth_name")  // 읍면동
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

}
