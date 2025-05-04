package com.ssafy.project.util;

import java.io.InputStream;
import org.springframework.web.multipart.MultipartFile;
import com.drew.imaging.ImageMetadataReader;
import com.drew.lang.GeoLocation;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.GpsDirectory;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ExifUtil {
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
	    	log.error("❌ GPS 추출 실패: {}", e.getMessage(), e);
	    }

	    return null;
	}


}
