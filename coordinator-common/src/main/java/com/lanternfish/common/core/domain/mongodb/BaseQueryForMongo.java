package com.lanternfish.common.core.domain.mongodb;

import cn.hutool.core.util.ObjectUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.data.mongodb.core.query.Criteria;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author Liam
 * @date 2024-4-24
 * @apiNote
 */
@Data
public class BaseQueryForMongo {

    @Schema(description = "页码", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer pageNum;

    @Schema(description = "每页数量", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer pageSize;

    @Schema(description = "排序字段", requiredMode = Schema.RequiredMode.REQUIRED)
    private String[] sortColumn;

    @Schema(description = "排序方式", requiredMode = Schema.RequiredMode.REQUIRED, example = "asc/desc")
    private String sortOrder;

    @Schema(description = "查询条件")
    private List<ConditionForMongoQuery> queryCondition;

    /**
     * 构建查询条件
     *
     * @param criteria 查询条件
     */
    public void build(Criteria criteria) {
        if (ObjectUtil.isNotNull(queryCondition) && !queryCondition.isEmpty()) {
            queryCondition.forEach(condition -> {
                condition.build(criteria);
            });
        }
    }


}
