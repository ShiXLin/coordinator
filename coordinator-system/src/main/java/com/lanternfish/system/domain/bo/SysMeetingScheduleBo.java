package com.lanternfish.system.domain.bo;

import com.lanternfish.common.core.validate.AddGroup;
import com.lanternfish.common.core.validate.EditGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.validation.constraints.*;

import java.util.Date;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.lanternfish.common.core.domain.BaseEntity;

/**
 * MeetingSchedule业务对象 sys_meeting_schedule
 *
 * @author Liam
 * @date 2024-04-22
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class SysMeetingScheduleBo extends BaseEntity {

    /**
     * 会议室ID
     */
    @NotNull(message = "会议室ID不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 会议室id
     */
    @NotNull(message = "会议室id不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long roomId;

    /**
     * 会议开始时间
     */
    @NotNull(message = "会议开始时间不能为空", groups = { AddGroup.class, EditGroup.class })
    private Date startTime;

    /**
     * 会议结束时间
     */
    @NotNull(message = "会议结束时间不能为空", groups = { AddGroup.class, EditGroup.class })
    private Date endTime;

    /**
     * 会议室状态
     */
    @NotNull(message = "会议室状态不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long status;


}
