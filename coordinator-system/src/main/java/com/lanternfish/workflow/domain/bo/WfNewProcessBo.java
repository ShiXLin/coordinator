package com.lanternfish.workflow.domain.bo;

import lombok.Data;

import java.util.List;

/**
 * @author Liam
 * @date 2024-4-1
 * @apiNote
 */
@Data
public class WfNewProcessBo {
    private String processId;
    private String processName;
    private List<WfNewProcessFlowElement> newProcessUserTaskList;
    private List<WfNewProcessFlowElement> newProcessGatewayList;
    private List<WfNewProcessFlowElement> newProcessSequenceFlowList;
}
