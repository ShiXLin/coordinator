package com.lanternfish.web.controller.form;

import com.lanternfish.common.core.controller.BaseController;
import com.lanternfish.common.core.domain.R;
import com.lanternfish.common.core.page.TableDataInfo;
import com.lanternfish.common.core.domain.mongodb.BaseQueryForMongo;
import com.lanternfish.form.domain.bo.FormInstanceDataBo;
import com.lanternfish.form.domain.vo.FormInstanceDataVo;
import com.lanternfish.form.service.IFormInstanceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author Liam
 * @date 2024-4-25
 * @apiNote
 */
@RestController
@RequestMapping("/form/fromInstance")
@RequiredArgsConstructor
@Validated
@Tag(name = "表单实例信息相关操作", description = "表单实例信息相关操作")
public class FormInstanceController extends BaseController {

    private final IFormInstanceService formInstanceService;

    @PostMapping()
    @Operation(summary = "创建表单实例", description = "创建表单模板")
    public R<Boolean> createFormInstance(@RequestBody FormInstanceDataBo formInstanceDataBo) {
        return R.ok(formInstanceService.createFormInstance(formInstanceDataBo));
    }

    @PostMapping("/query")
    @Operation(summary = "查询表单实例", description = "查询表单实例")
    public R<TableDataInfo<FormInstanceDataVo>> queryFormInstance(@RequestBody BaseQueryForMongo baseQueryForMongo) {
        return R.ok(formInstanceService.queryFormInstance(baseQueryForMongo));
    }

    @GetMapping("/{id}")
    @Operation(summary = "查询表单实例详情", description = "查询表单实例详情")
    public R<Map<String, Object>> queryFormInstanceById(@PathVariable String id) {
        return R.ok(formInstanceService.queryFormInstanceById(new ObjectId(id)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新表单实例", description = "更新表单实例")
    public R<Boolean> updateFormInstance(@PathVariable String id, @RequestBody FormInstanceDataBo formInstanceDataBo) {
        return R.ok(formInstanceService.updateFormInstance(new ObjectId(id), formInstanceDataBo));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除表单实例", description = "删除表单实例")
    public R<Boolean> deleteFormInstance(@PathVariable String id) {
        return R.ok(formInstanceService.deleteFormInstance(new ObjectId(id)));
    }

    @PutMapping("/updateStatus/{id}")
    @Operation(summary = "更新表单实例状态", description = "更新表单实例状态")
    public R<Boolean> updateFormInstanceStatus(@PathVariable String id, @RequestParam("status") Boolean status) {
        return R.ok(formInstanceService.updateFormInstanceStatus(new ObjectId(id), status));
    }
}
