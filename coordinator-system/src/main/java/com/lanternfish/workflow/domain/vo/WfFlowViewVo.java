package com.lanternfish.workflow.domain.vo;

import lombok.Data;

import java.util.List;

/**
 * @author Liam
 * @date 2024-3-29
 * @apiNote
 */
@Data
public class WfFlowViewVo {
    /**
     * 任务id
     */
    private String taskId;
    /**
     * 流程实例id
     */
    private String processInstanceId;
    /**
     * 流程名称
     */
    private String processName;
    /**
     * 流程定义id
     */
    private String processDefinitionKey;
    /**
     * 流程展示(用于颜色调配)
     */
    private WfViewerVo flowViewer;
    /**
     * 流程展示(流程走向)
     */
    private WfFlowElementInfoVo wfFlowElementInfoVo;
    /**
     * 当前任务节点相关信息
     */
    private List<CurNodeInfoVo> listCurNodeInfo;

}
