package com.lanternfish.workflow.domain.bo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Liam
 * @date 2024-3-26
 * @apiNote 加签节点类型
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WfTaskAddSignNode {
    /**
     * 前节点
     */
    private List<String> preWfTaskAddSignNodeIds;
    /**
     * 节点id
     */
    private String nodeId;
    /**
     * 节点类型
     * userTask :用户任务类型
     * parallelGateway :并行网关类型 one2more
     */
    private String nodeType;
    /**
     * 节点名称
     */
    private String name;
    /**
     * 节点处理人
     */
    private String assignee;
    /**
     * 后节点
     */
    private List<String> nextWfTaskAddSignNodeIds;
}
