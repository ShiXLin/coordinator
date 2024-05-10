package com.lanternfish.workflow.mapper;

import com.lanternfish.common.core.mapper.BaseMapperPlus;
import com.lanternfish.workflow.domain.WfCustomForm;
import com.lanternfish.workflow.domain.WfDeployForm;
import com.lanternfish.workflow.domain.vo.WfDeployFormVo;

/**
 * 流程实例关联表单Mapper接口
 *
 * @author KonBAI
 * @createTime 2022/3/7 22:07
 */
public interface WfDeployFormMapper extends BaseMapperPlus<WfDeployFormMapper, WfDeployForm, WfDeployFormVo> {

	WfDeployForm selectSysDeployFormByFormId(String id);

}
