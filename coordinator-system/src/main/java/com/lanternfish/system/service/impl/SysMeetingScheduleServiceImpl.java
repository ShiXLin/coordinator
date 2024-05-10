package com.lanternfish.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.lanternfish.common.utils.StringUtils;
import com.lanternfish.common.core.page.TableDataInfo;
import com.lanternfish.common.core.domain.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.lanternfish.system.domain.bo.SysMeetingScheduleBo;
import com.lanternfish.system.domain.vo.SysMeetingScheduleVo;
import com.lanternfish.system.domain.SysMeetingSchedule;
import com.lanternfish.system.mapper.SysMeetingScheduleMapper;
import com.lanternfish.system.service.ISysMeetingScheduleService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * MeetingScheduleService业务层处理
 *
 * @author Liam
 * @date 2024-04-22
 */
@RequiredArgsConstructor
@Service
public class SysMeetingScheduleServiceImpl implements ISysMeetingScheduleService {

    private final SysMeetingScheduleMapper baseMapper;

    /**
     * 查询MeetingSchedule
     */
    @Override
    public SysMeetingScheduleVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询MeetingSchedule列表
     */
    @Override
    public TableDataInfo<SysMeetingScheduleVo> queryPageList(SysMeetingScheduleBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<SysMeetingSchedule> lqw = buildQueryWrapper(bo);
        Page<SysMeetingScheduleVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询MeetingSchedule列表
     */
    @Override
    public List<SysMeetingScheduleVo> queryList(SysMeetingScheduleBo bo) {
        LambdaQueryWrapper<SysMeetingSchedule> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<SysMeetingSchedule> buildQueryWrapper(SysMeetingScheduleBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<SysMeetingSchedule> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getRoomId() != null, SysMeetingSchedule::getRoomId, bo.getRoomId());
        lqw.eq(bo.getStartTime() != null, SysMeetingSchedule::getStartTime, bo.getStartTime());
        lqw.eq(bo.getEndTime() != null, SysMeetingSchedule::getEndTime, bo.getEndTime());
        lqw.eq(bo.getStatus() != null, SysMeetingSchedule::getStatus, bo.getStatus());
        return lqw;
    }

    /**
     * 新增MeetingSchedule
     */
    @Override
    public Boolean insertByBo(SysMeetingScheduleBo bo) {
        SysMeetingSchedule add = BeanUtil.toBean(bo, SysMeetingSchedule.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改MeetingSchedule
     */
    @Override
    public Boolean updateByBo(SysMeetingScheduleBo bo) {
        SysMeetingSchedule update = BeanUtil.toBean(bo, SysMeetingSchedule.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(SysMeetingSchedule entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除MeetingSchedule
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }
}
