package com.lanternfish.system.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lanternfish.common.core.mapper.BaseMapperPlus;
import com.lanternfish.system.domain.SysNotice;
import com.lanternfish.system.domain.vo.SysNoticeVo;

/**
 * 通知公告Mapper接口
 *
 * @author Liam
 * @date 2023-09-21
 */
public interface SysNoticeMapper extends BaseMapperPlus<SysNoticeMapper, SysNotice, SysNoticeVo> {

	List<SysNotice> querySysNoticeListByUserId(Page<SysNotice> page, @Param("userId")String userId,@Param("msgCategory")String msgCategory);

}
