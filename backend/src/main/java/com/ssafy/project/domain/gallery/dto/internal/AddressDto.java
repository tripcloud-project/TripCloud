package com.ssafy.project.domain.gallery.dto.internal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddressDto {
    public String sido;
    public String sigungu;
    public String eupmyeondong;
}