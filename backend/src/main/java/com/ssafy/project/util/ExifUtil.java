package com.ssafy.project.util;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

import org.springframework.web.multipart.MultipartFile;
import com.drew.imaging.ImageMetadataReader;
import com.drew.lang.GeoLocation;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import com.drew.metadata.exif.GpsDirectory;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ExifUtil {
	// [위경도 데이터 추출]
	public static GeoLocation extractGps(MultipartFile file) {
	    String filename = file.getOriginalFilename();
	    log.debug("📸 GPS 추출 시작: {}", filename);

	    if (filename == null || filename.isBlank()) {
	        return null;
	    }

	    try (InputStream input = file.getInputStream()) {
	        Metadata metadata = ImageMetadataReader.readMetadata(input);
	        GpsDirectory gps = metadata.getFirstDirectoryOfType(GpsDirectory.class);

	        if (gps != null) {
	            GeoLocation geo = gps.getGeoLocation();
	            if (geo != null && !(geo.getLatitude() == 0.0 && geo.getLongitude() == 0.0)) {
	            	log.debug("📍 위도: {}, 경도: {}", geo.getLatitude(), geo.getLongitude());
	                return geo;
	            } else {
	            	log.info("ℹ️ GPS 정보는 있으나 좌표가 (0.0, 0.0) → 무시");
	            }
	        } else {
	        	log.info("ℹ️ GPS 메타데이터 없음");
	        }

	    } catch (Exception e) {
			return null;
	    }

	    return null;
	}

	// [촬영일 추출]
	public static LocalDateTime extractDateTaken(MultipartFile file) {
		try (InputStream input = file.getInputStream()) {
			Metadata metadata = ImageMetadataReader.readMetadata(input);
			ExifSubIFDDirectory directory = metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);
			if (directory != null) {
				Date date = directory.getDate(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL);
				if (date != null) {
					// 시스템 기본 시간대 기준으로 LocalDateTime 변환
					return LocalDateTime.ofInstant(date.toInstant(), ZoneOffset.UTC);
				}
			}
		} catch (Exception e) {
			return null;
		}
		return null;
	}

}
