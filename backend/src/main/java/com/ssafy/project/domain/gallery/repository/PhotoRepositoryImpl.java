package com.ssafy.project.domain.gallery.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ssafy.project.domain.board.dto.request.ThumbnailRequestDto;
import com.ssafy.project.domain.gallery.dto.internal.DirectoryEntry;
import com.ssafy.project.domain.gallery.dto.internal.FileEntry;
import com.ssafy.project.domain.gallery.dto.internal.RegionDto;
import com.ssafy.project.domain.gallery.dto.request.PhotoDescriptionRequestDto;
import com.ssafy.project.domain.gallery.dto.response.FileDetailResponseDto;
import com.ssafy.project.domain.gallery.dto.response.ThumbnailResponseDto;
import com.ssafy.project.domain.gallery.mapper.PhotoMapper;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class PhotoRepositoryImpl implements PhotoRepository{
    private final PhotoMapper photoMapper;

    @Override
    public List<DirectoryEntry> findAllSidoByMemberId(Long memberId){
        return photoMapper.findAllSidoByMemberId(memberId);
    }
    

    @Override
    public List<DirectoryEntry> findSigunguBySidoAndMemberId(String province, Long memberId){
        return photoMapper.findSigunguBySidoAndMemberId( province, memberId);
    }
    
    @Override
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
	public List<String> findDistinctSidoByMemberId(Long memberId){
		return photoMapper.findDistinctSidoByMemberId(memberId);
	}
	
	@Override
	public List<String> findDistinctSigunguBySidoAndMemberId(String sido, Long memberId){
		return photoMapper.findDistinctSigunguBySidoAndMemberId(sido, memberId);
	}
	
	public List<ThumbnailResponseDto> findSidoThumbnails(Long memberId) {
		return photoMapper.selectSidoThumbnailsByMemberId(memberId);
	}

	@Override
	public List<ThumbnailResponseDto> findSigunguThumbnails(Long memberId, String sido) {
		return photoMapper.selectSidoThumbnailsByMemberIdAndSido(memberId, sido);
	}
}
