package com.lanternfish.workflow.domain.bo;

import com.lanternfish.common.core.validate.AddGroup;
import com.lanternfish.common.core.validate.EditGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.validation.constraints.*;

import java.util.Date;

import com.lanternfish.common.core.domain.BaseEntity;

/**
 * 流程操作规则业务对象 wf_operate_rule
 *
 * @author Liam
 * @date 2023-11-23
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class WfOperateRuleBo extends BaseEntity {

    /**
     * 流程操作主键
     */
    @NotNull(message = "流程操作主键不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 流程配置主表ID
     */
    @NotNull(message = "流程配置主表ID不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long configId;

    /**
     * 操作类型
     */
    @NotBlank(message = "操作类型不能为空", groups = { AddGroup.class, EditGroup.class })
    private String opeType;

    /**
     * 操作名称
     */
    @NotBlank(message = "操作名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String opeName;

    /**
     * 是否启用1-启用0-关闭默认
     */
    @NotBlank(message = "是否启用1-启用0-关闭默认不能为空", groups = { AddGroup.class, EditGroup.class })
    private String isEnable;

    /**
     * 排序
     */
    @NotNull(message = "排序不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long sort;


}
