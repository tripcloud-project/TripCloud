package com.ssafy.project.domain.board.service;

import org.springframework.web.multipart.MultipartFile;

public interface BoardService {
    String uploadImage(MultipartFile image);
}
