package com.ssafy.project.domain.gallery.service;

import com.ssafy.project.domain.gallery.dto.internal.ByteFileDto;
import com.ssafy.project.domain.gallery.dto.internal.HashtagDto;

import java.util.List;

public interface HashtagService {
    void parseHashtag(List<ByteFileDto> hashtagParseList);
    List<HashtagDto> getHashtags(Long fileId);
}
