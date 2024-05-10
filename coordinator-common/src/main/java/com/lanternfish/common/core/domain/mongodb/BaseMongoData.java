package com.lanternfish.common.core.domain.mongodb;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

import java.time.LocalDateTime;

/**
 * @author Liam
 * @date 2024-4-25
 * @apiNote
 */
@Data
public class BaseMongoData {
    /**
     * 主键id
     */
    @Id
    private ObjectId id;

    /**
     * 创建人id
     */
    private Long createUser;

    /**
     * 更新人id
     */
    private Long updateUser;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 是否删除
     */
    @Indexed
    private Boolean isDeleted;

}
