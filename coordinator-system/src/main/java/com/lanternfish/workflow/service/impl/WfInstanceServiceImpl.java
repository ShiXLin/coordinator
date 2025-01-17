package com.lanternfish.workflow.service.impl;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.BetweenFormatter;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lanternfish.common.core.domain.entity.SysDept;
import com.lanternfish.common.core.domain.entity.SysRole;
import com.lanternfish.common.core.domain.model.LoginUser;
import com.lanternfish.common.core.service.CommonService;
import com.lanternfish.common.core.service.UserService;
import com.lanternfish.common.exception.ServiceException;
import com.lanternfish.common.utils.JsonUtils;
import com.lanternfish.common.utils.SpringContextUtils;
import com.lanternfish.common.utils.StringUtils;
import com.lanternfish.flowable.common.constant.TaskConstants;
import com.lanternfish.flowable.core.domain.ActStatus;
import com.lanternfish.flowable.factory.FlowServiceFactory;
import com.lanternfish.system.service.ISysDeptService;
import com.lanternfish.system.service.ISysRoleService;
import com.lanternfish.system.service.ISysUserService;
import com.lanternfish.workflow.domain.WfMyBusiness;
import com.lanternfish.workflow.domain.bo.WfTaskBo;
import com.lanternfish.workflow.domain.vo.WfFormVo;
import com.lanternfish.workflow.domain.vo.WfTaskVo;
import com.lanternfish.workflow.service.IWfDeployFormService;
import com.lanternfish.workflow.service.IWfInstanceService;
import com.lanternfish.workflow.service.WfCallBackServiceI;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.collections4.CollectionUtils;
import org.flowable.common.engine.api.FlowableObjectNotFoundException;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.task.Comment;
import org.flowable.identitylink.api.history.HistoricIdentityLink;
import org.flowable.task.api.Task;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import javax.annotation.Resource;

/**
 * 工作流流程实例管理
 *
 * @author KonBAI
 * @createTime 2022/3/10 00:12
 */
@RequiredArgsConstructor
@Service
@Slf4j
public class WfInstanceServiceImpl extends FlowServiceFactory implements IWfInstanceService {

    private final IWfDeployFormService deployFormService;
    private final UserService userService;
    private final ISysRoleService roleService;
    private final ISysDeptService deptService;
    private final ISysUserService sysUserService;
    private final  WfMyBusinessServiceImpl wfMyBusinessService;
    @Resource
    private CommonService commonService;

    /**
     * 结束流程实例
     *
     * @param vo
     */
    @Override
    public void stopProcessInstance(WfTaskBo vo) {
        String taskId = vo.getTaskId();

    }

    /**
     * 激活或挂起流程实例
     *
     * @param state      状态
     * @param instanceId 流程实例ID
     */
    @Override
    public void updateState(Integer state, String instanceId) {

        // 激活
        if (state == 1) {
            runtimeService.activateProcessInstanceById(instanceId);
        }
        // 挂起
        if (state == 2) {
            runtimeService.suspendProcessInstanceById(instanceId);
        }
    }

    /**
     * 删除流程实例ID
     *
     * @param instanceId   流程实例ID
     * @param deleteReason 删除原因
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(String instanceId, String deleteReason) {

        // 查询历史数据
        HistoricProcessInstance historicProcessInstance = getHistoricProcessInstanceById(instanceId);
        if (historicProcessInstance.getEndTime() != null) {
            historyService.deleteHistoricProcessInstance(historicProcessInstance.getId());
            return;
        }
        // 删除流程实例
        runtimeService.deleteProcessInstance(instanceId, deleteReason);
        // 删除历史流程实例
        historyService.deleteHistoricProcessInstance(instanceId);
    }

    /**
     * 删除流程实例ID
     *
     * @param instanceId   流程实例ID
     * @param deleteReason 删除原因
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(String instanceId, String deleteReason, String dataId ) {
       if( !dataId.toString().equals("null")){
    	   LambdaQueryWrapper<WfMyBusiness> flowMyBusinessLambdaQueryWrapper = new LambdaQueryWrapper<>();
           flowMyBusinessLambdaQueryWrapper.eq(WfMyBusiness::getDataId, dataId)
           ;
           //如果保存数据前未调用必调的FlowCommonService.initActBusiness方法，就会有问题
           WfMyBusiness business = wfMyBusinessService.getOne(flowMyBusinessLambdaQueryWrapper);
           this.deleteForDataId(business.getProcessInstanceId(),deleteReason);
       }
       else {
    	   List<Task> task = taskService.createTaskQuery().processInstanceId(instanceId).list();
           if (CollectionUtils.isEmpty(task)) {//真要想删除，可以考虑去掉这部分代码
               throw new ServiceException("流程未启动或已执行完成，删除申请失败");
           }
	        // 查询历史数据
	        HistoricProcessInstance historicProcessInstance = getHistoricProcessInstanceById(instanceId);
	        if (historicProcessInstance.getEndTime() != null) {
	            historyService.deleteHistoricProcessInstance(historicProcessInstance.getId());
	            return;
	        }
	        // 删除流程实例
	        runtimeService.deleteProcessInstance(instanceId, deleteReason);
	        // 删除历史流程实例
	        historyService.deleteHistoricProcessInstance(instanceId);
       }
    }

    /**
     * 删除流程实例ID
     *
     * @param instanceId   流程实例ID，涉及业务DataId
     * @param deleteReason 删除原因
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteForDataId(String instanceId, String deleteReason) {
        List<Task> task = taskService.createTaskQuery().processInstanceId(instanceId).list();
        if (CollectionUtils.isEmpty(task)) {
            throw new ServiceException("流程未启动或已执行完成，删除申请失败");
        }
        // 查询历史数据
        HistoricProcessInstance historicProcessInstance = getHistoricProcessInstanceById(instanceId);
        if (historicProcessInstance.getEndTime() != null) {
            historyService.deleteHistoricProcessInstance(historicProcessInstance.getId());
            return;
        }
        // 删除流程实例
        runtimeService.deleteProcessInstance(instanceId, deleteReason);
        // 删除历史流程实例
        historyService.deleteHistoricProcessInstance(instanceId);
        /*======================撤回删除 回调以及关键数据保存======================*/
        LambdaQueryWrapper<WfMyBusiness> flowMyBusinessLambdaQueryWrapper = new LambdaQueryWrapper<>();
        flowMyBusinessLambdaQueryWrapper.eq(WfMyBusiness::getProcessInstanceId,instanceId)
        ;
        //如果保存数据前未调用必调的FlowCommonService.initActBusiness方法，就会有问题
        WfMyBusiness business = wfMyBusinessService.getOne(flowMyBusinessLambdaQueryWrapper);
        //设置数据
        String doneUsers = business.getDoneUsers();
        //SysUser sysUser = iFlowThirdService.getLoginUser();
        LoginUser loginUser = commonService.getLoginUser();
        // 处理过流程的人
        JSONArray doneUserList = new JSONArray();
        if (StrUtil.isNotBlank(doneUsers)){
            doneUserList = JSON.parseArray(doneUsers);
        }
        if (!doneUserList.contains(loginUser.getUsername())){
            doneUserList.add(loginUser.getUsername());
        }
        business.setActStatus(ActStatus.recall);
        business.setTaskId("");
        business.setTaskName("已撤回");
        business.setPriority("");
        business.setDoneUsers(doneUserList.toJSONString());
        business.setTodoUsers("");
        wfMyBusinessService.updateById(business);
        //spring容器类名
        String serviceImplName = business.getServiceImplName();
        WfCallBackServiceI flowCallBackService = (WfCallBackServiceI) SpringContextUtils.getBean(serviceImplName);
        // 流程处理完后，进行回调业务层
        if (flowCallBackService!=null)flowCallBackService.afterFlowHandle(business);

    }

    /**
     * 根据实例ID查询历史实例数据
     *
     * @param processInstanceId
     * @return
     */
    @Override
    public HistoricProcessInstance getHistoricProcessInstanceById(String processInstanceId) {
        HistoricProcessInstance historicProcessInstance =
                historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
        if (Objects.isNull(historicProcessInstance)) {
            throw new FlowableObjectNotFoundException("流程实例不存在: " + processInstanceId);
        }
        return historicProcessInstance;
    }


    /**
     * 流程历史流转记录
     *
     * @param procInsId 流程实例Id
     * @return
     */
    @Override
    public Map<String, Object> queryDetailProcess(String procInsId, String deployId) {
        Map<String, Object> map = new HashMap<>();
        if (StringUtils.isNotBlank(procInsId)) {
            List<HistoricTaskInstance> taskInstanceList = historyService.createHistoricTaskInstanceQuery()
                .processInstanceId(procInsId)
                .orderByHistoricTaskInstanceStartTime().desc()
                .list();
            List<Comment> commentList = taskService.getProcessInstanceComments(procInsId);
            List<WfTaskVo> taskVoList = new ArrayList<>(taskInstanceList.size());
            taskInstanceList.forEach(taskInstance -> {
                WfTaskVo taskVo = new WfTaskVo();
                taskVo.setProcDefId(taskInstance.getProcessDefinitionId());
                taskVo.setTaskId(taskInstance.getId());
                taskVo.setTaskDefKey(taskInstance.getTaskDefinitionKey());
                taskVo.setTaskName(taskInstance.getName());
                taskVo.setCreateTime(taskInstance.getStartTime());
                taskVo.setFinishTime(taskInstance.getEndTime());
                if (StringUtils.isNotBlank(taskInstance.getAssignee())) {
                    String userId = taskInstance.getAssignee();
                    String nickName = sysUserService.selectUserByUserName(userId).getNickName();
                    taskVo.setAssigneeId(userId);
                    taskVo.setAssigneeName(nickName);
                }
                // 展示审批人员
                List<HistoricIdentityLink> linksForTask = historyService.getHistoricIdentityLinksForTask(taskInstance.getId());
                StringBuilder stringBuilder = new StringBuilder();
                for (HistoricIdentityLink identityLink : linksForTask) {
                    if ("candidate".equals(identityLink.getType())) {
                        if (StringUtils.isNotBlank(identityLink.getUserId())) {
                            String userId = identityLink.getUserId();
                            String nickName = sysUserService.selectUserByUserName(userId).getNickName();
                            stringBuilder.append(nickName).append(",");
                        }
                        if (StringUtils.isNotBlank(identityLink.getGroupId())) {
                            if (identityLink.getGroupId().startsWith(TaskConstants.ROLE_GROUP_PREFIX)) {
                                Long roleId = Long.parseLong(StringUtils.stripStart(identityLink.getGroupId(), TaskConstants.ROLE_GROUP_PREFIX));
                                SysRole role = roleService.selectRoleById(roleId);
                                stringBuilder.append(role.getRoleName()).append(",");
                            } else if (identityLink.getGroupId().startsWith(TaskConstants.DEPT_GROUP_PREFIX)) {
                                Long deptId = Long.parseLong(StringUtils.stripStart(identityLink.getGroupId(), TaskConstants.DEPT_GROUP_PREFIX));
                                SysDept dept = deptService.selectDeptById(deptId);
                                stringBuilder.append(dept.getDeptName()).append(",");
                            }
                        }
                    }
                }
                if (StringUtils.isNotBlank(stringBuilder)) {
                    taskVo.setCandidate(stringBuilder.substring(0, stringBuilder.length() - 1));
                }
                if (ObjectUtil.isNotNull(taskInstance.getDurationInMillis())) {
                    taskVo.setDuration(DateUtil.formatBetween(taskInstance.getDurationInMillis(), BetweenFormatter.Level.SECOND));
                }
                // 获取意见评论内容
                if (CollUtil.isNotEmpty(commentList)) {
                    List<Comment> comments = new ArrayList<>();
                    // commentList.stream().filter(comment -> taskInstance.getId().equals(comment.getTaskId())).collect(Collectors.toList());
                    for (Comment comment : commentList) {
                        if (comment.getTaskId().equals(taskInstance.getId())) {
                            comments.add(comment);
                            // taskVo.setComment(WfCommentDto.builder().type(comment.getType()).comment(comment.getFullMessage()).build());
                        }
                    }
                    taskVo.setCommentList(comments);
                }
                taskVoList.add(taskVo);
            });
            map.put("flowList", taskVoList);
//            // 查询当前任务是否完成
//            List<Task> taskList = taskService.createTaskQuery().processInstanceId(procInsId).list();
//            if (CollectionUtils.isNotEmpty(taskList)) {
//                map.put("finished", true);
//            } else {
//                map.put("finished", false);
//            }
        }
        // 第一次申请获取初始化表单
        if (StringUtils.isNotBlank(deployId)) {
            WfFormVo formVo = deployFormService.selectDeployFormByDeployId(deployId);
            if (Objects.isNull(formVo)) {
                throw new ServiceException("请先配置流程表单");
            }
            map.put("formData", JsonUtils.parseObject(formVo.getContent(), Map.class));
        }
        return map;
    }
}
