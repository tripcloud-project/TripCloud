package com.ssafy.project.domain.gallery.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RenameRequest {
    private String oldKey;
    private String newKey;
}