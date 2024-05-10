package com.lanternfish.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lanternfish.common.core.domain.PageQuery;
import com.lanternfish.common.core.page.TableDataInfo;
import com.lanternfish.common.core.service.CommonService;
import com.lanternfish.common.utils.StringUtils;
import com.lanternfish.common.websocket.WebSocketServer;
import com.lanternfish.system.domain.SysNoticeSend;
import com.lanternfish.system.domain.SysNotice;
import com.lanternfish.system.mapper.SysNoticeMapper;
import com.lanternfish.system.mapper.SysNoticeSendMapper;
import com.lanternfish.system.mapper.SysUserMapper;
import com.lanternfish.system.service.ISysNoticeService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

/**
 * 公告 服务层实现
 *
 *  @author Liam
 *  @date 2023-09-20
 */
@RequiredArgsConstructor
@Service
public class SysNoticeServiceImpl extends ServiceImpl<SysNoticeMapper, SysNotice> implements ISysNoticeService {

    private final SysNoticeMapper baseMapper;

    @Override
    public TableDataInfo<SysNotice> selectPageNoticeList(SysNotice notice, PageQuery pageQuery) {
        LambdaQueryWrapper<SysNotice> lqw = new LambdaQueryWrapper<SysNotice>()
            .like(StringUtils.isNotBlank(notice.getNoticeTitle()), SysNotice::getNoticeTitle, notice.getNoticeTitle())
            .eq(StringUtils.isNotBlank(notice.getNoticeType()), SysNotice::getNoticeType, notice.getNoticeType())
            .like(StringUtils.isNotBlank(notice.getCreateBy()), SysNotice::getCreateBy, notice.getCreateBy());
        Page<SysNotice> page = baseMapper.selectPage(pageQuery.build(), lqw);
        return TableDataInfo.build(page);
    }

    /**
     * 查询公告信息
     *
     * @param noticeId 公告ID
     * @return 公告信息
     */
    @Override
    public SysNotice selectNoticeById(Long noticeId) {
        return baseMapper.selectById(noticeId);
    }

    /**
     * 查询公告列表
     *
     * @param notice 公告信息
     * @return 公告集合
     */
    @Override
    public List<SysNotice> selectNoticeList(SysNotice notice) {
        return baseMapper.selectList(new LambdaQueryWrapper<SysNotice>()
            .like(StringUtils.isNotBlank(notice.getNoticeTitle()), SysNotice::getNoticeTitle, notice.getNoticeTitle())
            .eq(StringUtils.isNotBlank(notice.getNoticeType()), SysNotice::getNoticeType, notice.getNoticeType())
            .like(StringUtils.isNotBlank(notice.getCreateBy()), SysNotice::getCreateBy, notice.getCreateBy()));
    }

    /**
     * 新增公告
     *
     * @param notice 公告信息
     * @return 结果
     */
    @Override
    public int insertNotice(SysNotice notice) {
        return baseMapper.insert(notice);
    }

    /**
     * 修改公告
     *
     * @param notice 公告信息
     * @return 结果
     */
    @Override
    public int updateNotice(SysNotice notice) {
        return baseMapper.updateById(notice);
    }

    /**
     * 删除公告对象
     *
     * @param noticeId 公告ID
     * @return 结果
     */
    @Override
    public int deleteNoticeById(Long noticeId) {
        return baseMapper.deleteById(noticeId);
    }

    /**
     * 批量删除公告信息
     *
     * @param noticeIds 需要删除的公告ID
     * @return 结果
     */
    @Override
    public int deleteNoticeByIds(Long[] noticeIds) {
        return baseMapper.deleteBatchIds(Arrays.asList(noticeIds));
    }

	@Override
	public Page<SysNotice> querySysNoticePageByUserId(Page<SysNotice> page, Long userId, String msgCategory) {
		if (page.getSize() == -1) {
			return page.setRecords(baseMapper.querySysNoticeListByUserId(null, userId.toString(), msgCategory));
		} else {
			return page.setRecords(baseMapper.querySysNoticeListByUserId(page, userId.toString(), msgCategory));
		}
	}

}
