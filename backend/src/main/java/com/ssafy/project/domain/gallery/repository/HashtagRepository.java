package com.ssafy.project.domain.gallery.repository;

import java.util.List;

import com.ssafy.project.domain.gallery.dto.internal.HashtagDto;

public interface HashtagRepository {
    Long findFileIdByS3Key(String s3Key);
    Long findHashtagIdByKeyword(String keyword);
    void insertHashtag(String keyword);
    void insertHashtagFile(Long fileId, Long hashtagId);
    List<HashtagDto> findHashtagByFildId(Long fileId);
}
