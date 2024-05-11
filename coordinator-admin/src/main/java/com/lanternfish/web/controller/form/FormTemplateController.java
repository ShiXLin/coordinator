package com.lanternfish.web.controller.form;

import com.lanternfish.common.core.controller.BaseController;
import com.lanternfish.common.core.domain.R;
import com.lanternfish.common.core.page.TableDataInfo;
import com.lanternfish.form.domain.bo.FormTemplateDataBo;
import com.lanternfish.common.core.domain.mongodb.BaseQueryForMongo;
import com.lanternfish.form.domain.vo.FormTemplateDataVo;
import com.lanternfish.form.service.IFormTemplateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author Liam
 * @date 2024-4-24
 * @apiNote
 */
@RestController
@RequestMapping("/form/fromTemplate")
@RequiredArgsConstructor
@Validated
@Tag(name = "表单相关操作", description = "表单相关操作")
public class FormTemplateController  extends BaseController {

    private final IFormTemplateService formTemplateService;


    @PostMapping()
    @Operation(summary = "创建表单模板", description = "创建表单模板")
    public R<Boolean> createFormTemplate(@RequestBody FormTemplateDataBo formTemplateDataBo) {
        return R.ok(formTemplateService.createFormTemplate(formTemplateDataBo));
    }

    @PostMapping("/query")
    @Operation(summary = "查询表单模板", description = "查询表单模板")
    public TableDataInfo<FormTemplateDataVo> queryFormTemplate(@RequestBody BaseQueryForMongo baseQueryForMongo) {
        return formTemplateService.queryFormTemplate(baseQueryForMongo);
    }

    @GetMapping("/{id}")
    @Operation(summary = "查询表单模板详情", description = "查询表单模板详情")
    public R<List<Map<String, Object>>> queryFormTemplateById(@PathVariable String id) {
        return R.ok(formTemplateService.queryFormTemplateById(new ObjectId(id)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新表单模板", description = "更新表单模板")
    public R<Boolean> updateFormTemplate(@PathVariable String id, @RequestBody FormTemplateDataBo formTemplateDataBo) {
        return R.ok(formTemplateService.updateFormTemplate(new ObjectId(id), formTemplateDataBo));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除表单模板", description = "删除表单模板")
    public R<Boolean> deleteFormTemplate(@PathVariable String id) {
        return R.ok(formTemplateService.deleteFormTemplate(new ObjectId(id)));
    }

    @PutMapping("/updateStatus/{id}")
    @Operation(summary = "更新表单模板状态", description = "更新表单模板状态")
    public R<Boolean> updateFormTemplateStatus(@PathVariable String id, @RequestParam("status") Boolean status) {
        return R.ok(formTemplateService.updateFormTemplateStatus(new ObjectId(id), status));
    }

}
