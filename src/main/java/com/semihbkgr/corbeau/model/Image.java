package com.semihbkgr.corbeau.model;

import com.semihbkgr.corbeau.model.base.TimeAuditable;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table("images")
public class Image extends TimeAuditable implements Serializable {

    @Id
    private String id;

    private String name;

}
