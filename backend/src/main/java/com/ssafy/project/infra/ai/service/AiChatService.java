package com.ssafy.project.infra.ai.service;

import org.springframework.core.io.Resource;
import org.springframework.util.MimeType;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface AiChatService {
    public default String multiModal(String userInput, MimeType mime, Resource resource) {
        throw new RuntimeException("not yet ready");
    }
}
