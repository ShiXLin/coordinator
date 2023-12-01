package com.ruoyi.workflow.mapper;

import com.ruoyi.workflow.domain.WfOperateRule;
import com.ruoyi.workflow.domain.vo.WfOperateRuleVo;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.ruoyi.common.core.mapper.BaseMapperPlus;

/**
 * 流程操作规则Mapper接口
 *
 * @author nbacheng
 * @date 2023-11-23
 */
public interface WfOperateRuleMapper extends BaseMapperPlus<WfOperateRuleMapper, WfOperateRule, WfOperateRuleVo> {

	//根据主表configId获取操作规则
	@Select("SELECT * FROM wf_operate_rule WHERE config_id = #{configId}")
	List<WfOperateRuleVo> selectRuleByConfigId(@Param("configId") Long configId);
}
