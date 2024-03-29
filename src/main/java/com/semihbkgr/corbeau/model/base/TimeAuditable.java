package com.semihbkgr.corbeau.model.base;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TimeAuditable {

    @CreatedDate
    protected long createdAt;

    @LastModifiedDate
    protected long updatedAt;

}
