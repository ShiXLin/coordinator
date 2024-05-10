package com.lanternfish.workflow.utils;

import com.lanternfish.flowable.flow.FlowableUtils;
import org.flowable.bpmn.model.Process;
import org.flowable.bpmn.model.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Liam
 * @date 2024-4-2
 * @apiNote
 */
public class AutoDrawFlow {
    /**
     * event size
     */
    private final static double EVENT_HEIGHT = 30.0;
    private final static double EVENT_WIDTH = 30.0;
    /**
     * task size
     */
    private final static double USER_TASK_HEIGHT = 60.0;
    private final static double USER_TASK_WIDTH = 100.0;
    /**
     * gateway size
     */
    private final static double GATEWAY_HEIGHT = 40.0;
    private final static double GATEWAY_WIDTH = 40.0;
    /**
     * start event location
     */
    private final static double START_EVENT_X = 0;
    private final static double START_EVENT_Y = 150.0;

    /**
     * event & task & gateway distance
     */
    private final static double DISTANCE_X = 50.0;
    private final static double DISTANCE_Y = 200.0;
    private final String START_TYPE = "Start";
    private final String END_TYPE = "END";

    /**
     * location map
     */
    private Map<String, GraphicInfo> locationMap;

    /**
     * bpmn object
     */
    private final BpmnModel bpmnModel;

    /**
     * the nodes already laid out
     */
    private final Set<String> layoutedNodeSet = new HashSet<>();


    /**
     * current process
     */
    private Process process;


    public AutoDrawFlow(BpmnModel bpmnModel) {
        this.bpmnModel = bpmnModel;
    }

    public void execute() {
        // Reset any previous DI information
        bpmnModel.getLocationMap().clear();
        bpmnModel.getFlowLocationMap().clear();

        // Generate DI for each process
        for (Process process : bpmnModel.getProcesses()) {
            this.process = process;
            layout();
        }
    }

    /**
     * layout the process
     */
    private void layout() {
        locationMap = bpmnModel.getLocationMap();
        graphicNodeWidthAndHeight();
        layoutNodes();
        graphicLappingNodes();
        layoutSequenceFlow();
    }


    /**
     * layout sequence flow
     */
    private void layoutSequenceFlow() {
        List<FlowNode> sequenceFlowList = process.getFlowElements().stream().filter(flowElement -> !(flowElement instanceof SequenceFlow)).map(flowElement -> (FlowNode) flowElement).collect(Collectors.toList());
        for (FlowNode flowNode : sequenceFlowList) {
            buildWaypoints(flowNode);
        }
    }

    /**
     * create sequence flow waypoint
     *
     * @param flowNode current flow node
     */
    private void buildWaypoints(FlowNode flowNode) {
        List<SequenceFlow> outgoingFlows = flowNode.getOutgoingFlows();
        if (outgoingFlows.size() == 1) {
            FlowElement toFlowElement = process.getFlowElement(outgoingFlows.get(0).getTargetRef());
            List<SequenceFlow> toFlowElementInComingFlow = FlowableUtils.getElementIncomingFlows(toFlowElement);
            if (toFlowElementInComingFlow.size() == 1) {
                List<GraphicInfo> graphicInfos = buildStartAndEndWaypoints(outgoingFlows.get(0));
                bpmnModel.addFlowGraphicInfoList(outgoingFlows.get(0).getId(), graphicInfos);
            }
        } else {
            createInflectionPoint(flowNode, START_TYPE);
        }
        List<SequenceFlow> incomingFlows = flowNode.getIncomingFlows();
        if (incomingFlows.size() != 1) {
            createInflectionPoint(flowNode, END_TYPE);
        }
    }

    /**
     * build start and end flow point when one node to one node
     *
     * @param sequenceFlow the floe
     * @return all flow point graphic info
     */
    private List<GraphicInfo> buildStartAndEndWaypoints(SequenceFlow sequenceFlow) {
        List<GraphicInfo> startAndEndPoint = new ArrayList<>();
        GraphicInfo startNodeGraphicInfo = locationMap.get(sequenceFlow.getSourceRef());
        GraphicInfo endNodeGraphicInfo = locationMap.get(sequenceFlow.getTargetRef());
        if (startNodeGraphicInfo.getX() >= endNodeGraphicInfo.getX()) {
            whenToXSmallerFromX(startAndEndPoint, startNodeGraphicInfo, endNodeGraphicInfo);
        } else {
            startAndEndPoint.add(new GraphicInfo(startNodeGraphicInfo.getX() + startNodeGraphicInfo.getWidth(), startNodeGraphicInfo.getY() + startNodeGraphicInfo.getHeight() / 2));
            startAndEndPoint.add(new GraphicInfo(endNodeGraphicInfo.getX(), endNodeGraphicInfo.getY() + endNodeGraphicInfo.getHeight() / 2));
        }
        return startAndEndPoint;
    }

    /**
     * reject node when start node x smaller end node x
     *
     * @param graphicInfoList      all flow point graphic info
     * @param startNodeGraphicInfo start node graphic
     * @param endNodeGraphicInfo   end node graphic
     */
    private void whenToXSmallerFromX(List<GraphicInfo> graphicInfoList, GraphicInfo startNodeGraphicInfo, GraphicInfo endNodeGraphicInfo) {
        graphicInfoList.add(new GraphicInfo(startNodeGraphicInfo.getX() + startNodeGraphicInfo.getWidth(), startNodeGraphicInfo.getY() + startNodeGraphicInfo.getHeight() / 2));
        graphicInfoList.add(new GraphicInfo(startNodeGraphicInfo.getX() + startNodeGraphicInfo.getWidth() + DISTANCE_X / 2, startNodeGraphicInfo.getY() + startNodeGraphicInfo.getHeight() / 2));
        graphicInfoList.add(new GraphicInfo(startNodeGraphicInfo.getX() + startNodeGraphicInfo.getWidth() + DISTANCE_X / 2, endNodeGraphicInfo.getY() - 30));
        graphicInfoList.add(new GraphicInfo(endNodeGraphicInfo.getX() + endNodeGraphicInfo.getWidth() / 2, endNodeGraphicInfo.getY() - 30));
        graphicInfoList.add(new GraphicInfo(endNodeGraphicInfo.getX() + endNodeGraphicInfo.getWidth() / 2, endNodeGraphicInfo.getY()));
    }

    /**
     * delete repeat point in the flow points
     *
     * @param graphicInfoList the flow points
     */
    private void deleteRepeatPoint(List<GraphicInfo> graphicInfoList) {
        Map<String, Map<String, Integer>> pointMap = new HashMap<>();
        for (int i = 0; i < graphicInfoList.size(); i++) {
            String point = graphicInfoList.get(i).getX() + "&&" + graphicInfoList.get(i).getY();
            if (!pointMap.containsKey(point)) {
                Map<String, Integer> startAndEnd = new HashMap<>();
                startAndEnd.put(START_TYPE, i);
                pointMap.put(point, startAndEnd);
            } else {
                pointMap.get(point).put(END_TYPE, i);
            }
        }
        if (pointMap.isEmpty()) {
            return;
        }
        Iterator<Map.Entry<String, Map<String, Integer>>> iterator = pointMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Map<String, Integer>> entry = iterator.next();
            Map<String, Integer> value = entry.getValue();
            if (value.get(START_TYPE) == null || value.get(END_TYPE) == null) {
                iterator.remove();
            }
        }
        for (Map.Entry<String, Map<String, Integer>> point : pointMap.entrySet()) {
            Map<String, Integer> value = point.getValue();
            for (Map.Entry<String, Map<String, Integer>> otherPoint : pointMap.entrySet()) {
                Map<String, Integer> otherValue = otherPoint.getValue();
                if (value.get(START_TYPE) < otherValue.get(START_TYPE) && value.get(END_TYPE) > otherValue.get(START_TYPE)) {
                    pointMap.remove(otherPoint.getKey());
                }
            }
            graphicInfoList.subList(value.get(START_TYPE) + 1, value.get(END_TYPE)).clear();
        }
    }

    /**
     * create inflection point in flow
     *
     * @param flowNode start node or end node
     * @param type     the type of the flow node is start node or end node
     */
    private void createInflectionPoint(FlowNode flowNode, String type) {
        GraphicInfo currentNode = locationMap.get(flowNode.getId());
        if (START_TYPE.equals(type)) {
            List<SequenceFlow> outgoingFlows = flowNode.getOutgoingFlows();
            for (SequenceFlow outgoingFlow : outgoingFlows) {
                List<GraphicInfo> graphicInfoList = new ArrayList<>();
                GraphicInfo startPoint = new GraphicInfo(currentNode.getX() + currentNode.getWidth(), currentNode.getY() + currentNode.getHeight() / 2);
                GraphicInfo commonInflectionPoint = new GraphicInfo(startPoint.getX() + DISTANCE_X / 2, startPoint.getY());
                String endNodeId = outgoingFlow.getTargetRef();
                GraphicInfo endGraphic = locationMap.get(endNodeId);
                List<GraphicInfo> specialInflectionPoint = new ArrayList<>();
                if (FlowableUtils.getElementIncomingFlows(process.getFlowElement(endNodeId)).size() > 1) {
                    continue;
                }
                specialInflectionPoint.add(new GraphicInfo(commonInflectionPoint.getX(), endGraphic.getY() + endGraphic.getHeight() / 2));
                specialInflectionPoint.add(new GraphicInfo(endGraphic.getX(), endGraphic.getY() + endGraphic.getHeight() / 2));
                Collections.addAll(graphicInfoList, startPoint, commonInflectionPoint);
                graphicInfoList.addAll(specialInflectionPoint);
                deleteRepeatPoint(graphicInfoList);
                bpmnModel.addFlowGraphicInfoList(outgoingFlow.getId(), graphicInfoList);
            }
        } else if (END_TYPE.equals(type)) {
            List<SequenceFlow> incomingFlows = flowNode.getIncomingFlows();
            for (SequenceFlow incomingFlow : incomingFlows) {
                List<GraphicInfo> graphicInfoList = new ArrayList<>();
                FlowElement sourceFlowElement = process.getFlowElement(incomingFlow.getSourceRef());
                GraphicInfo startNode = locationMap.get(incomingFlow.getSourceRef());
                GraphicInfo startPoint = new GraphicInfo(startNode.getX() + startNode.getWidth(), startNode.getY() + startNode.getHeight() / 2);
                graphicInfoList.add(startPoint);
                List<SequenceFlow> sourceFlowNodeOutgoingFlow = FlowableUtils.getElementOutgoingFlows(sourceFlowElement);
                if (sourceFlowNodeOutgoingFlow.size() == 1) {
                    if (currentNode.getX() <= startNode.getX()) {
                        graphicInfoList = new ArrayList<>();
                        whenToXSmallerFromX(graphicInfoList, startNode, currentNode);
                    } else {
                        graphicInfoList.add(new GraphicInfo(currentNode.getX() - DISTANCE_X / 2, startPoint.getY()));
                        graphicInfoList.add(new GraphicInfo(currentNode.getX() - DISTANCE_X / 2, currentNode.getY() + currentNode.getHeight() / 2));
                        graphicInfoList.add(new GraphicInfo(currentNode.getX(), currentNode.getY() + currentNode.getHeight() / 2));
                    }
                    deleteRepeatPoint(graphicInfoList);
                    bpmnModel.addFlowGraphicInfoList(incomingFlow.getId(), graphicInfoList);
                } else {
                    double originalX = startNode.getX() + startNode.getWidth() + DISTANCE_X / 2;
                    if ((currentNode.getX() <= startNode.getX())) {
                        graphicInfoList = new ArrayList<>();
                        whenToXSmallerFromX(graphicInfoList, startNode, currentNode);
                        deleteRepeatPoint(graphicInfoList);
                        bpmnModel.addFlowGraphicInfoList(incomingFlow.getId(), graphicInfoList);
                    } else {
                        boolean flag = true;
                        for (SequenceFlow sequenceFlow : sourceFlowNodeOutgoingFlow) {
                            List<GraphicInfo> flowLocationGraphicInfo = bpmnModel.getFlowLocationGraphicInfo(sequenceFlow.getId());
                            if (flowLocationGraphicInfo != null && !flowLocationGraphicInfo.isEmpty()) {
                                flag = false;
                            }
                        }
                        if (flag) {
                            createInflectionPoint((FlowNode) sourceFlowElement, START_TYPE);
                        }
                        int topCount = 0, downCount = 0;
                        double topY = startPoint.getY(), downY = startPoint.getY();
                        for (SequenceFlow sequenceFlow : sourceFlowNodeOutgoingFlow) {
                            List<GraphicInfo> sourceOutgoingFlowPoint = bpmnModel.getFlowLocationGraphicInfo(sequenceFlow.getId());
                            if (sourceOutgoingFlowPoint == null || sourceOutgoingFlowPoint.isEmpty()) {
                                continue;
                            }
                            for (GraphicInfo graphicInfo : sourceOutgoingFlowPoint) {
                                if (graphicInfo.getX() == originalX && graphicInfo.getY() > startPoint.getY()) {
                                    downCount++;
                                    if (graphicInfo.getY() > downY) {
                                        downY = graphicInfo.getY();
                                    }
                                } else if (graphicInfo.getX() == originalX && graphicInfo.getY() < startPoint.getY()) {
                                    topCount++;
                                    if (graphicInfo.getY() < topY) {
                                        topY = graphicInfo.getY();
                                    }
                                }
                            }
                        }
                        graphicInfoList.add(new GraphicInfo(originalX, startPoint.getY()));
                        if (topCount <= downCount) {
                            if (currentNode.getY() < startNode.getY()) {
                                graphicInfoList.add(new GraphicInfo(originalX, topY - DISTANCE_Y));
                                graphicInfoList.add(new GraphicInfo(currentNode.getX() - DISTANCE_X / 2, topY - DISTANCE_Y));
                            }
                            if (originalX != currentNode.getX() - DISTANCE_X / 2) {
                                graphicInfoList.add(new GraphicInfo(currentNode.getX() - DISTANCE_X / 2, startPoint.getY()));
                            }
                        } else {
                            if (currentNode.getY() > startNode.getY() && originalX != currentNode.getX() - DISTANCE_X / 2) {
                                graphicInfoList.add(new GraphicInfo(originalX, downY + DISTANCE_Y));
                                graphicInfoList.add(new GraphicInfo(currentNode.getX() - DISTANCE_X / 2, downY + DISTANCE_Y));
                            }
                            if (originalX != currentNode.getX() - DISTANCE_X / 2) {
                                graphicInfoList.add(new GraphicInfo(currentNode.getX() - DISTANCE_X / 2, startPoint.getY()));
                            }
                        }
                        graphicInfoList.add(new GraphicInfo(currentNode.getX() - DISTANCE_X / 2, currentNode.getY() + currentNode.getHeight() / 2));
                        graphicInfoList.add(new GraphicInfo(currentNode.getX(), currentNode.getY() + currentNode.getHeight() / 2));
                        deleteRepeatPoint(graphicInfoList);
                        bpmnModel.addFlowGraphicInfoList(incomingFlow.getId(), graphicInfoList);
                    }
                }
            }
        }
    }


    /**
     * graphic lapping nodes
     */
    private void graphicLappingNodes() {
        ArrayList<SourceOfOverlapping> sourceOfOverlappingList = new ArrayList<>();
        Set<String> overlappingElements = new HashSet<>();
        findOverlappingElements(overlappingElements, sourceOfOverlappingList);
        for (SourceOfOverlapping sourceOfOverlapping : sourceOfOverlappingList) {
            for (String toUpNode : sourceOfOverlapping.getToUpNodeSet()) {
                GraphicInfo graphicInfo = locationMap.get(toUpNode);
                graphicInfo.setY(graphicInfo.getY() - (DISTANCE_Y));
            }
            for (String toDownNode : sourceOfOverlapping.getToDownNodeSet()) {
                GraphicInfo graphicInfo = locationMap.get(toDownNode);
                graphicInfo.setY(graphicInfo.getY() + (DISTANCE_Y));
            }
        }
        sourceOfOverlappingList = new ArrayList<>();
        overlappingElements = new HashSet<>();
        findOverlappingElements(overlappingElements, sourceOfOverlappingList);
        if (!overlappingElements.isEmpty()) {
            graphicLappingNodes();
        }
    }

    /**
     * find all overlapping nodes
     *
     * @param overlappingElements     all overlapping nodes
     * @param sourceOfOverlappingList to update nodes
     */
    private void findOverlappingElements(Set<String> overlappingElements, List<SourceOfOverlapping> sourceOfOverlappingList) {
        Set<String> processedElements = new HashSet<>();
        for (String elementId : locationMap.keySet()) {
            if (process.getFlowElement(elementId) instanceof SequenceFlow) {
                continue;
            }
            if (processedElements.contains(elementId)) {
                continue;
            }
            GraphicInfo graphicInfo = locationMap.get(elementId);
            for (String otherElementId : locationMap.keySet()) {

                if (elementId.equals(otherElementId)) {
                    continue;
                }
                GraphicInfo otherGraphicInfo = locationMap.get(otherElementId);
                if (isOverlapping(graphicInfo, otherGraphicInfo)) {
                    SourceOfOverlapping sourceOfOverlapping = new SourceOfOverlapping();
                    sourceOfOverlapping.build(elementId, otherElementId, process);
                    sourceOfOverlappingList.add(sourceOfOverlapping);
                    overlappingElements.add(elementId);
                    overlappingElements.add(otherElementId);
                }
            }
            processedElements.add(elementId);
        }
    }

    /**
     * judge two element is overlap
     *
     * @param graphicInfo      element graphic
     * @param otherGraphicInfo other element graphic
     * @return judgement
     */
    private boolean isOverlapping(GraphicInfo graphicInfo, GraphicInfo otherGraphicInfo) {
        double x1 = graphicInfo.getX();
        double y1 = graphicInfo.getY();
        double width1 = graphicInfo.getWidth();
        double height1 = graphicInfo.getHeight();
        double x2 = otherGraphicInfo.getX();
        double y2 = otherGraphicInfo.getY();
        double width2 = otherGraphicInfo.getWidth();
        double height2 = otherGraphicInfo.getHeight();
        boolean horizontalOverlap = Math.abs(x1 - x2) < (width1 + width2) / 2.0;
        boolean verticalOverlap = Math.abs(y1 - y2) < (height1 + height2) / 2.0;
        return horizontalOverlap && verticalOverlap;
    }

    /**
     * start layout nodes
     */
    private void layoutNodes() {
        FlowElement startFlowElement = process.getFlowElements().stream().filter(flowElement -> flowElement instanceof StartEvent).findFirst().orElseThrow(() -> new IllegalStateException("No StartEvent found"));

        GraphicInfo startFlowElementGraphic = locationMap.get(startFlowElement.getId());
        startFlowElementGraphic.setX(START_EVENT_X);
        startFlowElementGraphic.setY(START_EVENT_Y);
        layoutNodes(startFlowElement);
    }

    /**
     * overloading layout nodes other than the start node
     * recursion method
     * this recursion method that stop when the parameter currentElement has not outgoing sequence flow
     *
     * @param currentElement current element
     */
    private void layoutNodes(FlowElement currentElement) {
        List<SequenceFlow> outgoingFlows = FlowableUtils.getElementOutgoingFlows(currentElement);

        double x = locationMap.get(currentElement.getId()).getX();
        double y = locationMap.get(currentElement.getId()).getY();
        double width = locationMap.get(currentElement.getId()).getWidth();
        double height = locationMap.get(currentElement.getId()).getHeight();
        if (outgoingFlows.isEmpty()) {
            return;
        }
        if (outgoingFlows.size() == 1) {
            String toFlowElementId = outgoingFlows.get(0).getTargetRef();
            FlowElement toFlowElement = process.getFlowElement(toFlowElementId);
            List<SequenceFlow> incomingFlows = FlowableUtils.getElementIncomingFlows(toFlowElement);
            GraphicInfo toGraphic = locationMap.get(toFlowElementId);
            if (incomingFlows.size() == 1) {
                updateLocation(toFlowElementId, x + width + DISTANCE_X, y + (height - toGraphic.getHeight()) / 2);
                layoutNodes(toFlowElement);
            } else {
                if (hasOnlyOneNoJudgeIncoming(outgoingFlows.get(0), incomingFlows)) {
                    updateLocation(toFlowElementId, x + width + DISTANCE_X, y + (height - toGraphic.getHeight()) / 2);
                    layoutedNodeSet.add(toFlowElementId);
                    layoutNodes(toFlowElement);
                } else {
                    layoutNodeWhenMoreToOne(toFlowElementId);
                }
            }
        } else {
            boolean isExclusiveGateway = currentElement instanceof ExclusiveGateway;
            if (isExclusiveGateway) {
                List<SequenceFlow> newOutgoingFlows = new ArrayList<>();
                for (SequenceFlow outgoingFlow : outgoingFlows) {
                    if (!layoutedNodeSet.contains(outgoingFlow.getTargetRef())) {
                        newOutgoingFlows.add(outgoingFlow);
                    }
                }
                outgoingFlows = newOutgoingFlows;
            }
            double topY = (y + height / 2) - ((double) (outgoingFlows.size() - 1) / 2) * DISTANCE_Y;
            for (SequenceFlow sequenceFlow : outgoingFlows) {
                String toFlowElementId = sequenceFlow.getTargetRef();
                FlowElement toFlowElement = process.getFlowElement(toFlowElementId);
                List<SequenceFlow> incomingFlows = FlowableUtils.getElementIncomingFlows(toFlowElement);
                GraphicInfo toFlowElementGraphic = locationMap.get(toFlowElementId);
                if (incomingFlows.size() == 1) {
                    updateLocation(toFlowElementId, x + width + DISTANCE_X, topY - toFlowElementGraphic.getHeight() / 2);
                    layoutNodes(toFlowElement);
                    topY += DISTANCE_Y;
                } else {
                    if (hasOnlyOneNoJudgeIncoming(outgoingFlows.get(0), incomingFlows)) {
                        updateLocation(toFlowElementId, x + width + DISTANCE_X, topY - toFlowElementGraphic.getHeight() / 2);
                        layoutNodes(toFlowElement);
                        topY += DISTANCE_Y;
                    } else {
                        layoutNodeWhenMoreToOne(toFlowElementId);
                    }
                }
            }
        }
    }

    /**
     * judge the node's incoming flows has one no judge line
     *
     * @param sequenceFlow  the no judge flow
     * @param incomingFlows the node's incoming flows
     * @return judgement
     */
    private boolean hasOnlyOneNoJudgeIncoming(SequenceFlow sequenceFlow, List<SequenceFlow> incomingFlows) {
        int judgeIncomingCount = 0;
        for (SequenceFlow incomingFlow : incomingFlows) {
            if (incomingFlow.getId().equals(sequenceFlow.getId())) {
                continue;
            }
            if (process.getFlowElement(incomingFlow.getSourceRef()) instanceof ExclusiveGateway) {
                judgeIncomingCount++;
            }
        }
        return judgeIncomingCount == incomingFlows.size() - 1;
    }

    /**
     * layout node when more node to one
     *
     * @param toFlowElementId to flow element id
     */
    private void layoutNodeWhenMoreToOne(String toFlowElementId) {
        FlowElement toFlowElement = process.getFlowElement(toFlowElementId);
        List<SequenceFlow> incomingFlows = FlowableUtils.getElementIncomingFlows(toFlowElement);
        Double maxX = incomingFlows.stream().map(SequenceFlow::getSourceRef).map(id -> locationMap.get(id).getX() + locationMap.get(id).getWidth() + DISTANCE_X).max(Comparator.naturalOrder()).orElse(0.0);
        double avgHeight = incomingFlows.stream().mapToDouble(incomingFlow -> {
            String sourceRef = incomingFlow.getSourceRef();
            GraphicInfo graphicInfo = locationMap.get(sourceRef);
            return graphicInfo.getY() + graphicInfo.getHeight() / 2;
        }).average().orElse(0.0);

        updateLocation(toFlowElementId, maxX, avgHeight - locationMap.get(toFlowElementId).getHeight() / 2);
        layoutNodes(toFlowElement);
    }


    /**
     * update the flow element x & y when id is parameter flowElementId
     *
     * @param flowElementId the id of the flow element
     * @param x             update x value
     * @param y             update y value
     */
    private void updateLocation(String flowElementId, double x, double y) {
        GraphicInfo graphicInfo = locationMap.get(flowElementId);
        graphicInfo.setX(x);
        graphicInfo.setY(y);
    }


    /**
     * set nodes of the process width and height
     */
    private void graphicNodeWidthAndHeight() {
        for (FlowElement flowElement : process.getFlowElements()) {
            if (flowElement instanceof SequenceFlow) {
                continue;
            }
            String flowElementId = flowElement.getId();
            GraphicInfo graphicInfo = new GraphicInfo();
            locationMap.put(flowElementId, graphicInfo);
            if (flowElement instanceof Event) {
                locationMap.get(flowElementId).setWidth(EVENT_WIDTH);
                locationMap.get(flowElementId).setHeight(EVENT_HEIGHT);
            }
            if (flowElement instanceof UserTask) {
                locationMap.get(flowElementId).setWidth(USER_TASK_WIDTH);
                locationMap.get(flowElementId).setHeight(USER_TASK_HEIGHT);
            }
            if (flowElement instanceof Gateway) {
                locationMap.get(flowElementId).setWidth(GATEWAY_WIDTH);
                locationMap.get(flowElementId).setHeight(GATEWAY_HEIGHT);
            }

        }
    }


}
