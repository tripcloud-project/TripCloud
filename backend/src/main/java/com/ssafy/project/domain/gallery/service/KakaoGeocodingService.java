package com.ssafy.project.domain.gallery.service;

import com.ssafy.project.domain.gallery.dto.internal.AddressDto;

public interface KakaoGeocodingService {

	AddressDto reverseGeocode(double lat, double lng);

}