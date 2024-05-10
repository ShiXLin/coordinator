package com.lanternfish.form.service;

import com.lanternfish.common.core.page.TableDataInfo;
import com.lanternfish.form.domain.bo.FormTemplateDataBo;
import com.lanternfish.common.core.domain.mongodb.BaseQueryForMongo;
import com.lanternfish.form.domain.vo.FormTemplateDataVo;
import org.bson.types.ObjectId;

import java.util.List;
import java.util.Map;

/**
 * @author Liam
 * @date 2024-4-24
 * @apiNote
 */
public interface IFormTemplateService {

    /**
     * 创建表单模板
     *
     * @param formTemplateDataBo 表单模板数据
     * @return 是否创建成功
     */
    Boolean createFormTemplate(FormTemplateDataBo formTemplateDataBo);

    /**
     * 查询表单模板
     *
     * @param baseQueryForMongo 查询条件
     * @return 查询结果
     */
    TableDataInfo<FormTemplateDataVo> queryFormTemplate(BaseQueryForMongo baseQueryForMongo);

    /**
     * 根据id查询表单模板内容
     *
     * @param id 表单模板id
     * @return 表单模板内容
     */
    List<Map<String, Object>> queryFormTemplateById(ObjectId id);

    /**
     * 更新表单模板内容
     *
     * @param objectId           表单模板id
     * @param formTemplateDataBo 表单模板数据
     * @return 是否更新成功
     */
    Boolean updateFormTemplate(ObjectId objectId, FormTemplateDataBo formTemplateDataBo);

    /**
     * 删除表单模板
     *
     * @param objectId 表单模板id
     * @return 是否删除成功
     */
    Boolean deleteFormTemplate(ObjectId objectId);

    /**
     * 更新表单模板状态
     *
     * @param objectId 表单模板id
     * @param status   状态
     * @return 是否更新成功
     */
    Boolean updateFormTemplateStatus(ObjectId objectId, Boolean status);
}
