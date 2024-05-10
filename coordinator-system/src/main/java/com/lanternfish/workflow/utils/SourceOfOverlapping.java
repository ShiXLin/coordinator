package com.lanternfish.workflow.utils;

import com.lanternfish.flowable.flow.FlowableUtils;
import lombok.Data;
import org.flowable.bpmn.model.FlowElement;
import org.flowable.bpmn.model.Process;
import org.flowable.bpmn.model.SequenceFlow;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * source of overlapping nodes
 */
@Data
public class SourceOfOverlapping {
    private Process process;
    private String sourceNodeId;
    private String endNodeId;
    private Set<String> toUpNodeSet;
    private Set<String> toDownNodeSet;
    private final String START_TYPE = "Start";
    private final String END_TYPE = "END";

    public void build(String nodeId1, String nodeId2, Process process) {
        this.process = process;
        FlowElement flowElement1 = process.getFlowElement(nodeId1);
        FlowElement flowElement2 = process.getFlowElement(nodeId2);
        this.sourceNodeId = findStartOrEnd(flowElement1, flowElement2, START_TYPE, process);
        this.endNodeId = findStartOrEnd(flowElement1, flowElement2, END_TYPE, process);
        findUpAndDownNodes(this);
    }

    private void findUpAndDownNodes(SourceOfOverlapping sourceOfOverlapping) {
        String startNodeId = sourceOfOverlapping.getSourceNodeId();
        String endNodeId = sourceOfOverlapping.getEndNodeId();
        FlowElement startNode = process.getFlowElement(startNodeId);
        List<SequenceFlow> startOutgoing = FlowableUtils.getElementOutgoingFlows(startNode);
        Set<String> toUpSet = startOutgoing.stream().limit(startOutgoing.size() / 2).map(SequenceFlow::getTargetRef).collect(Collectors.toSet());
        Set<String> toDownSet = startOutgoing.stream().skip((startOutgoing.size()+1) / 2).map(SequenceFlow::getTargetRef).collect(Collectors.toSet());
        findAllNode(toUpSet, endNodeId, new ArrayList<>(toUpSet), sourceOfOverlapping.process);
        findAllNode(toDownSet, endNodeId, new ArrayList<>(toDownSet), sourceOfOverlapping.process);
        sourceOfOverlapping.setToUpNodeSet(toUpSet);
        sourceOfOverlapping.setToDownNodeSet(toDownSet);
    }

    /**
     * find all node between start node and end node
     *
     * @param toMoveNodes to move nodes
     * @param endNodeId   end node id
     * @param nextNodes   next nodes
     * @param process     process model
     */
    private void findAllNode(Set<String> toMoveNodes, String endNodeId, List<String> nextNodes, Process process) {
        List<String> newNextNodes = new ArrayList<>();
        for (String nodeId : nextNodes) {
            FlowElement flowElement = process.getFlowElement(nodeId);
            List<SequenceFlow> outgoingFlows = FlowableUtils.getElementOutgoingFlows(flowElement);
            boolean flag = false;
            for (SequenceFlow outgoingFlow : outgoingFlows) {
                if (outgoingFlow.getTargetRef().equals(endNodeId)) {
                    flag = true;
                    break;
                } else {
                    newNextNodes.add(outgoingFlow.getTargetRef());
                }
            }
            if (flag) {
                return;
            } else {
                toMoveNodes.addAll(newNextNodes);
                findAllNode(toMoveNodes, endNodeId, newNextNodes, process);
            }
        }
    }

    /**
     * find two different line nodes common source and end
     *
     * @param flowElement1 node
     * @param flowElement2 the other node
     * @param type         the type of discovery
     * @param process      process model
     * @return the common source or end
     */
    private String findStartOrEnd(FlowElement flowElement1, FlowElement flowElement2, String type, Process process) {
        Set<String> sequenceFlowSet1 = new HashSet<>();
        Set<String> sequenceFlowSet2 = new HashSet<>();
        List<SequenceFlow> sequenceFlows1;
        List<SequenceFlow> sequenceFlows2;
        if ("Start".equals(type)) {
            sequenceFlows1 = FlowableUtils.getElementIncomingFlows(flowElement1);
            sequenceFlows2 = FlowableUtils.getElementIncomingFlows(flowElement2);
        } else {
            sequenceFlows1 = FlowableUtils.getElementOutgoingFlows(flowElement1);
            sequenceFlows2 = FlowableUtils.getElementOutgoingFlows(flowElement2);
        }
        while (true) {
            sequenceFlows1 = findDeepSequenceFlowList(sequenceFlows1, type, process);
            sequenceFlows2 = findDeepSequenceFlowList(sequenceFlows2, type, process);
            sequenceFlowSet1.addAll(sequenceFlows1.stream().map(SequenceFlow::getTargetRef).collect(Collectors.toSet()));
            sequenceFlowSet2.addAll(sequenceFlows2.stream().map(SequenceFlow::getTargetRef).collect(Collectors.toSet()));
            if (stopCondition(sequenceFlowSet1, sequenceFlowSet2) != null) {
                return stopCondition(sequenceFlowSet1, sequenceFlowSet2);
            }
        }
    }

    /**
     * find sequence flow of the source or target
     *
     * @param sequenceFlows current sequence flows
     * @param type          the type of discovery
     * @param process       process model
     * @return the source or target list
     */
    private List<SequenceFlow> findDeepSequenceFlowList(List<SequenceFlow> sequenceFlows, String type, Process process) {
        List<SequenceFlow> flows = new ArrayList<>();
        if ("Start".equals(type)) {
            for (SequenceFlow flow : sequenceFlows) {
                flows.addAll(FlowableUtils.getElementIncomingFlows(process.getFlowElement(flow.getSourceRef())));
            }
        } else {
            for (SequenceFlow flow : sequenceFlows) {
                flows.addAll(FlowableUtils.getElementOutgoingFlows(process.getFlowElement(flow.getTargetRef())));
            }
        }
        return flows;
    }

    private String stopCondition(Set<String> sequenceFlows1, Set<String> sequenceFlows2) {
        for (String sequenceFlowNodeId : sequenceFlows1) {
            if (sequenceFlows2.contains(sequenceFlowNodeId)) {
                return sequenceFlowNodeId;
            }
        }
        return null;
    }
}
