package com.lanternfish.web.controller.workflow;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;

import com.lanternfish.common.annotation.Log;
import com.lanternfish.common.annotation.RepeatSubmit;
import com.lanternfish.common.core.controller.BaseController;
import com.lanternfish.common.core.domain.PageQuery;
import com.lanternfish.common.core.domain.R;
import com.lanternfish.common.core.page.TableDataInfo;
import com.lanternfish.common.enums.BusinessType;
import com.lanternfish.common.utils.poi.ExcelUtil;
import com.lanternfish.flowable.core.domain.ProcessQuery;
import com.lanternfish.workflow.domain.bo.WfCopyBo;
import com.lanternfish.workflow.domain.bo.WfNewProcessBo;
import com.lanternfish.workflow.domain.vo.*;
import com.lanternfish.workflow.service.IWfCopyService;
import com.lanternfish.workflow.service.IWfProcessService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 工作流流程管理
 *
 * @author KonBAI
 * @createTime 2022/3/24 18:54
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/workflow/process")
public class WfProcessController extends BaseController {

    private final IWfProcessService processService;
    private final IWfCopyService copyService;

    /**
     * 查询可发起流程列表
     *
     * @param pageQuery 分页参数
     */
    @GetMapping(value = "/list")
    @SaCheckPermission("workflow:process:startList")
    public TableDataInfo<WfDefinitionVo> startProcessList(ProcessQuery processQuery, PageQuery pageQuery) {
        return processService.selectPageStartProcessList(processQuery, pageQuery);
    }

    /**
     * 我拥有的流程
     */
    @SaCheckPermission("workflow:process:ownList")
    @GetMapping(value = "/ownList")
    public TableDataInfo<WfTaskVo> ownProcessList(ProcessQuery processQuery, PageQuery pageQuery) {
        return processService.selectPageOwnProcessList(processQuery, pageQuery);
    }

    /**
     * 获取待办列表
     */
    @SaCheckPermission("workflow:process:todoList")
    @GetMapping(value = "/todoList")
    public TableDataInfo<WfTaskVo> todoProcessList(ProcessQuery processQuery, PageQuery pageQuery) {
        return processService.selectPageTodoProcessList(processQuery, pageQuery);
    }

    /**
     * 获取待签列表
     *
     * @param processQuery 流程业务对象
     * @param pageQuery 分页参数
     */
    @SaCheckPermission("workflow:process:claimList")
    @GetMapping(value = "/claimList")
    public TableDataInfo<WfTaskVo> claimProcessList(ProcessQuery processQuery, PageQuery pageQuery) {
        return processService.selectPageClaimProcessList(processQuery, pageQuery);
    }

    /**
     * 获取已办列表
     *
     * @param pageQuery 分页参数
     */
    @SaCheckPermission("workflow:process:finishedList")
    @GetMapping(value = "/finishedList")
    public TableDataInfo<WfTaskVo> finishedProcessList(ProcessQuery processQuery, PageQuery pageQuery) {
        return processService.selectPageFinishedProcessList(processQuery, pageQuery);
    }

    /**
     * 获取抄送列表
     *
     * @param copyBo 流程抄送对象
     * @param pageQuery 分页参数
     */
    @SaCheckPermission("workflow:process:copyList")
    @GetMapping(value = "/copyList")
    public TableDataInfo<WfCopyVo> copyProcessList(WfCopyBo copyBo, PageQuery pageQuery) {
        copyBo.setUserId(getUserId());
        return copyService.selectPageList(copyBo, pageQuery);
    }

    /**
     * 获取我的抄送列表
     *
     * @param copyBo 流程抄送对象
     * @param pageQuery 分页参数
     */
    @SaCheckPermission("workflow:process:myCopyList")
    @GetMapping(value = "/myCopyList")
    public TableDataInfo<WfCopyVo> myCopyProcessList(WfCopyBo copyBo, PageQuery pageQuery) {
        copyBo.setUserId(getUserId());
        return copyService.selectMyPageList(copyBo, pageQuery);
    }

    /**
     *更新抄送状态为已读
     *
     * @param id
     *
     */
    @GetMapping(value = "/updateViewStatust")
    public R<Object> updateViewStatus(@RequestParam(name = "id",required = false) String id) {
        copyService.updateStatus(id);
        return R.ok("更新成功!");
    }


    /**
     * 导出可发起流程列表
     */
    @SaCheckPermission("workflow:process:startExport")
    @Log(title = "可发起流程", businessType = BusinessType.EXPORT)
    @PostMapping("/startExport")
    public void startExport(@Validated ProcessQuery processQuery, HttpServletResponse response) {
        List<WfDefinitionVo> list = processService.selectStartProcessList(processQuery);
        ExcelUtil.exportExcel(list, "可发起流程", WfDefinitionVo.class, response);
    }

    /**
     * 导出我拥有流程列表
     */
    @SaCheckPermission("workflow:process:ownExport")
    @Log(title = "我拥有流程", businessType = BusinessType.EXPORT)
    @PostMapping("/ownExport")
    public void ownExport(@Validated ProcessQuery processQuery, HttpServletResponse response) {
        List<WfTaskVo> list = processService.selectOwnProcessList(processQuery);
        List<WfOwnTaskExportVo> listVo = BeanUtil.copyToList(list, WfOwnTaskExportVo.class);
        for (WfOwnTaskExportVo exportVo : listVo) {
            exportVo.setStatus(ObjectUtil.isNull(exportVo.getFinishTime()) ? "进行中" : "已完成");
        }
        ExcelUtil.exportExcel(listVo, "我拥有流程", WfOwnTaskExportVo.class, response);
    }

    /**
     * 导出待办流程列表
     */
    @SaCheckPermission("workflow:process:todoExport")
    @Log(title = "待办流程", businessType = BusinessType.EXPORT)
    @PostMapping("/todoExport")
    public void todoExport(@Validated ProcessQuery processQuery, HttpServletResponse response) {
        List<WfTaskVo> list = processService.selectTodoProcessList(processQuery);
        List<WfTodoTaskExportVo> listVo = BeanUtil.copyToList(list, WfTodoTaskExportVo.class);
        ExcelUtil.exportExcel(listVo, "待办流程", WfTodoTaskExportVo.class, response);
    }

    /**
     * 导出待签流程列表
     */
    @SaCheckPermission("workflow:process:claimExport")
    @Log(title = "待签流程", businessType = BusinessType.EXPORT)
    @PostMapping("/claimExport")
    public void claimExport(@Validated ProcessQuery processQuery, HttpServletResponse response) {
        List<WfTaskVo> list = processService.selectClaimProcessList(processQuery);
        List<WfClaimTaskExportVo> listVo = BeanUtil.copyToList(list, WfClaimTaskExportVo.class);
        ExcelUtil.exportExcel(listVo, "待签流程", WfClaimTaskExportVo.class, response);
    }

    /**
     * 导出已办流程列表
     */
    @SaCheckPermission("workflow:process:finishedExport")
    @Log(title = "已办流程", businessType = BusinessType.EXPORT)
    @PostMapping("/finishedExport")
    public void finishedExport(@Validated ProcessQuery processQuery, HttpServletResponse response) {
        List<WfTaskVo> list = processService.selectFinishedProcessList(processQuery);
        List<WfFinishedTaskExportVo> listVo = BeanUtil.copyToList(list, WfFinishedTaskExportVo.class);
        ExcelUtil.exportExcel(listVo, "已办流程", WfFinishedTaskExportVo.class, response);
    }

    /**
     * 导出抄送流程列表
     */
    @SaCheckPermission("workflow:process:copyExport")
    @Log(title = "抄送流程", businessType = BusinessType.EXPORT)
    @PostMapping("/copyExport")
    public void copyExport(WfCopyBo copyBo, HttpServletResponse response) {
        copyBo.setUserId(getUserId());
        List<WfCopyVo> list = copyService.selectList(copyBo);
        ExcelUtil.exportExcel(list, "抄送流程", WfCopyVo.class, response);
    }

    /**
     * 查询流程部署关联表单信息
     *
     * @param definitionId 流程定义id
     * @param deployId 流程部署id
     */
    @GetMapping("/getProcessForm")
    @SaCheckPermission("workflow:process:start")
    public R<?> getForm(@RequestParam(value = "definitionId") String definitionId,
                        @RequestParam(value = "deployId") String deployId,
                        @RequestParam(value = "procInsId", required = false) String procInsId) {
        return R.ok(processService.selectFormContent(definitionId, deployId, procInsId));
    }

    /**
     * 根据流程定义id启动流程实例
     *
     * @param processDefId 流程定义id
     * @param variables 变量集合,json对象
     */
    @SaCheckPermission("workflow:process:start")
    @PostMapping("/start/{processDefId}")
    public R<Void> start(@PathVariable(value = "processDefId") String processDefId, @RequestBody Map<String, Object> variables) {
        return processService.startProcessByDefId(processDefId, variables);

    }

    /**
     * 根据业务数据Id和服务名启动流程实例
     *
     * @param dataId, serviceName
     * @param variables 变量集合,json对象
     */
    @SaCheckPermission("workflow:process:start")
    @RepeatSubmit()
    @PostMapping("/startByDataId/{dataId}/{serviceName}")
    public R<Void> startByDataId(@PathVariable(value = "dataId") String dataId,
                                 @PathVariable(value = "serviceName") String serviceName,
                                 @RequestBody Map<String, Object> variables) {
        variables.put("dataId",dataId);
        return processService.startProcessByDataId(dataId, serviceName, variables);

    }

    /**
     * 删除流程实例
     *
     * @param instanceIds 流程实例ID串
     */
    @DeleteMapping("/instance/{instanceIds}")
    public R<Void> delete(@PathVariable String[] instanceIds) {
        processService.deleteProcessByIds(instanceIds);
        return R.ok();
    }

    /**
     * 读取xml文件
     * @param processDefId 流程定义ID
     */
    @GetMapping("/bpmnXml/{processDefId}")
    public R<String> getBpmnXml(@PathVariable(value = "processDefId") String processDefId) {
        return R.ok(null, processService.queryBpmnXmlById(processDefId));
    }

    /**
     * 查询流程详情信息
     *
     * @param procInsId 流程实例ID
     * @param taskId 任务ID
     * @param dataId 业务数据ID
     */
    @GetMapping("/detail")
    public R detail(String procInsId, String taskId, String dataId) {
        return R.ok(processService.queryProcessDetail(procInsId, taskId, dataId));
    }

    /**
     * 查询流程详情信息
     *
     * @param dataId 任务ID
     */
    @GetMapping("/detailbydataid")
    public R detail(String dataId) {
        return R.ok(processService.queryProcessDetailByDataId(dataId));
    }

    /**
     * 查询流程是否结束
     *
     * @param procInsId
     * @param
     */
    @GetMapping("/iscompleted")
    public R processIscompleted(String procInsId) {
        return R.ok(processService.processIscompleted(procInsId));
    }

    /**
     * 根据钉钉流程json转flowable的bpmn的xml格式
     *
     * @param ddjson json对象
     *
     */
    @PostMapping("/ddtobpmn")
    public R<Void> ddToBpmn(@RequestBody String ddjson) {
        return processService.dingdingToBpmn(ddjson);

    }

    /**
     * 测试新的流程模板
     */
    @GetMapping("/newProcessDetail")
    public R newProcessDetail(String procInsId, String taskId, String dataId) {
        return R.ok(processService.newProcessDetail(procInsId, taskId, dataId));
    }

    /**
     * 转交BS流程数据
     */
    @PostMapping("/newProcessFromJson")
    public R newProcessFromJson(WfNewProcessBo newProcess) {
        processService.newProcessFromJson(newProcess);
        return R.ok();
    }
}