package com.lanternfish.workflow.mapper;

import com.lanternfish.workflow.domain.WfFlowConfig;
import com.lanternfish.workflow.domain.vo.WfFlowConfigVo;

import org.apache.ibatis.annotations.Param;

import com.lanternfish.common.core.mapper.BaseMapperPlus;

/**
 * 流程配置主Mapper接口
 *
 * @author Liam
 * @date 2023-11-19
 */
public interface WfFlowConfigMapper extends BaseMapperPlus<WfFlowConfigMapper, WfFlowConfig, WfFlowConfigVo> {
	void updateFlowConfig(@Param("flowConfigVo") WfFlowConfigVo flowConfigVo);
	WfFlowConfig selectByModelIdAndNodeKey(@Param("flowConfigVo") WfFlowConfigVo flowConfigVo);
}
