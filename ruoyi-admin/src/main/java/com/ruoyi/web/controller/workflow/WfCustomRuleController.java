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
import com.ruoyi.workflow.domain.vo.WfCustomRuleVo;
import com.ruoyi.workflow.domain.bo.WfCustomRuleBo;
import com.ruoyi.workflow.service.IWfCustomRuleService;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 流程自定义业务规则
 *
 * @author nbacheng
 * @date 2023-11-23
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/workflow/customRule")
public class WfCustomRuleController extends BaseController {

    private final IWfCustomRuleService iWfCustomRuleService;

    /**
     * 查询流程自定义业务规则列表
     */
    @SaCheckPermission("workflow:customRule:list")
    @GetMapping("/list")
    public TableDataInfo<WfCustomRuleVo> list(WfCustomRuleBo bo, PageQuery pageQuery) {
        return iWfCustomRuleService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出流程自定义业务规则列表
     */
    @SaCheckPermission("workflow:customRule:export")
    @Log(title = "流程自定义业务规则", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(WfCustomRuleBo bo, HttpServletResponse response) {
        List<WfCustomRuleVo> list = iWfCustomRuleService.queryList(bo);
        ExcelUtil.exportExcel(list, "流程自定义业务规则", WfCustomRuleVo.class, response);
    }

    /**
     * 获取流程自定义业务规则详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("workflow:customRule:query")
    @GetMapping("/{id}")
    public R<WfCustomRuleVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(iWfCustomRuleService.queryById(id));
    }

    /**
     * 新增流程自定义业务规则
     */
    @SaCheckPermission("workflow:customRule:add")
    @Log(title = "流程自定义业务规则", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody WfCustomRuleBo bo) {
        return toAjax(iWfCustomRuleService.insertByBo(bo));
    }

    /**
     * 修改流程自定义业务规则
     */
    @SaCheckPermission("workflow:customRule:edit")
    @Log(title = "流程自定义业务规则", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody WfCustomRuleBo bo) {
        return toAjax(iWfCustomRuleService.updateByBo(bo));
    }

    /**
     * 删除流程自定义业务规则
     *
     * @param ids 主键串
     */
    @SaCheckPermission("workflow:customRule:remove")
    @Log(title = "流程自定义业务规则", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(iWfCustomRuleService.deleteWithValidByIds(Arrays.asList(ids), true));
    }
}
