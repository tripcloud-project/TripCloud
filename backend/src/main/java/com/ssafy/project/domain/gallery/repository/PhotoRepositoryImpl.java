package com.ssafy.project.domain.gallery.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ssafy.project.domain.board.dto.request.ThumbnailRequestDto;
import com.ssafy.project.domain.gallery.dto.internal.DirectoryEntry;
import com.ssafy.project.domain.gallery.dto.internal.FileEntry;
import com.ssafy.project.domain.gallery.dto.request.PhotoDescriptionRequestDto;
import com.ssafy.project.domain.gallery.dto.response.FileDetailResponseDto;
import com.ssafy.project.domain.gallery.dto.response.ThumbnailResponseDto;
import com.ssafy.project.domain.gallery.mapper.PhotoMapper;

import lombok.RequiredArgsConstructor;

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
    
	@Override
	public boolean updateDescription(PhotoDescriptionRequestDto requestDto) {
		return photoMapper.updateDescription(requestDto) == 1;
	}
	
	@Override
	public FileDetailResponseDto findPhotoByPhotoId(Long memberId, Long photoId) {
		return photoMapper.selectByMemberIdAndPhotoId(memberId, photoId);
	}
	
	@Override
	public boolean setThumbnail(ThumbnailRequestDto requestDto) {
		return photoMapper.updateThumbnail(requestDto) >= 1;
	}
	
	@Override
	public List<ThumbnailResponseDto> findSidoThumbnails(Long memberId) {
		return photoMapper.selectSidoThumbnailsByMemberId(memberId);
	}
}
