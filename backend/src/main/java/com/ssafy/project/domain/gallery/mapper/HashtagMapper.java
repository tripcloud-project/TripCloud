package com.ssafy.project.domain.gallery.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface HashtagMapper {
    Long findFileIdByS3Key(String s3Key);
    Long findHashtagIdByKeyword(String keyword);
    void insertHashtag(String keyword);
    void insertHashtagFile(Long fileId, Long hashtagId);
}

