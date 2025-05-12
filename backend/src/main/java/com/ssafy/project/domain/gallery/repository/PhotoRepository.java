package com.ssafy.project.domain.gallery.repository;

import com.ssafy.project.domain.gallery.dto.internal.DirectoryEntry;
import com.ssafy.project.domain.gallery.dto.internal.FileEntry;

import java.util.List;

public interface PhotoRepository {
    List<DirectoryEntry> findAllSidoByMemberId(Long memberId);
    List<DirectoryEntry> findSigunguBySidoAndMemberId(String province, Long memberId);
    List<FileEntry> findPhotosBySidoAndSigunguAndMemberId(String province, String city, Long memberId);
}
