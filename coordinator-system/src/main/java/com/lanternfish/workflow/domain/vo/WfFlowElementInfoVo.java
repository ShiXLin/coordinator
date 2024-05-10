package com.lanternfish.workflow.domain.vo;

import lombok.Data;

/**
 * @author Liam
 * @date 2024-3-29
 * @apiNote
 */
@Data
public class WfFlowElementInfoVo {
    /**
     * 流程主键
     */
    private String flowElementKey;
    /**
     * 流程节点名称(用户任务节点名称)
     */
    private String flowElementName;
    /**
     * 流程节点类型
     */
    private String flowElementType;
    /**
     * 下一节点
     */
    private WfFlowElementInfoVo nextFlowElementInfo;

}
