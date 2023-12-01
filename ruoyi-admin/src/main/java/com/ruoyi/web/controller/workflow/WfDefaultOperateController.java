package com.ruoyi.web.controller.workflow;

import java.util.List;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import lombok.RequiredArgsConstructor;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.*;
import cn.dev33.satoken.annotation.SaCheckPermission;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;
import com.ruoyi.common.annotation.RepeatSubmit;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.PageQuery;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.validate.AddGroup;
import com.ruoyi.common.core.validate.EditGroup;
import com.ruoyi.common.core.validate.QueryGroup;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.workflow.domain.vo.WfDefaultOperateVo;
import com.ruoyi.workflow.domain.bo.WfDefaultOperateBo;
import com.ruoyi.workflow.service.IWfDefaultOperateService;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 流程默认操作
 *
 * @author nbacheng
 * @date 2023-11-23
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/workflow/defaultOperate")
public class WfDefaultOperateController extends BaseController {

    private final IWfDefaultOperateService iWfDefaultOperateService;

    /**
     * 查询流程默认操作列表
     */
    @SaCheckPermission("workflow:defaultOperate:list")
    @GetMapping("/list")
    public TableDataInfo<WfDefaultOperateVo> list(WfDefaultOperateBo bo, PageQuery pageQuery) {
        return iWfDefaultOperateService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出流程默认操作列表
     */
    @SaCheckPermission("workflow:defaultOperate:export")
    @Log(title = "流程默认操作", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(WfDefaultOperateBo bo, HttpServletResponse response) {
        List<WfDefaultOperateVo> list = iWfDefaultOperateService.queryList(bo);
        ExcelUtil.exportExcel(list, "流程默认操作", WfDefaultOperateVo.class, response);
    }

    /**
     * 获取流程默认操作详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("workflow:defaultOperate:query")
    @GetMapping("/{id}")
    public R<WfDefaultOperateVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(iWfDefaultOperateService.queryById(id));
    }

    /**
     * 新增流程默认操作
     */
    @SaCheckPermission("workflow:defaultOperate:add")
    @Log(title = "流程默认操作", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody WfDefaultOperateBo bo) {
        return toAjax(iWfDefaultOperateService.insertByBo(bo));
    }

    /**
     * 修改流程默认操作
     */
    @SaCheckPermission("workflow:defaultOperate:edit")
    @Log(title = "流程默认操作", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody WfDefaultOperateBo bo) {
        return toAjax(iWfDefaultOperateService.updateByBo(bo));
    }

    /**
     * 删除流程默认操作
     *
     * @param ids 主键串
     */
    @SaCheckPermission("workflow:defaultOperate:remove")
    @Log(title = "流程默认操作", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(iWfDefaultOperateService.deleteWithValidByIds(Arrays.asList(ids), true));
    }
}
