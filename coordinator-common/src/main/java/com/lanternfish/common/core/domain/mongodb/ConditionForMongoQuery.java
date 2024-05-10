package com.lanternfish.common.core.domain.mongodb;

import com.lanternfish.common.constant.MongoConstants;
import com.lanternfish.common.exception.ServiceException;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.data.mongodb.core.query.Criteria;

/**
 * @author Liam
 * @date 2024-5-9
 * @apiNote
 */
@Data
public class ConditionForMongoQuery {
    @Schema(description = "字段名")
    private String fieldId;
    @Schema(description = "字段类型")
    private String fieldType;
    @Schema(description = "操作关系")
    private String operator;
    @Schema(description = "值")
    private Object[] value;

    /**
     * 构造查询条件
     *
     * @param criteria 查询条件
     */
    public void build(Criteria criteria) {
        if (value == null || value.length == 0) {
            throw new ServiceException("["+fieldId+"]字段，查询值不能为空");
        }
        switch (fieldType) {
            case MongoConstants.FiledType.STRING:
                buildCriteriaForString(criteria);
                break;
            case MongoConstants.FiledType.NUMBER:
                buildCriteriaForNumber(criteria);
                break;
            case MongoConstants.FiledType.DATE:
                buildCriteriaForDate(criteria);
                break;
            default:
                throw new ServiceException("["+fieldId+"]字段，不支持的查询字段类型");
        }
    }

    /**
     * 构造日期类型的查询条件
     *
     * @param criteria 查询条件
     */
    private void buildCriteriaForDate(Criteria criteria) {
        switch (operator) {
            case MongoConstants.OperatorType.EQUAL:
                criteria.and(fieldId).is(value[0]);
                break;
            case MongoConstants.OperatorType.NOT_EQUAL:
                criteria.and(fieldId).ne(value[0]);
                break;
            case MongoConstants.OperatorType.GT:
                criteria.and(fieldId).gt(value[0]);
                break;
            case MongoConstants.OperatorType.GTE:
                criteria.and(fieldId).gte(value[0]);
                break;
            case MongoConstants.OperatorType.LT:
                criteria.and(fieldId).lt(value[0]);
                break;
            case MongoConstants.OperatorType.LTE:
                criteria.and(fieldId).lte(value[0]);
                break;
            case MongoConstants.OperatorType.BETWEEN:
                criteria.and(fieldId).gte(value[0]).lte(value[1]);
                break;
            default:
                throw new ServiceException("不支持的查询操作");
        }
    }

    /**
     * 构造数字类型的查询条件
     *
     * @param criteria 查询条件
     */
    private void buildCriteriaForNumber(Criteria criteria) {
        switch (operator) {
            case MongoConstants.OperatorType.EQUAL:
                criteria.and(fieldId).is(value[0]);
                break;
            case MongoConstants.OperatorType.NOT_EQUAL:
                criteria.and(fieldId).ne(value[0]);
                break;
            case MongoConstants.OperatorType.GT:
                criteria.and(fieldId).gt(value[0]);
                break;
            case MongoConstants.OperatorType.GTE:
                criteria.and(fieldId).gte(value[0]);
                break;
            case MongoConstants.OperatorType.LT:
                criteria.and(fieldId).lt(value[0]);
                break;
            case MongoConstants.OperatorType.LTE:
                criteria.and(fieldId).lte(value[0]);
                break;
            case MongoConstants.OperatorType.BETWEEN:
                criteria.and(fieldId).gte(value[0]).lte(value[1]);
                break;
            case MongoConstants.OperatorType.IN:
                criteria.and(fieldId).in(value);
                break;
            default:
                throw new ServiceException("不支持的查询操作");
        }
    }

    /**
     * 构造字符串类型的查询条件
     *
     * @param criteria 查询条件
     */
    private void buildCriteriaForString(Criteria criteria) {
        switch (operator) {
            case MongoConstants.OperatorType.EQUAL:
                criteria.and(fieldId).is(value[0]);
                break;
            case MongoConstants.OperatorType.NOT_EQUAL:
                criteria.and(fieldId).ne(value[0]);
                break;
            case MongoConstants.OperatorType.LIKE:
                criteria.and(fieldId).regex(value[0].toString());
                break;
            case MongoConstants.OperatorType.IN:
                criteria.and(fieldId).in(value);
                break;
            default:
                throw new ServiceException("不支持的查询操作");
        }
    }
}
