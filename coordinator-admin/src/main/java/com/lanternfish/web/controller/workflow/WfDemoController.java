package com.lanternfish.web.controller.workflow;

import java.util.List;
import java.util.Map;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.*;
import cn.dev33.satoken.annotation.SaCheckPermission;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;
import com.lanternfish.common.annotation.RepeatSubmit;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lanternfish.common.annotation.Log;
import com.lanternfish.common.core.controller.BaseController;
import com.lanternfish.common.core.domain.PageQuery;
import com.lanternfish.common.core.domain.R;
import com.lanternfish.common.core.validate.AddGroup;
import com.lanternfish.common.core.validate.EditGroup;
import com.lanternfish.common.core.page.TableDataInfo;
import com.lanternfish.common.enums.BusinessType;
import com.lanternfish.common.utils.poi.ExcelUtil;
import com.lanternfish.system.query.QueryGenerator;
import com.lanternfish.workflow.domain.vo.WfDemoVo;
import com.lanternfish.workflow.domain.bo.WfDemoBo;
import com.lanternfish.workflow.service.IWfDemoService;

/**
 * DEMO
 *
 * @author Liam
 * @date 2023-10-12
 */
@Validated
@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/workflow/demo")
public class WfDemoController extends BaseController {

    private final IWfDemoService iWfDemoService;

    /**
     * 查询DEMO列表
     */
    @SaCheckPermission("workflow:demo:list")
    @GetMapping("/list")
    public TableDataInfo<WfDemoVo> list(WfDemoVo vo, PageQuery pageQuery, HttpServletRequest req) {
    	//by nbacheng for java.sql.SQLSyntaxErrorException: ORA-00918: 未明确定义列
		Map<String, String[]> ParameterMap = new HashMap<String, String[]>(req.getParameterMap());
		String[] column = new String[]{""};
		if(ParameterMap!=null&&  ParameterMap.containsKey("column")) {
			column[0] = ParameterMap.get("column")[0];
			column[0] = "t."+ column[0];
			ParameterMap.replace("column", column);
			log.info("修改的排序规则>>列:" + ParameterMap.get("column")[0]);
		}
		QueryWrapper<WfDemoVo> queryWrapper = QueryGenerator.initQueryWrapper(vo, ParameterMap);

		Page<WfDemoVo> page = new Page<WfDemoVo>(pageQuery.getPageNum(), pageQuery.getPageSize());
	    Page<WfDemoVo> result = iWfDemoService.myPage(page, queryWrapper);
	    return TableDataInfo.build(result);
    }

    /**
     * 导出DEMO列表
     */
    @SaCheckPermission("workflow:demo:export")
    @Log(title = "DEMO", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(WfDemoBo bo, HttpServletResponse response) {
        List<WfDemoVo> list = iWfDemoService.queryList(bo);
        ExcelUtil.exportExcel(list, "DEMO", WfDemoVo.class, response);
    }

    /**
     * 获取DEMO详细信息
     *
     * @param demoId 主键
     */
    @SaCheckPermission("workflow:demo:query")
    @GetMapping("/{demoId}")
    public R<WfDemoVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long demoId) {
        return R.ok(iWfDemoService.queryById(demoId));
    }

    /**
     * 新增DEMO
     */
    @SaCheckPermission("workflow:demo:add")
    @Log(title = "DEMO", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody WfDemoBo bo) {
        return toAjax(iWfDemoService.insertByBo(bo));
    }

    /**
     * 修改DEMO
     */
    @SaCheckPermission("workflow:demo:edit")
    @Log(title = "DEMO", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody WfDemoBo bo) {
        return toAjax(iWfDemoService.updateByBo(bo));
    }

    /**
     * 删除DEMO
     *
     * @param demoIds 主键串
     */
    @SaCheckPermission("workflow:demo:remove")
    @Log(title = "DEMO", businessType = BusinessType.DELETE)
    @DeleteMapping("/{demoIds}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] demoIds) {
        return toAjax(iWfDemoService.deleteWithValidByIds(Arrays.asList(demoIds), true));
    }
}
