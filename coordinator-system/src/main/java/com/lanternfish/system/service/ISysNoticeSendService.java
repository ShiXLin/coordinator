package com.lanternfish.system.service;

import com.lanternfish.system.domain.SysNoticeSend;
import com.lanternfish.system.domain.vo.SysNoticeSendVo;
import com.lanternfish.system.model.NoticeSendModel;
import com.lanternfish.system.domain.bo.SysNoticeSendBo;
import com.lanternfish.common.core.page.TableDataInfo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lanternfish.common.core.domain.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 用户公告阅读标记Service接口
 *
 * @author Liam
 * @date 2023-09-21
 */
public interface ISysNoticeSendService extends IService<SysNoticeSend> {

    /**
     * 查询用户公告阅读标记
     */
    SysNoticeSendVo queryById(Long sendId);

    /**
     * 查询用户公告阅读标记列表
     */
    TableDataInfo<SysNoticeSendVo> queryPageList(SysNoticeSendBo bo, PageQuery pageQuery);

    /**
     * 查询用户公告阅读标记列表
     */
    List<SysNoticeSendVo> queryList(SysNoticeSendBo bo);

    /**
     * 新增用户公告阅读标记
     */
    Boolean insertByBo(SysNoticeSendBo bo);

    /**
     * 修改用户公告阅读标记
     */
    Boolean updateByBo(SysNoticeSendBo bo);

    /**
     * 校验并批量删除用户公告阅读标记信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

	Page<NoticeSendModel> getMyNoticeSendPage(Page<NoticeSendModel> pageList, NoticeSendModel noticeSendModel);
}
