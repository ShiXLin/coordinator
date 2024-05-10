package com.lanternfish.workflow.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serializable;
import java.util.Date;
import java.math.BigDecimal;

import com.lanternfish.common.core.domain.BaseEntity;

/**
 * 流程默认操作对象 wf_default_operate
 *
 * @author Liam
 * @date 2023-11-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("wf_default_operate")
public class WfDefaultOperate extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * 流程默认操作主键
     */
    @TableId(value = "id")
    private Long id;
    /**
     * 操作类型
     */
    private String opeType;
    /**
     * 操作名称
     */
    private String opeName;
    /**
     * 是否启用1-启用0-关闭默认
     */
    private String isEnable;
    /**
     * 排序
     */
    private Long sort;

}
