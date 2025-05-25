package com.ssafy.project.domain.member.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StorageResponseDto {
    private String usedStorage;
    private String maxStorage;
}
