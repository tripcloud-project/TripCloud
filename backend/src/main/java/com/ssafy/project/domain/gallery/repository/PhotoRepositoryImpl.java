package com.ssafy.project.domain.gallery.repository;

import com.ssafy.project.domain.gallery.dto.internal.DirectoryEntry;
import com.ssafy.project.domain.gallery.dto.internal.FileEntry;
import com.ssafy.project.domain.gallery.mapper.PhotoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class PhotoRepositoryImpl implements PhotoRepository{
    private final PhotoMapper photoMapper;

    public List<DirectoryEntry> findAllSidoByMemberId(Long memberId){
        return photoMapper.findAllSidoByMemberId(memberId);
    }
    public List<DirectoryEntry> findSigunguBySidoAndMemberId(String province, Long memberId){
        return photoMapper.findSigunguBySidoAndMemberId( province, memberId);
    }
    public List<FileEntry> findPhotosBySidoAndSigunguAndMemberId(String province, String city, Long memberId){
        return photoMapper.findPhotosBySidoAndSigunguAndMemberId(province, city, memberId);
    }
}
