package com.lanternfish.form.service;

import com.lanternfish.common.core.page.TableDataInfo;
import com.lanternfish.common.core.domain.mongodb.BaseQueryForMongo;
import com.lanternfish.form.domain.bo.FormInstanceDataBo;
import com.lanternfish.form.domain.vo.FormInstanceDataVo;
import org.bson.types.ObjectId;

import java.util.Map;

/**
 * @author Liam
 * @date 2024-4-25
 * @apiNote
 */
public interface IFormInstanceService {
    /**
     * 创建表单实例
     *
     * @param formInstanceDataBo 表单实例数据
     * @return 是否创建成功
     */
    Boolean createFormInstance(FormInstanceDataBo formInstanceDataBo);

    /**
     * 查询表单实例
     *
     * @param baseQueryForMongo 查询条件
     * @return 表单实例列表
     */
    TableDataInfo<FormInstanceDataVo> queryFormInstance(BaseQueryForMongo baseQueryForMongo);

    /**
     * 根据id查询表单实例内容
     *
     * @param objectId 表单实例id
     * @return 表单实例数据内容
     */
    Map<String, Object> queryFormInstanceById(ObjectId objectId);

    /**
     * 更新表单实例内容
     *
     * @param objectId            表单实例id
     * @param formInstanceDataBo 表单实例数据
     * @return 是否更新成功
     */
    Boolean updateFormInstance(ObjectId objectId, FormInstanceDataBo formInstanceDataBo);

    /**
     * 删除表单实例
     *
     * @param objectId 表单实例id
     * @return 是否删除成功
     */
    Boolean deleteFormInstance(ObjectId objectId);

    /**
     * 更新表单实例状态
     *
     * @param objectId 表单实例id
     * @param status   状态
     * @return 是否更新成功
     */
    Boolean updateFormInstanceStatus(ObjectId objectId, Boolean status);
}
