package com.semihbkgr.corbeau.model.base;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AllAuditable {

    @CreatedBy
    protected String createdBy;

    @LastModifiedBy
    protected String updatedBy;

    @CreatedDate
    protected long createdAt;

    @LastModifiedDate
    protected long updatedAt;

}
