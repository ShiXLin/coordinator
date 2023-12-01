package com.ruoyi.workflow.service;

import com.ruoyi.workflow.domain.WfFlowConfig;
import com.ruoyi.workflow.domain.vo.WfFlowConfigVo;
import com.ruoyi.workflow.domain.vo.WfRuleVo;
import com.ruoyi.workflow.domain.bo.WfFlowConfigBo;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.core.domain.PageQuery;

import java.util.Collection;
import java.util.List;

import javax.validation.constraints.NotNull;


/**
 * 流程配置主Service接口
 *
 * @author nbacheng
 * @date 2023-11-19
 */
public interface IWfFlowConfigService {

    /**
     * 查询流程配置主
     */
    WfFlowConfigVo queryById(Long id);

    /**
     * 查询流程配置主列表
     */
    TableDataInfo<WfFlowConfigVo> queryPageList(WfFlowConfigBo bo, PageQuery pageQuery);

    /**
     * 查询流程配置主列表
     */
    List<WfFlowConfigVo> queryList(WfFlowConfigBo bo);

    /**
     * 新增流程配置主
     */
    Boolean insertByBo(WfFlowConfigBo bo);

    /**
     * 修改流程配置主
     */
    Boolean updateByBo(WfFlowConfigBo bo);

    /**
     * 校验并批量删除流程配置主信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
    
    void updateFlowConfig(WfFlowConfigVo flowConfigVo);
    
	WfFlowConfig selectByModelIdAndNodeKey(WfFlowConfigVo flowConfigVo);

	/**
     * 获取流程节点规则信息
     */
	WfRuleVo queryConfigRule(WfFlowConfigBo bo);

	Boolean updateConfigRule(WfRuleVo vo);
}
