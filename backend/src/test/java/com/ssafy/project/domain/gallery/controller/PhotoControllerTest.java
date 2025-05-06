package com.ssafy.project.domain.gallery.controller;


import com.ssafy.project.domain.gallery.dto.internal.UploadDto;
import com.ssafy.project.domain.gallery.service.MinioService;
import com.ssafy.project.domain.gallery.service.PhotoService;
import com.ssafy.project.exception.UploadFailException;
import com.ssafy.project.util.JWTUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PhotoController.class)
@AutoConfigureMockMvc(addFilters = false)
public class PhotoControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PhotoService photoService;

    @MockitoBean
    private MinioService minioService;

    @MockitoBean
    private JWTUtil jwtUtil;

    @Test
    @DisplayName("사진 업로드 성공")
    @WithMockUser(username = "test@naver.com")  // 인증 우회
    void testUploadPhoto() throws Exception{
        MockMultipartFile file = new MockMultipartFile(
                "files", "test.jpg", MediaType.IMAGE_JPEG_VALUE, "test image".getBytes());

        String rawPrefix = "여행폴더/";
        String expectedPrefix = "test@naver.com/여행폴더/"; // ✔️ 실제 사용될 prefix

        UploadDto uploadDto = new UploadDto();
        when(minioService.uploadFiles(anyList(), eq(expectedPrefix))).thenReturn(Collections.singletonList(uploadDto));

        mockMvc.perform(multipart("/api/v1/gallery/upload")
                        .file(file)
                        .param("prefix", rawPrefix))
                .andDo(print())
                .andExpect(status().isOk());

        verify(minioService).uploadFiles(anyList(), eq(expectedPrefix));
        verify(photoService).uploadPhotos(anyList());
    }

    @Test
    @DisplayName("사진 업로드 실패 - minioService에서 예외 발생")
    @WithMockUser(username = "test@naver.com")
    void testUploadFileFail() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "files", "test.jpg", MediaType.IMAGE_JPEG_VALUE, "test image".getBytes());

        String rawPrefix = "여행폴더/";
        String expectedPrefix = "test@naver.com/여행폴더/";

        // 업로드 실패 예외 발생 설정
        when(minioService.uploadFiles(anyList(), eq(expectedPrefix)))
                .thenThrow(new UploadFailException());

        mockMvc.perform(multipart("/api/v1/gallery/upload")
                        .file(file)
                        .param("prefix", rawPrefix))
                .andDo(print())
                .andExpect(status().isInternalServerError()); // 500 상태 코드 확인

        // 실패 시 verify 생략 가능 (예외 발생하므로 이후 로직은 호출 안 됨)
    }

    @Test
    @DisplayName("사진 업로드 실패 - photoService에서 예외 발생")
    @WithMockUser(username = "test@naver.com")
    void testUploadPhotoFailInPhotoService() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "files", "test.jpg", MediaType.IMAGE_JPEG_VALUE, "test image".getBytes());

        String rawPrefix = "기타사진/";
        String expectedPrefix = "test@naver.com/기타사진/";

        UploadDto uploadDto = new UploadDto();
        when(minioService.uploadFiles(anyList(), eq(expectedPrefix)))
                .thenReturn(Collections.singletonList(uploadDto));

        // photoService에서 예외 발생하도록 설정
        doThrow(new UploadFailException())
                .when(photoService).uploadPhotos(anyList());

        mockMvc.perform(multipart("/api/v1/gallery/upload")
                        .file(file)
                        .param("prefix", rawPrefix))
                .andDo(print())
                .andExpect(status().isInternalServerError()); // 500 상태코드 확인
    }

}
