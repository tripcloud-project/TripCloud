package com.ssafy.project.infra.kakao;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.ssafy.project.domain.attraction.dto.response.AttractionResponseDto;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@Service
public class KakaoSearchService {
    @Value("${kakao.rest-api-key}")
    private String kakaoRestApiKey;
    
    private final OkHttpClient client = new OkHttpClient();

	public List<AttractionResponseDto> SearchRecommendedSpots(String sido, String sigungu) {
        String url = String.format(
        	"https://dapi.kakao.com/v2/local/search/keyword.json?query=%s %s의 관광명소",
        	sido, sigungu
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

            List<AttractionResponseDto> attractionList = new ArrayList<>();
            for(int i=0; i<Math.min(documents.length(), 5); i++) {
            	JSONObject attraction = documents.getJSONObject(i);
            	
            	AttractionResponseDto responseDto = AttractionResponseDto.builder()
            			.name(attraction.getString("place_name"))
            			.category(attraction.getString("category_name"))
            			.address(attraction.getString("address_name"))
            			.placeURL(attraction.getString("place_url"))
            			.build();
            	
            	attractionList.add(responseDto);
            }
            
            return attractionList;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

}
