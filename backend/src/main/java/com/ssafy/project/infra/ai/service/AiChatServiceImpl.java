package com.ssafy.project.infra.ai.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeType;

@Slf4j
@Service
@RequiredArgsConstructor
public class AiChatServiceImpl implements AiChatService{
    private final ChatClient simpleChatClient;

    public String multiModal(String userInput, MimeType mime, Resource resource) {

        return simpleChatClient.prompt()
                .system(spec -> spec.param("language", "korean").param("character",  "chill"))
                .user(spec -> spec.media(mime, resource).text(userInput))
                .call()
                .content();
    }
}
