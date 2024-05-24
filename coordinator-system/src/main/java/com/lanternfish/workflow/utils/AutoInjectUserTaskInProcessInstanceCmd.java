package com.lanternfish.workflow.utils;

import com.lanternfish.flowable.flow.FlowableUtils;
import com.lanternfish.workflow.domain.bo.WfTaskAddSign;
import com.lanternfish.workflow.domain.bo.WfTaskAddSignNode;
import org.apache.commons.lang3.StringUtils;
import org.flowable.bpmn.BpmnAutoLayout;
import org.flowable.bpmn.constants.BpmnXMLConstants;
import org.flowable.bpmn.model.*;
import org.flowable.bpmn.model.Process;
import org.flowable.common.engine.impl.interceptor.Command;
import org.flowable.common.engine.impl.interceptor.CommandContext;
import org.flowable.engine.impl.cmd.AbstractDynamicInjectionCmd;
import org.flowable.engine.impl.dynamic.BaseDynamicSubProcessInjectUtil;
import org.flowable.engine.impl.persistence.entity.DeploymentEntity;
import org.flowable.engine.impl.persistence.entity.ExecutionEntity;
import org.flowable.engine.impl.persistence.entity.ExecutionEntityManager;
import org.flowable.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.flowable.engine.impl.util.CommandContextUtil;

import java.util.*;

/**
 * @author Liam
 * @date 2024-3-26
 * @apiNote
 */
public class AutoInjectUserTaskInProcessInstanceCmd extends AbstractDynamicInjectionCmd implements Command<Void> {
    protected String processInstanceId;
    protected FlowElement currentFlowElement;
    private final WfTaskAddSign wfTaskAddSign;
    private final Map<String, WfTaskAddSignNode> wfTaskAddSignNodeMap;


    public AutoInjectUserTaskInProcessInstanceCmd(String processInstanceId,
                                                  FlowElement currentFlowElement,
                                                  WfTaskAddSign wfTaskAddSign
    ) {
        this.processInstanceId = processInstanceId;
        this.currentFlowElement = currentFlowElement;
        this.wfTaskAddSign = wfTaskAddSign;
        this.wfTaskAddSignNodeMap = new HashMap<>();
        wfTaskAddSign.getAddSignNode().forEach(item -> this.wfTaskAddSignNodeMap.put(item.getNodeId(), item));
    }

    @Override
    public Void execute(CommandContext commandContext) {
        createDerivedProcessDefinitionForProcessInstance(commandContext, processInstanceId);
        return null;
    }

    @Override
    protected void updateBpmnProcess(CommandContext commandContext, Process process, BpmnModel bpmnModel, ProcessDefinitionEntity originalProcessDefinitionEntity, DeploymentEntity newDeploymentEntity) {
        if (currentFlowElement == null) {
            throw new RuntimeException("加签节点有误");
        }
        //加节点与连线
        FlowElement startElement = process.getFlowElement(wfTaskAddSign.getStartNodeId());
        FlowElement endElement = process.getFlowElement(wfTaskAddSign.getEndNodeId());
        if (Objects.isNull(startElement) || Objects.isNull(endElement)) {
            throw new RuntimeException("加签失败");
        }
        //查找加签前后链接
        SequenceFlow oldSequenceFlow = findSequenceFlowFromProcess(process, startElement, endElement);
        if (Objects.isNull(oldSequenceFlow)) {
            throw new RuntimeException("加签失败");
        }
        List<FlowElement> addSignFlowElement = new ArrayList<>();
        //构建所有要添加的节点(首尾节点没有入出)
        for (WfTaskAddSignNode value : wfTaskAddSignNodeMap.values()) {
            if (value.getPreWfTaskAddSignNodeIds() == null || value.getPreWfTaskAddSignNodeIds().isEmpty()) {
                buildAddSign(value, addSignFlowElement);
            }
        }
        if (!addSignFlowElement.isEmpty()) {
            process.removeFlowElement(oldSequenceFlow.getId());
        }
        //为加签的第一个节点设定入口链接
        setFirstFlowElementIncomingFlow(oldSequenceFlow, addSignFlowElement);
        //为加签的最后一个节点设定出口链接
        setLastFlowElementOutGoingFlow(endElement, addSignFlowElement);
        if (!addSignFlowElement.isEmpty()) {
            for (FlowElement flowElement : addSignFlowElement) {
                process.addFlowElement(flowElement);
            }
        }
        new BpmnAutoLayout(bpmnModel).execute();
        BaseDynamicSubProcessInjectUtil.processFlowElements(commandContext, process, bpmnModel, originalProcessDefinitionEntity, newDeploymentEntity);
    }

    @Override
    protected void updateExecutions(CommandContext commandContext, ProcessDefinitionEntity processDefinitionEntity, ExecutionEntity processInstance, List<ExecutionEntity> childExecutions) {
        // 更新流程执行实体
        ExecutionEntityManager executionEntityManager = CommandContextUtil.getExecutionEntityManager(commandContext);
        executionEntityManager.update(processInstance);
    }

    /**
     * 根据开始节点和结束节点获取流程中的连线
     *
     * @param process      流程
     * @param startElement 开始节点
     * @param endElement   结束节点
     * @return 连线
     */
    private SequenceFlow findSequenceFlowFromProcess(Process process, FlowElement startElement, FlowElement endElement) {
        Collection<FlowElement> flowElements = process.getFlowElements();
        for (FlowElement flowElement : flowElements) {
            if (flowElement instanceof SequenceFlow) {
                SequenceFlow tempSequenceFlow = (SequenceFlow) flowElement;
                boolean flag = tempSequenceFlow.getSourceRef().equals(startElement.getId())
                    && tempSequenceFlow.getTargetRef().equals(endElement.getId());
                if (flag) {
                    return tempSequenceFlow;
                }
            }
        }
        return null;
    }

    /**
     * 为加签的最后一个节点设定出口链接
     *
     * @param endElement         加签最后一个节点的后一个节点
     * @param addSignFlowElement 所有加签节点
     */
    private static void setLastFlowElementOutGoingFlow(FlowElement endElement, List<FlowElement> addSignFlowElement) {
        SequenceFlow lastSequenceFlow = new SequenceFlow();
        lastSequenceFlow.setTargetRef(endElement.getId());
        List<SequenceFlow> lastOutGoingFlow = new ArrayList<>();
        for (FlowElement flowElement : addSignFlowElement) {
            if (flowElement instanceof UserTask) {
                UserTask lastUserTask = (UserTask) flowElement;
                if (lastUserTask.getOutgoingFlows() == null || lastUserTask.getOutgoingFlows().isEmpty()) {
                    lastSequenceFlow.setSourceRef(lastUserTask.getId());
                    lastOutGoingFlow.add(lastSequenceFlow);
                    lastUserTask.setOutgoingFlows(lastOutGoingFlow);
                    break;
                }
            }
            if (flowElement instanceof ParallelGateway) {
                ParallelGateway lastGateway = (ParallelGateway) flowElement;
                if (lastGateway.getOutgoingFlows() == null || lastGateway.getOutgoingFlows().isEmpty()) {
                    lastSequenceFlow.setSourceRef(lastGateway.getId());
                    lastOutGoingFlow.add(lastSequenceFlow);
                    lastGateway.setOutgoingFlows(lastOutGoingFlow);
                    break;
                }
            }
        }
        if (StringUtils.isNotBlank(lastSequenceFlow.getTargetRef()) || StringUtils.isNotBlank(lastSequenceFlow.getSourceRef())) {
            List<SequenceFlow> rearIncomingFlow = new ArrayList<>();
            rearIncomingFlow.add(lastSequenceFlow);
            if (endElement instanceof UserTask) {
                UserTask rearLastUserTask = (UserTask) endElement;
                rearLastUserTask.setIncomingFlows(rearIncomingFlow);
            }
            if (endElement instanceof ParallelGateway) {
                ParallelGateway rearLastGateway = (ParallelGateway) endElement;
                rearLastGateway.setIncomingFlows(rearIncomingFlow);
            }
            if (endElement instanceof Event) {
                Event rearLastEvent = (Event) endElement;
                rearLastEvent.setIncomingFlows(rearIncomingFlow);
            }
            addSignFlowElement.add(lastSequenceFlow);
        }
    }

    /**
     * 为加签的第一个节点设定入口链接
     *
     * @param oldSequenceFlow    前一节点老链接
     * @param addSignFlowElement 所有加签元素
     */
    private void setFirstFlowElementIncomingFlow(SequenceFlow oldSequenceFlow, List<FlowElement> addSignFlowElement) {
        oldSequenceFlow.setTargetRef(null);
        List<SequenceFlow> firstIncomingFlow = new ArrayList<>();
        for (FlowElement flowElement : addSignFlowElement) {
            if (flowElement instanceof UserTask) {
                UserTask firstUserTask = (UserTask) flowElement;
                if (firstUserTask.getIncomingFlows() == null || firstUserTask.getIncomingFlows().isEmpty()) {
                    oldSequenceFlow.setTargetRef(firstUserTask.getId());
                    firstIncomingFlow.add(oldSequenceFlow);
                    firstUserTask.setIncomingFlows(firstIncomingFlow);
                    break;
                }
            }
            if (flowElement instanceof ParallelGateway) {
                ParallelGateway firstGateway = (ParallelGateway) flowElement;
                if (firstGateway.getIncomingFlows() == null || firstGateway.getIncomingFlows().isEmpty()) {
                    oldSequenceFlow.setTargetRef(firstGateway.getId());
                    firstIncomingFlow.add(oldSequenceFlow);
                    firstGateway.setIncomingFlows(firstIncomingFlow);
                    break;
                }
            }
        }
        if (StringUtils.isNotBlank(oldSequenceFlow.getTargetRef()) || StringUtils.isNotBlank(oldSequenceFlow.getSourceRef())) {
            addSignFlowElement.add(oldSequenceFlow);
        }
    }

    /**
     * 构建所有要添加的节点(首尾节点没有入出)
     *
     * @param wfTaskAddSignNode  加签节点参数
     * @param addSignFlowElement 所有价钱节点集合
     */
    private void buildAddSign(WfTaskAddSignNode wfTaskAddSignNode, List<FlowElement> addSignFlowElement) {
        //预添加元素
        UserTask userTask = null;
        ParallelGateway parallelGateway = null;
        Map<String, FlowElement> existFlowElement = new HashMap<>();
        addSignFlowElement.forEach(item -> existFlowElement.put(item.getId(), item));
        if (!existFlowElement.containsKey(wfTaskAddSignNode.getNodeId())) {
            if (BpmnXMLConstants.ELEMENT_TASK_USER.equals(wfTaskAddSignNode.getNodeType())) {
                userTask = new UserTask();
                userTask.setId(wfTaskAddSignNode.getNodeId());
                userTask.setAssignee(wfTaskAddSignNode.getAssignee());
                userTask.setName(wfTaskAddSignNode.getName());
                addSignFlowElement.add(userTask);
            } else if (BpmnXMLConstants.ELEMENT_GATEWAY_PARALLEL.equals(wfTaskAddSignNode.getNodeType())) {
                parallelGateway = new ParallelGateway();
                parallelGateway.setId(wfTaskAddSignNode.getNodeId());
                addSignFlowElement.add(parallelGateway);
            }
        } else {
            FlowElement flowElement = existFlowElement.get(wfTaskAddSignNode.getNodeId());
            if (flowElement instanceof UserTask) {
                userTask = (UserTask) flowElement;
            } else if (flowElement instanceof ParallelGateway) {
                parallelGateway = (ParallelGateway) flowElement;
            }
        }
        if (userTask != null) {
            userTask.setOutgoingFlows(buildSequenceFlows(userTask, wfTaskAddSignNode, addSignFlowElement));
            userTask.setIncomingFlows(getIncomingFlows(userTask, wfTaskAddSignNode, addSignFlowElement));
        }
        if (parallelGateway != null) {
            parallelGateway.setOutgoingFlows(buildSequenceFlows(parallelGateway, wfTaskAddSignNode, addSignFlowElement));
            parallelGateway.setIncomingFlows(getIncomingFlows(parallelGateway, wfTaskAddSignNode, addSignFlowElement));
        }
        if (wfTaskAddSignNode.getNextWfTaskAddSignNodeIds() != null && !wfTaskAddSignNode.getNextWfTaskAddSignNodeIds().isEmpty()) {
            for (String taskAddSignNodeId : wfTaskAddSignNode.getNextWfTaskAddSignNodeIds()) {

                buildAddSign(wfTaskAddSignNodeMap.get(taskAddSignNodeId), addSignFlowElement);
            }
        }
    }

    /**
     * 获取入口链接
     *
     * @param wfTaskAddSignNode  当前节点DTO
     * @param addSignFlowElement 需要添加的节点
     * @return 入口链接
     */
    private List<SequenceFlow> getIncomingFlows(FlowElement currentFlowElement, WfTaskAddSignNode wfTaskAddSignNode, List<FlowElement> addSignFlowElement) {
        List<SequenceFlow> incomingSequenceFlow = FlowableUtils.getElementIncomingFlows(currentFlowElement) == null
            ? new ArrayList<>() : FlowableUtils.getElementIncomingFlows(currentFlowElement);

        if (!Objects.nonNull(wfTaskAddSignNode.getPreWfTaskAddSignNodeIds()) || wfTaskAddSignNode.getPreWfTaskAddSignNodeIds().isEmpty()) {
            return incomingSequenceFlow;
        }
        for (FlowElement flowElement : addSignFlowElement) {
            if (flowElement instanceof SequenceFlow) {
                SequenceFlow incomingFlow = (SequenceFlow) flowElement;
                boolean flag = incomingFlow.getTargetRef().equals(wfTaskAddSignNode.getNodeId());
                if (flag) {
                    incomingSequenceFlow.add(incomingFlow);
                }
            }
        }

        return incomingSequenceFlow;
    }

    /**
     * 创建节点与下节点的链接
     *
     * @param currentFlowElement 当前节点
     * @param wfTaskAddSignNode  当前节点
     * @param addSignFlowElement 需要添加的节点
     * @return 出口链接
     */
    private List<SequenceFlow> buildSequenceFlows(FlowElement currentFlowElement, WfTaskAddSignNode wfTaskAddSignNode, List<FlowElement> addSignFlowElement) {
        List<SequenceFlow> outGoingSequenceFlow = FlowableUtils.getElementOutgoingFlows(currentFlowElement) == null
            ? new ArrayList<>() : FlowableUtils.getElementOutgoingFlows(currentFlowElement);
        if (Objects.nonNull(wfTaskAddSignNode.getNextWfTaskAddSignNodeIds()) && !wfTaskAddSignNode.getNextWfTaskAddSignNodeIds().isEmpty()) {
            for (String nextNodeId : wfTaskAddSignNode.getNextWfTaskAddSignNodeIds()) {
                SequenceFlow sequenceFlow = new SequenceFlow();
                sequenceFlow.setSourceRef(wfTaskAddSignNode.getNodeId());
                sequenceFlow.setTargetRef(nextNodeId);
                outGoingSequenceFlow.add(sequenceFlow);
                addSignFlowElement.add(sequenceFlow);
            }
        }
        return outGoingSequenceFlow;
    }
}
