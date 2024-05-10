package com.lanternfish.web.controller.system;

import java.util.List;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import lombok.RequiredArgsConstructor;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.*;
import cn.dev33.satoken.annotation.SaCheckPermission;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;
import com.lanternfish.common.annotation.RepeatSubmit;
import com.lanternfish.common.annotation.Log;
import com.lanternfish.common.core.controller.BaseController;
import com.lanternfish.common.core.domain.PageQuery;
import com.lanternfish.common.core.domain.R;
import com.lanternfish.common.core.validate.AddGroup;
import com.lanternfish.common.core.validate.EditGroup;
import com.lanternfish.common.core.validate.QueryGroup;
import com.lanternfish.common.enums.BusinessType;
import com.lanternfish.common.utils.poi.ExcelUtil;
import com.lanternfish.system.domain.vo.SysMeetingScheduleVo;
import com.lanternfish.system.domain.bo.SysMeetingScheduleBo;
import com.lanternfish.system.service.ISysMeetingScheduleService;
import com.lanternfish.common.core.page.TableDataInfo;

/**
 * MeetingSchedule
 *
 * @author Liam
 * @date 2024-04-22
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/system/meetingSchedule")
public class SysMeetingScheduleController extends BaseController {

    private final ISysMeetingScheduleService iSysMeetingScheduleService;

    /**
     * 查询MeetingSchedule列表
     */
    @SaCheckPermission("system:meetingSchedule:list")
    @GetMapping("/list")
    public TableDataInfo<SysMeetingScheduleVo> list(SysMeetingScheduleBo bo, PageQuery pageQuery) {
        return iSysMeetingScheduleService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出MeetingSchedule列表
     */
    @SaCheckPermission("system:meetingSchedule:export")
    @Log(title = "MeetingSchedule", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(SysMeetingScheduleBo bo, HttpServletResponse response) {
        List<SysMeetingScheduleVo> list = iSysMeetingScheduleService.queryList(bo);
        ExcelUtil.exportExcel(list, "MeetingSchedule", SysMeetingScheduleVo.class, response);
    }

    /**
     * 获取MeetingSchedule详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("system:meetingSchedule:query")
    @GetMapping("/{id}")
    public R<SysMeetingScheduleVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(iSysMeetingScheduleService.queryById(id));
    }

    /**
     * 新增MeetingSchedule
     */
    @SaCheckPermission("system:meetingSchedule:add")
    @Log(title = "MeetingSchedule", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody SysMeetingScheduleBo bo) {
        return toAjax(iSysMeetingScheduleService.insertByBo(bo));
    }

    /**
     * 修改MeetingSchedule
     */
    @SaCheckPermission("system:meetingSchedule:edit")
    @Log(title = "MeetingSchedule", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody SysMeetingScheduleBo bo) {
        return toAjax(iSysMeetingScheduleService.updateByBo(bo));
    }

    /**
     * 删除MeetingSchedule
     *
     * @param ids 主键串
     */
    @SaCheckPermission("system:meetingSchedule:remove")
    @Log(title = "MeetingSchedule", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(iSysMeetingScheduleService.deleteWithValidByIds(Arrays.asList(ids), true));
    }


}
