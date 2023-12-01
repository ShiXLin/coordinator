package com.ruoyi.workflow.mapper;

import com.ruoyi.workflow.domain.WfFlowConfig;
import com.ruoyi.workflow.domain.vo.WfFlowConfigVo;

import org.apache.ibatis.annotations.Param;

import com.ruoyi.common.core.mapper.BaseMapperPlus;

/**
 * 流程配置主Mapper接口
 *
 * @author nbacheng
 * @date 2023-11-19
 */
public interface WfFlowConfigMapper extends BaseMapperPlus<WfFlowConfigMapper, WfFlowConfig, WfFlowConfigVo> {
	void updateFlowConfig(@Param("flowConfigVo") WfFlowConfigVo flowConfigVo);
	WfFlowConfig selectByModelIdAndNodeKey(@Param("flowConfigVo") WfFlowConfigVo flowConfigVo);
}
