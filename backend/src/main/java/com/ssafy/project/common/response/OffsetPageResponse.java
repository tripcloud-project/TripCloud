package com.ssafy.project.common.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OffsetPageResponse<T> {
	private List<T> content;
	private boolean hasNext;
	private Integer size;
	private Integer nextPage;
	private Integer totalCount;
}
