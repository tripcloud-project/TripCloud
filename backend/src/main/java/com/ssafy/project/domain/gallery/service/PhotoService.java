package com.ssafy.project.domain.gallery.service;

import com.ssafy.project.domain.gallery.dto.response.DirectoryResponseDto;

public interface PhotoService {
    DirectoryResponseDto select(String prefix);
}
