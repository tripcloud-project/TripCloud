package com.ssafy.project.infra.ai.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class AiConfig {
    @Value("${ssafy.ai.system-prompt}")
    String systemPrompt;

    @Bean
    ChatClient simpleChatClient(ChatClient.Builder builder) {
         return builder.defaultSystem(systemPrompt).build();
    }

}
