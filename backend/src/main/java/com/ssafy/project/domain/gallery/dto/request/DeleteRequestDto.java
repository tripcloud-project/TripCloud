package com.ssafy.project.domain.gallery.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class DeleteRequestDto {
    private List<String> prefixList;
    private List<Long> fileIdList;
}
