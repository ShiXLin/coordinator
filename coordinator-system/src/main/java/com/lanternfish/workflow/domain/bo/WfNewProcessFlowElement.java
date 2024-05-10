package com.lanternfish.workflow.domain.bo;

import lombok.Data;
import org.flowable.bpmn.model.*;

import java.util.*;

/**
 * @author Liam
 * @date 2024-4-1
 * @apiNote
 */
@Data
public class WfNewProcessFlowElement {
    /**
     * Gateway&Task&Sequence flow
     */
    private String id;
    private String name;
    private List<FlowableListener> executionListeners = new ArrayList<>();
    private FlowElementsContainer parentContainer;
    /**
     * Gateway&Task
     */
    private boolean asynchronous;
    private boolean asynchronousLeave;
    private boolean notExclusive;
    private List<SequenceFlow> incomingFlows = new ArrayList<>();
    private List<SequenceFlow> outgoingFlows = new ArrayList<>();
    private String defaultFlow;
    /**
     * 跳过表达式(Task&Sequence flow)
     */
    private String skipExpression;

    /**
     * Task
     */
    private boolean forCompensation;
    private MultiInstanceLoopCharacteristics loopCharacteristics;
    private IOSpecification ioSpecification;
    private List<DataAssociation> dataInputAssociations = new ArrayList<>();
    private List<DataAssociation> dataOutputAssociations = new ArrayList<>();
    private List<BoundaryEvent> boundaryEvents = new ArrayList<>();
    private String failedJobRetryTimeCycleValue;
    private List<MapExceptionEntry> mapExceptions = new ArrayList<>();
    private String assignee;
    private String owner;
    private String priority;
    private String formKey;
    private boolean sameDeployment = true;
    private String dueDate;
    private String businessCalendarName;
    private String category;
    private String extensionId;
    private List<String> candidateUsers = new ArrayList<>();
    private List<String> candidateGroups = new ArrayList<>();
    private List<FormProperty> formProperties = new ArrayList<>();
    private List<FlowableListener> taskListeners = new ArrayList<>();
    private String validateFormFields;
    private String taskIdVariableName;
    private Map<String, Set<String>> customUserIdentityLinks = new HashMap<>();
    private Map<String, Set<String>> customGroupIdentityLinks = new HashMap<>();
    private List<CustomProperty> customProperties = new ArrayList<>();

    /**
     * Gateway Type
     */
    private String nodeType;

    /**
     * sequence flow
     */
    private String conditionExpression;
    private String sourceRef;
    private String targetRef;


}
