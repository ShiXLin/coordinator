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
import com.ruoyi.workflow.domain.vo.WfOperateRuleVo;
import com.ruoyi.workflow.domain.bo.WfOperateRuleBo;
import com.ruoyi.workflow.service.IWfOperateRuleService;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 流程操作规则
 *
 * @author nbacheng
 * @date 2023-11-23
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/workflow/operateRule")
public class WfOperateRuleController extends BaseController {

    private final IWfOperateRuleService iWfOperateRuleService;

    /**
     * 查询流程操作规则列表
     */
    @SaCheckPermission("workflow:operateRule:list")
    @GetMapping("/list")
    public TableDataInfo<WfOperateRuleVo> list(WfOperateRuleBo bo, PageQuery pageQuery) {
        return iWfOperateRuleService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出流程操作规则列表
     */
    @SaCheckPermission("workflow:operateRule:export")
    @Log(title = "流程操作规则", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(WfOperateRuleBo bo, HttpServletResponse response) {
        List<WfOperateRuleVo> list = iWfOperateRuleService.queryList(bo);
        ExcelUtil.exportExcel(list, "流程操作规则", WfOperateRuleVo.class, response);
    }

    /**
     * 获取流程操作规则详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("workflow:operateRule:query")
    @GetMapping("/{id}")
    public R<WfOperateRuleVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(iWfOperateRuleService.queryById(id));
    }

    /**
     * 新增流程操作规则
     */
    @SaCheckPermission("workflow:operateRule:add")
    @Log(title = "流程操作规则", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody WfOperateRuleBo bo) {
        return toAjax(iWfOperateRuleService.insertByBo(bo));
    }

    /**
     * 修改流程操作规则
     */
    @SaCheckPermission("workflow:operateRule:edit")
    @Log(title = "流程操作规则", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody WfOperateRuleBo bo) {
        return toAjax(iWfOperateRuleService.updateByBo(bo));
    }

    /**
     * 删除流程操作规则
     *
     * @param ids 主键串
     */
    @SaCheckPermission("workflow:operateRule:remove")
    @Log(title = "流程操作规则", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(iWfOperateRuleService.deleteWithValidByIds(Arrays.asList(ids), true));
    }
}
