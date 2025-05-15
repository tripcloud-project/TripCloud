package com.ssafy.project.domain.gallery.repository;

import java.util.List;

public interface HashtagRepository {
    Long findFileIdByS3Key(String s3Key);
    Long findHashtagIdByKeyword(String keyword);
    void insertHashtag(String keyword);
    void insertHashtagFile(Long fileId, Long hashtagId);
}
