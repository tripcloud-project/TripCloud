package com.ssafy.project.common.response;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PageResponse<T> {
	private List<T> content;
	private boolean hasNext;
	private Integer size;
	private LocalDateTime nextCursor;
}
