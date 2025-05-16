package com.ssafy.project.domain.gallery.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.project.domain.board.dto.request.ThumbnailRequestDto;
import com.ssafy.project.domain.gallery.dto.internal.DirectoryEntry;
import com.ssafy.project.domain.gallery.dto.internal.FileEntry;
import com.ssafy.project.domain.gallery.dto.request.PhotoDescriptionRequestDto;
import com.ssafy.project.domain.gallery.dto.response.DirectoryResponseDto;
import com.ssafy.project.domain.gallery.dto.response.FileDetailResponseDto;
import com.ssafy.project.domain.gallery.dto.response.ThumbnailResponseDto;
import com.ssafy.project.domain.gallery.exception.InvalidRegionException;
import com.ssafy.project.domain.gallery.exception.SetThumbnailNotAllowedException;
import com.ssafy.project.domain.gallery.exception.UpdateDescriptionNotAllowedException;
import com.ssafy.project.domain.gallery.repository.PhotoRepository;
import com.ssafy.project.infra.s3.S3Service;
import com.ssafy.project.util.SecurityUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PhotoServiceImpl implements PhotoService{
    private final PhotoRepository photoRepository;
    private final S3Service s3Service;

    @Override
    public DirectoryResponseDto select(String prefix){
        Long memberId = SecurityUtil.getCurrentMemberId();

        List<DirectoryEntry> directories = new ArrayList<>();
        List<FileEntry> files = new ArrayList<>();


        if ("/".equals(prefix)) {
            // 시도 리스트 조회
            directories.addAll(photoRepository.findAllSidoByMemberId(memberId));
        } else if (prefix.chars().filter(c -> c == '/').count() == 1) {
            // 시군구 리스트 조회
            String province = prefix.replace("/", "");
            directories.addAll(photoRepository.findSigunguBySidoAndMemberId(province, memberId));
        } else if (prefix.chars().filter(c -> c == '/').count() == 2) {
            // 읍면동 리스트 조회
            String trimmed = prefix.endsWith("/") ? prefix.substring(0, prefix.length() - 1) : prefix;
            String[] parts = trimmed.split("/");
            String province = parts[0];
            String city = parts[1];
            files.addAll(photoRepository.findPhotosBySidoAndSigunguAndMemberId(province, city, memberId));
            for(FileEntry file : files){
                file.setPresignedUrl(s3Service.generatePresignedUrl(file.getS3Key()));
                file.setS3Key(null);
            }
        } else {
            throw new IllegalArgumentException("Invalid prefix format: " + prefix);
        }


        return DirectoryResponseDto.builder()
                .prefix(prefix)
                .directories(directories)
                .files(files)
                .totalSize(
                        directories.stream().mapToLong(d -> d.getSize() != null ? d.getSize() : 0L).sum() +
                                files.stream().mapToLong(f -> f.getSize() != null ? f.getSize() : 0L).sum()
                )
                .build();
    }

    @Transactional
	@Override
	public void updateDescription(Long photoId, PhotoDescriptionRequestDto requestDto) {
    	Long memberId = SecurityUtil.getCurrentMemberId();
    	requestDto.setMemberId(memberId);
    	requestDto.setFileId(photoId);
    	
    	if(!photoRepository.updateDescription(requestDto))
    		throw new UpdateDescriptionNotAllowedException("사진 설명 변경에 실패했습니다.");
	}

    @Transactional
	@Override
	public void setThumbnail(Long photoId, String region) {
		// 잘못된 범위 설정
		if(!region.equals("sido") && !region.equals("sigungu"))
			throw new InvalidRegionException("유효하지 않은 시도/시군구 입니다.");
		
		Long memberId = SecurityUtil.getCurrentMemberId();
		FileDetailResponseDto fileDetail = photoRepository.findPhotoByPhotoId(memberId, photoId);
				
		if(fileDetail.getSido() == null) 
			throw new InvalidRegionException("유효하지 않은 시도/시군구 입니다.");
		
		ThumbnailRequestDto.ThumbnailRequestDtoBuilder builder = ThumbnailRequestDto.builder()
		        .memberId(memberId)
		        .photoId(photoId)
		        .sido(fileDetail.getSido());

		if (region.equals("sigungu")) {
		    builder.sigungu(fileDetail.getSigungu());
		} else {
			builder.sigungu("");
		}

		ThumbnailRequestDto requestDto = builder.build();
		
		if(!photoRepository.setThumbnail(requestDto))
			throw new SetThumbnailNotAllowedException("대표사진 설정에 실패했습니다.");
		
	}

	@Override
	public List<ThumbnailResponseDto> getThumbnails(String sido) {
		Long memberId = SecurityUtil.getCurrentMemberId();
		
		List<ThumbnailResponseDto> thumbnailList = null;
		if(sido == null)
			thumbnailList = photoRepository.findSidoThumbnails(memberId);
		else
			thumbnailList = photoRepository.findSigunguThumbnails(memberId, sido);
		
		for(ThumbnailResponseDto thumbnail : thumbnailList) {
			thumbnail.setPresignedURL(s3Service.generatePresignedUrl(thumbnail.getPresignedURL()));
		}
		
		return thumbnailList;	
	}

}
