package com.lanternfish.workflow.utils.oop;

import org.flowable.bpmn.model.BpmnModel;
import org.flowable.bpmn.model.GraphicInfo;
import org.flowable.bpmn.model.Process;

import java.util.List;
import java.util.Map;

/**
 * @author Liam
 * @date 2024-4-15
 * @apiNote
 */
public class AutoDrawFlowOOP {
    /**
     * the process of the bpmnModel
     */
    private Process process;

    /**
     * the bpmnModel of the flow
     */
    private BpmnModel bpmnModel;

    /**
     * location map
     */
    private Map<String, GraphicInfo> locationMap;

    private AutoDrawFlowOOP(BpmnModel bpmnModel) {
        this.bpmnModel = bpmnModel;
        this.locationMap = bpmnModel.getLocationMap();
    }

    public void execute() {
        bpmnModel.getLocationMap().clear();
        bpmnModel.getFlowLocationMap().clear();
        for (Process bpmnModelProcess : bpmnModel.getProcesses()) {
            this.process = bpmnModelProcess;
            layout();
        }
    }

    private void layout() {
        graphicNodeWidthAndHeight();
    }

    /**
     * calculate the width and height of the node
     */
    private void graphicNodeWidthAndHeight() {
        List<NodeGraphicDef> nodeGraphicDefs = NodeGraphicDef.buildList(process);
        nodeGraphicDefs.forEach(nodeGraphicDef -> {
            GraphicInfo graphicInfo = new GraphicInfo(nodeGraphicDef.getX(), nodeGraphicDef.getY(), nodeGraphicDef.getWidth(), nodeGraphicDef.getHeight());
            locationMap.put(nodeGraphicDef.getId(), graphicInfo);
        });
    }




}
