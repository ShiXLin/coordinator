package com.ruoyi.workflow.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serializable;
import java.util.Date;
import java.math.BigDecimal;

import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 钉钉流程对象 wf_dd_flow
 *
 * @author nbacheng
 * @date 2023-11-29
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("wf_dd_flow")
public class WfDdFlow extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * 钉钉流程主键
     */
    @TableId(value = "id")
    private Long id;
    /**
     * 流程名称
     */
    private String name;
    /**
     * 流程JSON数据
     */
    private String flowData;

}
