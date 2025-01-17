package com.lanternfish.workflow.mapper;

import com.lanternfish.workflow.domain.WfCustomRule;
import com.lanternfish.workflow.domain.vo.WfCustomRuleVo;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.lanternfish.common.core.mapper.BaseMapperPlus;

/**
 * 流程自定义业务规则Mapper接口
 *
 * @author Liam
 * @date 2023-11-23
 */
public interface WfCustomRuleMapper extends BaseMapperPlus<WfCustomRuleMapper, WfCustomRule, WfCustomRuleVo> {

	//根据主表configId获取自定义表单规则
	@Select("SELECT * FROM wf_custom_rule WHERE config_id = #{configId}")
	List<WfCustomRuleVo> selectRuleByConfigId(@Param("configId") Long configId);
}
