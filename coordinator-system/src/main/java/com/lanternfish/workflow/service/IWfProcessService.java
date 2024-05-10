package com.lanternfish.workflow.service;

import com.lanternfish.common.core.domain.PageQuery;
import com.lanternfish.common.core.domain.R;
import com.lanternfish.common.core.page.TableDataInfo;
import com.lanternfish.flowable.core.FormConf;
import com.lanternfish.flowable.core.domain.ProcessQuery;
import com.lanternfish.workflow.domain.bo.WfNewProcessBo;
import com.lanternfish.workflow.domain.vo.WfDefinitionVo;
import com.lanternfish.workflow.domain.vo.WfDetailVo;
import com.lanternfish.workflow.domain.vo.WfFlowViewVo;
import com.lanternfish.workflow.domain.vo.WfTaskVo;

import java.util.List;
import java.util.Map;

/**
 * @author KonBAI
 * @createTime 2022/3/24 18:57
 */
public interface IWfProcessService {

    /**
     * 查询可发起流程列表
     * @param pageQuery 分页参数
     * @return
     */
    TableDataInfo<WfDefinitionVo> selectPageStartProcessList(ProcessQuery processQuery, PageQuery pageQuery);

    /**
     * 查询可发起流程列表
     */
    List<WfDefinitionVo> selectStartProcessList(ProcessQuery processQuery);

    /**
     * 查询我的流程列表
     * @param pageQuery 分页参数
     */
    TableDataInfo<WfTaskVo> selectPageOwnProcessList(ProcessQuery processQuery, PageQuery pageQuery);

    /**
     * 查询我的流程列表
     */
    List<WfTaskVo> selectOwnProcessList(ProcessQuery processQuery);

    /**
     * 查询代办任务列表
     * @param pageQuery 分页参数
     */
    TableDataInfo<WfTaskVo> selectPageTodoProcessList(ProcessQuery processQuery, PageQuery pageQuery);

    /**
     * 查询代办任务列表
     */
    List<WfTaskVo> selectTodoProcessList(ProcessQuery processQuery);

    /**
     * 查询待签任务列表
     * @param pageQuery 分页参数
     */
    TableDataInfo<WfTaskVo> selectPageClaimProcessList(ProcessQuery processQuery, PageQuery pageQuery);

    /**
     * 查询待签任务列表
     */
    List<WfTaskVo> selectClaimProcessList(ProcessQuery processQuery);

    /**
     * 查询已办任务列表
     * @param pageQuery 分页参数
     */
    TableDataInfo<WfTaskVo> selectPageFinishedProcessList(ProcessQuery processQuery, PageQuery pageQuery);

    /**
     * 查询已办任务列表
     */
    List<WfTaskVo> selectFinishedProcessList(ProcessQuery processQuery);

    /**
     * 查询流程部署关联表单信息
     * @param definitionId 流程定义ID
     * @param deployId 部署ID
     */
    FormConf selectFormContent(String definitionId, String deployId, String procInsId);

    /**
     * 启动流程实例
     * @param procDefId 流程定义ID
     * @param variables 扩展参数
     */
    R<Void> startProcessByDefId(String procDefId, Map<String, Object> variables);

    /**
     * 通过DefinitionKey启动流程
     * @param procDefKey 流程定义Key
     * @param variables 扩展参数
     */
    R<Void> startProcessByDefKey(String procDefKey, Map<String, Object> variables);

    /**
     * 删除流程实例
     */
    void deleteProcessByIds(String[] instanceIds);


    /**
     * 读取xml文件
     * @param processDefId 流程定义ID
     */
    String queryBpmnXmlById(String processDefId);


    /**
     * 查询流程任务详情信息
     * @param procInsId 流程实例ID
     * @param taskId 任务ID
     * @param dataId 因为表单ID
     */
    WfDetailVo queryProcessDetail(String procInsId, String taskId, String dataId);

    /**
     * 查询流程是否结束
     * @param procInsId 流程实例ID
     *
     */
	boolean processIscompleted(String procInsId);

	R<Void> startProcessByDataId(String dataId, String serviceName, Map<String, Object> variables);

	/**
	 * 流程详情信息
	 *
	 * @param dataId 业务数据ID
	 * @return
	 */
	WfDetailVo queryProcessDetailByDataId(String dataId);

	R<Void> dingdingToBpmn(String ddjson);

    /**
     * 新的流程模型预览
     * @param procInsId 流程实例id
     * @param taskId 当前任务id
     * @param dataId 业务数据id
     * @return 流程模型预览
     */
    WfFlowViewVo newProcessDetail(String procInsId, String taskId, String dataId);

    /**
     * 将json对象转化为流程模板导入并开启流程
     * @param newProcess json对象
     */
    void newProcessFromJson(WfNewProcessBo newProcess);
}
