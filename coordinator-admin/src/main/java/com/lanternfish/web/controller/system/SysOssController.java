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
import com.lanternfish.system.domain.vo.SysOssVo;
import com.lanternfish.system.domain.bo.SysOssBo;
import com.lanternfish.system.service.ISysOssService;
import com.lanternfish.common.core.page.TableDataInfo;

/**
 * OSS对象存储
 *
 * @author Liam
 * @date 2024-04-23
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/system/oss")
public class SysOssController extends BaseController {

    private final ISysOssService iSysOssService;

    /**
     * 查询OSS对象存储列表
     */
    @SaCheckPermission("system:oss:list")
    @GetMapping("/list")
    public TableDataInfo<SysOssVo> list(SysOssBo bo, PageQuery pageQuery) {
        return iSysOssService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出OSS对象存储列表
     */
    @SaCheckPermission("system:oss:export")
    @Log(title = "OSS对象存储", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(SysOssBo bo, HttpServletResponse response) {
        List<SysOssVo> list = iSysOssService.queryList(bo);
        ExcelUtil.exportExcel(list, "OSS对象存储", SysOssVo.class, response);
    }

    /**
     * 获取OSS对象存储详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("system:oss:query")
    @GetMapping("/{id}")
    public R<SysOssVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(iSysOssService.queryById(id));
    }

    /**
     * 新增OSS对象存储
     */
    @SaCheckPermission("system:oss:add")
    @Log(title = "OSS对象存储", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody SysOssBo bo) {
        return toAjax(iSysOssService.insertByBo(bo));
    }

    /**
     * 修改OSS对象存储
     */
    @SaCheckPermission("system:oss:edit")
    @Log(title = "OSS对象存储", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody SysOssBo bo) {
        return toAjax(iSysOssService.updateByBo(bo));
    }

    /**
     * 删除OSS对象存储
     *
     * @param ids 主键串
     */
    @SaCheckPermission("system:oss:remove")
    @Log(title = "OSS对象存储", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(iSysOssService.deleteWithValidByIds(Arrays.asList(ids), true));
    }
}
