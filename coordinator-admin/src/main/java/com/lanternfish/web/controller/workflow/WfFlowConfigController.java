package com.lanternfish.web.controller.workflow;

import java.util.List;
import java.util.Arrays;

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
import com.lanternfish.common.enums.BusinessType;
import com.lanternfish.common.utils.poi.ExcelUtil;
import com.lanternfish.workflow.domain.vo.WfFlowConfigVo;
import com.lanternfish.workflow.domain.vo.WfRuleVo;
import com.lanternfish.workflow.domain.bo.WfFlowConfigBo;
import com.lanternfish.workflow.service.IWfFlowConfigService;
import com.lanternfish.common.core.page.TableDataInfo;

/**
 * 流程配置
 *
 * @author Liam
 * @date 2023-11-19
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/workflow/flowConfig")
public class WfFlowConfigController extends BaseController {

    private final IWfFlowConfigService iWfFlowConfigService;

    /**
     * 查询流程配置列表
     */
    @SaCheckPermission("workflow:flowConfig:list")
    @GetMapping("/list")
    public TableDataInfo<WfFlowConfigVo> list(WfFlowConfigBo bo, PageQuery pageQuery) {
        return iWfFlowConfigService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出流程配置列表
     */
    @SaCheckPermission("workflow:flowConfig:export")
    @Log(title = "流程配置主", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(WfFlowConfigBo bo, HttpServletResponse response) {
        List<WfFlowConfigVo> list = iWfFlowConfigService.queryList(bo);
        ExcelUtil.exportExcel(list, "流程配置主", WfFlowConfigVo.class, response);
    }

    /**
     * 获取流程配置详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("workflow:flowConfig:query")
    @GetMapping("/{id}")
    public R<WfFlowConfigVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(iWfFlowConfigService.queryById(id));
    }

    /**
     * 根据configId获取流程配置两个子表
     *
     * @param id 主键
     */
    @SaCheckPermission("workflow:flowConfig:query")
    @GetMapping("/getConfigRule")
    public R<WfRuleVo> getConfigRule(WfFlowConfigBo bo) {
        return R.ok(iWfFlowConfigService.queryConfigRule(bo));
    }

    /**
     * 修改流程配置两个子表
     */
    @SaCheckPermission("workflow:flowConfig:edit")
    @Log(title = "更新流程配置两个子表", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PostMapping("/editConfigRule")
    public R<Void> editConfigRule(@RequestBody WfRuleVo vo) {
        return toAjax(iWfFlowConfigService.updateConfigRule(vo));
    }

    /**
     * 新增流程配置
     */
    @SaCheckPermission("workflow:flowConfig:add")
    @Log(title = "流程配置主", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody WfFlowConfigBo bo) {
        return toAjax(iWfFlowConfigService.insertByBo(bo));
    }

    /**
     * 修改流程配置
     */
    @SaCheckPermission("workflow:flowConfig:edit")
    @Log(title = "流程配置主", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody WfFlowConfigBo bo) {
        return toAjax(iWfFlowConfigService.updateByBo(bo));
    }

    /**
     * 删除流程配置
     *
     * @param ids 主键串
     */
    @SaCheckPermission("workflow:flowConfig:remove")
    @Log(title = "流程配置主", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(iWfFlowConfigService.deleteWithValidByIds(Arrays.asList(ids), true));
    }
}
