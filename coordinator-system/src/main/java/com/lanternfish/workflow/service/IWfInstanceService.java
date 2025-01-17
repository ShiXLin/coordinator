package com.lanternfish.workflow.service;

import com.lanternfish.workflow.domain.bo.WfTaskBo;
import org.flowable.engine.history.HistoricProcessInstance;

import java.util.Map;

/**
 * @author KonBAI
 * @createTime 2022/3/10 00:12
 */
public interface IWfInstanceService {

    /**
     * 结束流程实例
     *
     * @param vo
     */
    void stopProcessInstance(WfTaskBo vo);

    /**
     * 激活或挂起流程实例
     *
     * @param state      状态
     * @param instanceId 流程实例ID
     */
    void updateState(Integer state, String instanceId);

    /**
     * 删除流程实例ID
     *
     * @param instanceId   流程实例ID
     * @param deleteReason 删除原因
     */
    void delete(String instanceId, String deleteReason);

    /**
     * 根据实例ID查询历史实例数据
     *
     * @param processInstanceId
     * @return
     */
    HistoricProcessInstance getHistoricProcessInstanceById(String processInstanceId);


    /**
     * 查询流程详情信息
     * @param procInsId 流程实例ID
     * @param deployId 流程部署ID
     */
    Map<String, Object> queryDetailProcess(String procInsId, String deployId);

    /**
     * 删除流程实例ID
     *
     * @param instanceId   流程实例ID
     * @param deleteReason 删除原因
     * @param dataId       业务数据dataID
     */
    void delete(String instanceId, String deleteReason, String dataId);
    /**
     * 删除流程实例ID
     *
     * @param instanceId   流程实例ID，涉及业务DataId
     * @param deleteReason 删除原因
     */
	void deleteForDataId(String instanceId, String deleteReason);
}
