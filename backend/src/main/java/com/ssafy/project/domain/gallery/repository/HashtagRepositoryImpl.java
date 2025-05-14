package com.ssafy.project.domain.gallery.repository;

import com.ssafy.project.domain.gallery.mapper.HashtagMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class HashtagRepositoryImpl implements HashtagRepository{
    private final HashtagMapper hashtagMapper;

    public Long findFileIdByS3Key(String s3Key){
        return hashtagMapper.findFileIdByS3Key(s3Key);
    }
    public Long findHashtagIdByKeyword(String keyword){
        return hashtagMapper.findHashtagIdByKeyword(keyword);
    }
    public void insertHashtag(String keyword){
        hashtagMapper.insertHashtag(keyword);
    }
    public void insertHashtagFile(Long fileId, Long hashtagId){
        hashtagMapper.insertHashtagFile(fileId, hashtagId);
    }
}
