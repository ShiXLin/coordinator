package com.lanternfish.workflow.utils.oop;

import com.lanternfish.common.utils.BeanCopyUtils;
import com.lanternfish.flowable.flow.FlowableUtils;
import lombok.Data;
import org.flowable.bpmn.model.Process;
import org.flowable.bpmn.model.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Liam
 * @date 2024-4-15
 * @apiNote
 */
@Data
public class NodeGraphicDef {
    /**
     * node id
     */
    private String id;
    /**
     * node type
     */
    private String type;
    /**
     * x
     */
    private Double x;
    /**
     * y
     */
    private Double y;
    /**
     * node width
     */
    private Double width;
    /**
     * node height
     */
    private Double height;
    /**
     * next node id list
     */
    private List<String> nextNodeIds;
    /**
     * incoming node id list
     */
    private List<String> incomingNodeIds;


    /**
     * build a NodeGraphicDef object according to the flowElement
     *
     * @param flowElement the flowElement
     * @return the NodeGraphicDef object
     */
    public static NodeGraphicDef builder(FlowElement flowElement) {
        double taskWidth = 100;
        double taskHeight = 80;
        double EVENT_HEIGHT = 30.0;
        double EVENT_WIDTH = 30.0;
        double GATEWAY_HEIGHT = 40.0;
        double GATEWAY_WIDTH = 40.0;
        NodeGraphicDef nodeGraphicDef = new NodeGraphicDef();
        BeanCopyUtils.copy(flowElement, nodeGraphicDef);
        if (flowElement instanceof Task) {
            nodeGraphicDef.setWidth(taskWidth);
            nodeGraphicDef.setHeight(taskHeight);
        }
        if (flowElement instanceof Event) {
            nodeGraphicDef.setWidth(EVENT_WIDTH);
            nodeGraphicDef.setHeight(EVENT_HEIGHT);
        }
        if (flowElement instanceof Gateway) {
            nodeGraphicDef.setWidth(GATEWAY_WIDTH);
            nodeGraphicDef.setHeight(GATEWAY_HEIGHT);
        }
        FlowableUtils.getElementIncomingFlows(flowElement).stream().map(FlowElement::getId).forEach(nodeGraphicDef.getIncomingNodeIds()::add);
        FlowableUtils.getElementOutgoingFlows(flowElement).stream().map(FlowElement::getId).forEach(nodeGraphicDef.getNextNodeIds()::add);

        return nodeGraphicDef;
    }

    /**
     * build a list of NodeGraphicDef objects according to the process
     *
     * @param process the process
     * @return the list of NodeGraphicDef objects
     */
    public static List<NodeGraphicDef> buildList(Process process) {
        return process.getFlowElements().stream().filter(flowElement -> flowElement instanceof FlowNode).map(NodeGraphicDef::builder).collect(Collectors.toList());
    }
}
