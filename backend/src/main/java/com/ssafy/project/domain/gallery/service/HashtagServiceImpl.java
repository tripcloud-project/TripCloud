package com.ssafy.project.domain.gallery.service;

import com.ssafy.project.domain.gallery.dto.internal.ByteFileDto;
import com.ssafy.project.domain.gallery.dto.internal.HashtagDto;
import com.ssafy.project.domain.gallery.repository.HashtagRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class HashtagServiceImpl implements HashtagService{
    private final HashtagRepository hashtagRepository;
    private final HashtagAsyncWorker hashtagAsyncWorker;

    @Override
    @Async
    public void parseHashtag(List<ByteFileDto> byteFileList) {
        List<CompletableFuture<Void>> futures = new ArrayList<>();

        long startTime = System.currentTimeMillis(); // 시작 시간 측정
        for (ByteFileDto byteFileDto : byteFileList) {
            futures.add(hashtagAsyncWorker.processOneFileAsync(byteFileDto));
        }
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join(); // 전부 끝날 때까지 기다림
        long endTime = System.currentTimeMillis(); // 종료 시간 측정
        long duration = endTime - startTime;

        log.debug("API 응답 시간: {} ms", duration);
    }
    
    @Override
    public List<HashtagDto> getHashtags(Long fileId){
    	return hashtagRepository.findHashtagByFileId(fileId);
    }
}
