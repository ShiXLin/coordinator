package com.lanternfish.system.mapper;

import com.lanternfish.system.domain.SysNoticeSend;
import com.lanternfish.system.domain.vo.SysNoticeSendVo;
import com.lanternfish.system.model.NoticeSendModel;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lanternfish.common.core.mapper.BaseMapperPlus;

/**
 * 用户公告阅读标记Mapper接口
 *
 * @author Liam
 * @date 2023-09-21
 */
public interface SysNoticeSendMapper extends BaseMapperPlus<SysNoticeSendMapper, SysNoticeSend, SysNoticeSendVo> {

	List<NoticeSendModel> getMyNoticeSendList(Page<NoticeSendModel> pageList, @Param("noticeSendModel")NoticeSendModel noticeSendModel);

}
