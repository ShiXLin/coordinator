package com.lanternfish.workflow.domain.bo;

import com.lanternfish.common.core.validate.AddGroup;
import com.lanternfish.common.core.validate.EditGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.validation.constraints.*;

import java.util.Date;

import com.lanternfish.common.core.domain.BaseEntity;

/**
 * 流程配置主业务对象 wf_flow_config
 *
 * @author Liam
 * @date 2023-11-19
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class WfFlowConfigBo extends BaseEntity {

    /**
     * 流程配置主表主键
     */
    @NotNull(message = "流程配置主表主键不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 流程模型ID
     */
    @NotBlank(message = "流程模型ID不能为空", groups = { AddGroup.class, EditGroup.class })
    private String modelId;

    /**
     * 节点Key
     */
    @NotBlank(message = "节点Key不能为空", groups = { AddGroup.class, EditGroup.class })
    private String nodeKey;

    /**
     * 节点名称
     */
    @NotBlank(message = "节点名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String nodeName;

    /**
     * 表单Key
     */
    @NotBlank(message = "表单Key不能为空", groups = { AddGroup.class, EditGroup.class })
    private String formKey;

    /**
     * 应用类型
     */
    @NotBlank(message = "应用类型不能为空", groups = { AddGroup.class, EditGroup.class })
    private String appType;


}
