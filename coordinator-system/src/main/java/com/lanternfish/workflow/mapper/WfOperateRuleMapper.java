package com.lanternfish.workflow.mapper;

import com.lanternfish.workflow.domain.WfOperateRule;
import com.lanternfish.workflow.domain.vo.WfOperateRuleVo;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.lanternfish.common.core.mapper.BaseMapperPlus;

/**
 * 流程操作规则Mapper接口
 *
 * @author Liam
 * @date 2023-11-23
 */
public interface WfOperateRuleMapper extends BaseMapperPlus<WfOperateRuleMapper, WfOperateRule, WfOperateRuleVo> {

	//根据主表configId获取操作规则
	@Select("SELECT * FROM wf_operate_rule WHERE config_id = #{configId}")
	List<WfOperateRuleVo> selectRuleByConfigId(@Param("configId") Long configId);
}
