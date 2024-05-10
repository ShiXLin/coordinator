package com.lanternfish.flowable.listener;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.flowable.bpmn.model.FlowElement;
import org.flowable.bpmn.model.UserTask;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.runtime.ProcessInstance;
import org.springframework.stereotype.Component;

import com.lanternfish.common.core.service.CommonService;
import com.lanternfish.common.utils.SpringContextUtils;

import cn.hutool.core.util.ObjectUtil;
import lombok.AllArgsConstructor;

/**
 * 部门经理处理类
 *
 * @author Liam
 * @date 2023-08-06
 */
@AllArgsConstructor
@Component("DepManagerHandler")
public class DepManagerHandler {

	private CommonService commonService = SpringContextUtils.getBean(CommonService.class);
	RuntimeService runtimeService = SpringContextUtils.getBean(RuntimeService.class);

	public String getUser(DelegateExecution execution) {
		String assignUserName = "";
		FlowElement flowElement = execution.getCurrentFlowElement();
        if (ObjectUtil.isNotEmpty(flowElement) && flowElement instanceof UserTask) {
            UserTask userTask = (UserTask) flowElement;
            if ( StringUtils.isNotEmpty(userTask.getAssignee())) {
            	if(StringUtils.contains(userTask.getAssignee(),"DepManagerHandler")) {
            		 // 获取流程发起人
            		ProcessInstance processInstance = runtimeService
                            .createProcessInstanceQuery()
                            .processInstanceId(execution.getProcessInstanceId())
                            .singleResult();
                    String startUserId = processInstance.getStartUserId();
                    // 获取部门负责人
                    assignUserName = commonService.getDepLeaderByUserName(startUserId);
            	}
            }
        }
        return assignUserName;
	}
}
