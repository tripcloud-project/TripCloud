package com.ssafy.project.ai;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.project.infra.ai.service.AiChatService;
import com.ssafy.project.util.ImageUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.MimeType;
import org.springframework.util.MimeTypeUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;


@Slf4j
@SpringBootTest
public class MultimodalChatTest {
    @Autowired
    AiChatService aiService;

    @Test
    public void multiModalTest() {
        try {
            BufferedImage image = ImageIO.read(new ClassPathResource("/static/img/multimodal.test.jpg").getInputStream());

            String prompt = """
                    이 그림을 보고 연관 키워드 5개를 추출해서, 아래 형식의 JSON으로만 응답해줘.
                    **백틱(```) 없이 순수한 JSON만 출력해. 설명도 하지 마.**
                    예시:
                    {
                      "keywords": ["고양이", "귀여움", "동물", "잠", "애완동물"]
                    }
                    """;
            MimeType mimeType = MimeTypeUtils.parseMimeType("image/jpeg");
            ByteArrayResource resource = ImageUtil.resizeAndCompress(image, 600, 0.6f);

            long startTime = System.currentTimeMillis(); // 시작 시간 측정

            String result = aiService.multiModal(prompt, mimeType, resource);

            long endTime = System.currentTimeMillis(); // 종료 시간 측정
            long duration = endTime - startTime;

            log.debug("API 응답 시간: {} ms", duration);
            log.debug("Raw result: {}", result);

            // JSON 파싱
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(result);
            List<String> keywords = new ArrayList<>();
            for (JsonNode keyword : jsonNode.get("keywords")) {
                keywords.add(keyword.asText());
            }
            for (String keyword : keywords) {
                log.debug("Keyword: {}", keyword);
            }

            log.debug("Parsed keywords: {}", keywords);
        } catch (Exception e) {
            log.error("실패 로그: {}", e.getMessage());
        }
    }
}
