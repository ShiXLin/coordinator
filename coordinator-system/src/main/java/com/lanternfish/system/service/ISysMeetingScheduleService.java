package com.lanternfish.system.service;

import com.lanternfish.system.domain.SysMeetingSchedule;
import com.lanternfish.system.domain.vo.SysMeetingScheduleVo;
import com.lanternfish.system.domain.bo.SysMeetingScheduleBo;
import com.lanternfish.common.core.page.TableDataInfo;
import com.lanternfish.common.core.domain.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * MeetingScheduleService接口
 *
 * @author Liam
 * @date 2024-04-22
 */
public interface ISysMeetingScheduleService {

    /**
     * 查询MeetingSchedule
     */
    SysMeetingScheduleVo queryById(Long id);

    /**
     * 查询MeetingSchedule列表
     */
    TableDataInfo<SysMeetingScheduleVo> queryPageList(SysMeetingScheduleBo bo, PageQuery pageQuery);

    /**
     * 查询MeetingSchedule列表
     */
    List<SysMeetingScheduleVo> queryList(SysMeetingScheduleBo bo);

    /**
     * 新增MeetingSchedule
     */
    Boolean insertByBo(SysMeetingScheduleBo bo);

    /**
     * 修改MeetingSchedule
     */
    Boolean updateByBo(SysMeetingScheduleBo bo);

    /**
     * 校验并批量删除MeetingSchedule信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
