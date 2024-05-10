package com.lanternfish.workflow.mapper;

import org.apache.ibatis.annotations.Param;

import com.lanternfish.common.core.mapper.BaseMapperPlus;
import com.lanternfish.workflow.domain.WfCopy;
import com.lanternfish.workflow.domain.vo.WfCopyVo;

/**
 * 流程抄送Mapper接口
 *
 * @author KonBAI
 * @date 2022-05-19
 */
public interface WfCopyMapper extends BaseMapperPlus<WfCopyMapper, WfCopy, WfCopyVo> {
	public int updateState(@Param("id") String id);
}
