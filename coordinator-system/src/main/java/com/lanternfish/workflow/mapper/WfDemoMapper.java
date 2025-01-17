package com.lanternfish.workflow.mapper;

import com.lanternfish.workflow.domain.WfDemo;
import com.lanternfish.workflow.domain.vo.WfDemoVo;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lanternfish.common.core.mapper.BaseMapperPlus;

/**
 * DEMOMapper接口
 *
 * @author Liam
 * @date 2023-10-12
 */
public interface WfDemoMapper extends BaseMapperPlus<WfDemoMapper, WfDemo, WfDemoVo> {

	Page<WfDemoVo> myPage(Page<WfDemoVo> page, @Param(Constants.WRAPPER) QueryWrapper<WfDemoVo> queryWrapper);

}
