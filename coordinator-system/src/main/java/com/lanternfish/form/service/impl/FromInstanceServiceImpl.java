package com.lanternfish.form.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.lanternfish.common.core.page.TableDataInfo;
import com.lanternfish.common.exception.ServiceException;
import com.lanternfish.form.domain.FormInstanceData;
import com.lanternfish.common.core.domain.mongodb.BaseQueryForMongo;
import com.lanternfish.form.domain.bo.FormInstanceDataBo;
import com.lanternfish.form.domain.vo.FormInstanceDataVo;
import com.lanternfish.form.service.IFormInstanceService;
import com.lanternfish.common.utils.MongoUtil;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author Liam
 * @date 2024-4-25
 * @apiNote
 */
@Service
@RequiredArgsConstructor
public class FromInstanceServiceImpl implements IFormInstanceService {
    private static final Logger log = LoggerFactory.getLogger(FromInstanceServiceImpl.class);


    @Override
    public Boolean createFormInstance(FormInstanceDataBo formInstanceDataBo) {
        FormInstanceData formInstanceData = BeanUtil.copyProperties(formInstanceDataBo, FormInstanceData.class);
        try {
            MongoUtil.fill(formInstanceData);
            return MongoUtil.save(formInstanceData);
        } catch (Exception e) {
            log.error("表单实例创建失败，失败原因{}", e.getMessage());
            throw new ServiceException("表单实例创建失败，请联系管理员");
        }
    }

    @Override
    public TableDataInfo<FormInstanceDataVo> queryFormInstance(BaseQueryForMongo baseQueryForMongo) {
        return MongoUtil.getMongoTableDataInfo(baseQueryForMongo, FormInstanceData.class, FormInstanceDataVo.class);
    }

    @Override
    public Map<String, Object> queryFormInstanceById(ObjectId objectId) {
        FormInstanceData formInstanceData = MongoUtil.findById(objectId, FormInstanceData.class);

        if (ObjectUtil.isNull(formInstanceData) || formInstanceData.getIsDeleted()) {
            throw new ServiceException("表单模板已删除");
        }
        return formInstanceData.getFormData();
    }

    @Override
    public Boolean updateFormInstance(ObjectId objectId, FormInstanceDataBo formInstanceDataBo) {
        FormInstanceData formInstanceData = MongoUtil.findById(objectId, FormInstanceData.class);
        MongoUtil.checkDataExist(formInstanceData);

        try {
            Query query = MongoUtil.getQueryById(objectId);
            Update update = MongoUtil.getBaseUpdate()
                .set("formTemplateContent", formInstanceDataBo.getFormData());
            MongoUtil.updateFirst(query, update, FormInstanceData.class);
            return true;
        }catch (Exception e) {
            log.error("表单模板更新失败，失败原因{}", e.getMessage());
            throw new ServiceException("修改异常");
        }
    }

    @Override
    public Boolean deleteFormInstance(ObjectId objectId) {
        return null;
    }

    @Override
    public Boolean updateFormInstanceStatus(ObjectId objectId, Boolean status) {
        return null;
    }
}
