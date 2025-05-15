package com.ssafy.project.domain.gallery.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.project.domain.gallery.dto.internal.ByteFileDto;
import com.ssafy.project.domain.gallery.repository.HashtagRepository;
import com.ssafy.project.infra.ai.service.AiChatService;
import com.ssafy.project.util.ImageUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeType;
import org.springframework.util.MimeTypeUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class HashtagServiceImpl implements HashtagService{
    private final AiChatService aiChatService;
    private final HashtagRepository hashtagRepository;


    public void insertHashtags(String s3Key, List<String> keywords) {
        Long fileId = hashtagRepository.findFileIdByS3Key(s3Key);
        if (fileId == null) throw new RuntimeException("파일을 찾을 수 없습니다: " + s3Key);

        for (String keyword : keywords) {
            Long hashtagId = hashtagRepository.findHashtagIdByKeyword(keyword);
            if (hashtagId == null) {
                hashtagRepository.insertHashtag(keyword);
                hashtagId = hashtagRepository.findHashtagIdByKeyword(keyword); // 재조회
            }
            hashtagRepository.insertHashtagFile(fileId, hashtagId);
        }
    }


    @Override
    @Async
    public void parseHashtag(List<ByteFileDto> byteFileList) {
        // 시간이 오래 걸리는 작업
        for (ByteFileDto byteFileDto : byteFileList) {
            try {
                String contentType = byteFileDto.getContentType();
                if (contentType == null || !contentType.startsWith("image/")) {
                    continue;
                }

                String prompt = """
                    이 그림을 보고 연관 키워드 5개를 추출해서, 아래 형식의 JSON으로만 응답해줘.
                    **백틱(```) 없이 순수한 JSON만 출력해. 설명도 하지 마.**
                    예시:
                    {
                      "keywords": ["고양이", "귀여움", "동물", "잠", "애완동물"]
                    }
                    """;

                ByteArrayInputStream inputStream = new ByteArrayInputStream(byteFileDto.getContent());
                BufferedImage image = ImageIO.read(inputStream);
                ByteArrayResource resource = ImageUtil.resizeAndCompress(image, 600, 0.6f);

                MimeType mimeType = MimeTypeUtils.parseMimeType(contentType);

                long startTime = System.currentTimeMillis(); // 시작 시간 측정

                String result = aiChatService.multiModal(prompt, mimeType, resource);

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

                String s3Key = byteFileDto.getS3Key();

                insertHashtags(s3Key, keywords);

                log.debug("Parsed keywords: {}", keywords);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
