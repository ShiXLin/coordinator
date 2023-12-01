package com.ruoyi.web.controller.workflow;

import java.util.List;
import java.util.Arrays;

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
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.workflow.domain.vo.WfDdFlowVo;
import com.ruoyi.workflow.domain.bo.WfDdFlowBo;
import com.ruoyi.workflow.service.IWfDdFlowService;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 钉钉流程
 *
 * @author nbacheng
 * @date 2023-11-29
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/workflow/ddFlow")
public class WfDdFlowController extends BaseController {

    private final IWfDdFlowService iWfDdFlowService;

    /**
     * 查询钉钉流程列表
     */
    @SaCheckPermission("workflow:ddFlow:list")
    @GetMapping("/list")
    public TableDataInfo<WfDdFlowVo> list(WfDdFlowBo bo, PageQuery pageQuery) {
        return iWfDdFlowService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出钉钉流程列表
     */
    @SaCheckPermission("workflow:ddFlow:export")
    @Log(title = "钉钉流程", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(WfDdFlowBo bo, HttpServletResponse response) {
        List<WfDdFlowVo> list = iWfDdFlowService.queryList(bo);
        ExcelUtil.exportExcel(list, "钉钉流程", WfDdFlowVo.class, response);
    }

    /**
     * 获取钉钉流程详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("workflow:ddFlow:query")
    @GetMapping("/{id}")
    public R<WfDdFlowVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(iWfDdFlowService.queryById(id));
    }

    /**
     * 新增钉钉流程
     */
    @SaCheckPermission("workflow:ddFlow:add")
    @Log(title = "钉钉流程", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody WfDdFlowBo bo) {
        return toAjax(iWfDdFlowService.insertByBo(bo));
    }

    /**
     * 修改钉钉流程
     */
    @SaCheckPermission("workflow:ddFlow:edit")
    @Log(title = "钉钉流程", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody WfDdFlowBo bo) {
        return toAjax(iWfDdFlowService.updateByBo(bo));
    }

    /**
     * 删除钉钉流程
     *
     * @param ids 主键串
     */
    @SaCheckPermission("workflow:ddFlow:remove")
    @Log(title = "钉钉流程", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(iWfDdFlowService.deleteWithValidByIds(Arrays.asList(ids), true));
    }
}
