package com.lanternfish.system.domain.vo;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.lanternfish.common.annotation.ExcelDictFormat;
import com.lanternfish.common.convert.ExcelDictConvert;
import lombok.Data;
import java.util.Date;



/**
 * MeetingSchedule视图对象 sys_meeting_schedule
 *
 * @author Liam
 * @date 2024-04-22
 */
@Data
@ExcelIgnoreUnannotated
public class SysMeetingScheduleVo {

    private static final long serialVersionUID = 1L;

    /**
     * 会议室ID
     */
    @ExcelProperty(value = "会议室ID")
    private Long id;

    /**
     * 会议室id
     */
    @ExcelProperty(value = "会议室id")
    private Long roomId;

    /**
     * 会议开始时间
     */
    @ExcelProperty(value = "会议开始时间")
    private Date startTime;

    /**
     * 会议结束时间
     */
    @ExcelProperty(value = "会议结束时间")
    private Date endTime;

    /**
     * 会议室状态
     */
    @ExcelProperty(value = "会议室状态")
    private Long status;


}
