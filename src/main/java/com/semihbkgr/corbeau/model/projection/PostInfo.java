package com.semihbkgr.corbeau.model.projection;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostInfo {

    private int id;
    private String title;
    private String createdBy;
    private String updatedBy;
    private long createdAt;
    private long updatedAt;

}