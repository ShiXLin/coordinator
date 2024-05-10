package com.lanternfish.workflow.service;

import com.lanternfish.workflow.domain.vo.CustomFormVo;
import com.lanternfish.workflow.domain.vo.WfCustomFormVo;
import com.lanternfish.workflow.domain.WfCustomForm;
import com.lanternfish.workflow.domain.bo.WfCustomFormBo;
import com.lanternfish.common.core.page.TableDataInfo;
import com.lanternfish.common.core.domain.PageQuery;

import java.util.Collection;
import java.util.List;

import org.flowable.bpmn.model.BpmnModel;

/**
 * 流程业务单Service接口
 *
 * @author Liam
 * @date 2023-10-09
 */
public interface IWfCustomFormService {

    /**
     * 查询流程业务单
     */
    WfCustomFormVo queryById(Long id);

    /**
     * 查询流程业务单列表
     */
    TableDataInfo<WfCustomFormVo> queryPageList(WfCustomFormBo bo, PageQuery pageQuery);

    /**
     * 查询流程业务单列表
     */
    List<WfCustomFormVo> queryList(WfCustomFormBo bo);

    /**
     * 新增流程业务单
     */
    Boolean insertByBo(WfCustomFormBo bo);

    /**
     * 修改流程业务单
     */
    Boolean updateByBo(WfCustomFormBo bo);

    /**
     * 校验并批量删除流程业务单信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    void updateCustom(CustomFormVo customFormVo);

	WfCustomForm selectSysCustomFormByServiceName(String serviceName);
	/**
     * 保存流程实例关联自定义业务表单
     * @param deployId 部署ID
     * @param deployName 部署名称
     * @param bpmnModel bpmnModel对象
     * @return
     */
    boolean saveCustomDeployForm(String deployId, String deployName, BpmnModel bpmnModel);
}
