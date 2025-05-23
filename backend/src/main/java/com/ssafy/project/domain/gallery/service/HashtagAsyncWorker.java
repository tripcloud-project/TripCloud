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
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class HashtagAsyncWorker {

    private final AiChatService aiChatService;
    private final HashtagRepository hashtagRepository;

    @Async
    public CompletableFuture<Void> processOneFileAsync(ByteFileDto byteFileDto) {
        try {
            String contentType = byteFileDto.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                return CompletableFuture.completedFuture(null);
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

            String result = aiChatService.multiModal(prompt, mimeType, resource);

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(result);
            List<String> keywords = new ArrayList<>();
            for (JsonNode keyword : jsonNode.get("keywords")) {
                keywords.add(keyword.asText());
            }

            insertHashtags(byteFileDto.getS3Key(), keywords);
            log.debug("Parsed keywords: {}", keywords);

        } catch (Exception e) {
            log.error("비동기 해시태그 분석 실패: {}", byteFileDto.getS3Key(), e);
        }

        return CompletableFuture.completedFuture(null);
    }


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
}
