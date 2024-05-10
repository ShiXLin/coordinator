package com.lanternfish.workflow.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serializable;
import java.util.Date;
import java.math.BigDecimal;

import com.lanternfish.common.core.domain.BaseEntity;

/**
 * 流程自定义业务规则对象 wf_custom_rule
 *
 * @author Liam
 * @date 2023-11-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("wf_custom_rule")
public class WfCustomRule extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * 业务规则主键
     */
    @TableId(value = "id")
    private Long id;
    /**
     * 流程配置主表ID
     */
    private Long configId;
    /**
     * 字段编码
     */
    private String colCode;
    /**
     * 字段名称
     */
    private String colName;
    /**
     * java类型
     */
    private String javaType;
    /**
     * java字段名
     */
    private String javaField;
    /**
     * 属性0-隐藏1-只读默认2-可编辑
     */
    private String attribute;
    /**
     * 排序
     */
    private Long sort;

}
