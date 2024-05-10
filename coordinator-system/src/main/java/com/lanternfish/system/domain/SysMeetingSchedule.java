package com.lanternfish.system.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serializable;
import java.util.Date;
import java.math.BigDecimal;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.lanternfish.common.core.domain.BaseEntity;

/**
 * MeetingSchedule对象 sys_meeting_schedule
 *
 * @author Liam
 * @date 2024-04-22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_meeting_schedule")
public class SysMeetingSchedule extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * 会议室ID
     */
    @TableId(value = "id",type= IdType.ASSIGN_ID)
    private Long id;
    /**
     * 会议室id
     */
    private Long roomId;
    /**
     * 会议开始时间
     */
    private Date startTime;
    /**
     * 会议结束时间
     */
    private Date endTime;
    /**
     * 会议室状态
     */
    private Long status;

}
