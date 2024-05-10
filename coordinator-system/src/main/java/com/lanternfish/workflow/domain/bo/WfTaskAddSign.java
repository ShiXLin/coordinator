package com.lanternfish.workflow.domain.bo;

import lombok.Data;

import java.util.List;

/**
 * @author Liam
 * @date 2024-3-22
 * @apiNote
 */
@Data
public class WfTaskAddSign extends WfTaskBo{

    /**
     * 加签节点
     */
    private List<WfTaskAddSignNode> addSignNode;

    /**
     * 加签开始节点
     */
    private String startNodeId;

    /**
     * 加签结束节点
     */
    private String endNodeId;
}
