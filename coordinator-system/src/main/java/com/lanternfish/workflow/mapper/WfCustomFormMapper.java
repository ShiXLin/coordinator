package com.lanternfish.workflow.mapper;

import com.lanternfish.workflow.domain.WfCustomForm;
import com.lanternfish.workflow.domain.vo.CustomFormVo;
import com.lanternfish.workflow.domain.vo.WfCustomFormVo;

import org.apache.ibatis.annotations.Param;

import com.lanternfish.common.core.mapper.BaseMapperPlus;

/**
 * 流程业务单Mapper接口
 *
 * @author Liam
 * @date 2023-10-09
 */
public interface WfCustomFormMapper extends BaseMapperPlus<WfCustomFormMapper, WfCustomForm, WfCustomFormVo> {
	void updateCustom(@Param("customFormVo") CustomFormVo customFormVo);
	WfCustomForm selectSysCustomFormById(Long formId);
	WfCustomForm selectSysCustomFormByServiceName(String serviceName);
}
