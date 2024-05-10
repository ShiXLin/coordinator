package com.lanternfish.form.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.lanternfish.common.constant.MongoConstants;
import com.lanternfish.common.core.page.TableDataInfo;
import com.lanternfish.common.exception.ServiceException;
import com.lanternfish.form.domain.FormTemplateData;
import com.lanternfish.common.core.domain.mongodb.BaseQueryForMongo;
import com.lanternfish.form.domain.bo.FormTemplateDataBo;
import com.lanternfish.form.domain.vo.FormTemplateDataVo;
import com.lanternfish.form.service.IFormTemplateService;
import com.lanternfish.common.utils.MongoUtil;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author Liam
 * @date 2024-4-24
 * @apiNote
 */
@Service
@RequiredArgsConstructor
public class FormTemplateServiceImpl implements IFormTemplateService {

    private static final Logger log = LoggerFactory.getLogger(FormTemplateServiceImpl.class);



    @Override
    public Boolean createFormTemplate(FormTemplateDataBo formTemplateDataBo) {
        FormTemplateData formTemplateData = BeanUtil.copyProperties(formTemplateDataBo, FormTemplateData.class);
        try {
            MongoUtil.fill(formTemplateData);
            return MongoUtil.save(formTemplateData);
        } catch (Exception e) {
            log.error("表单模板创建失败，失败原因{}", e.getMessage());
            throw new ServiceException("表单模板创建失败");
        }
    }

    @Override
    public TableDataInfo<FormTemplateDataVo> queryFormTemplate(BaseQueryForMongo baseQueryForMongo) {
        return MongoUtil.getMongoTableDataInfo(baseQueryForMongo, FormTemplateData.class, FormTemplateDataVo.class);
    }

    @Override
    public List<Map<String, Object>> queryFormTemplateById(ObjectId id) {
        FormTemplateData formTemplateData = MongoUtil.findById(id, FormTemplateData.class);
        if (ObjectUtil.isNull(formTemplateData) || formTemplateData.getIsDeleted()) {
            throw new ServiceException("表单模板已删除");
        }
        return formTemplateData.getFormTemplateContent();
    }

    @Override
    public Boolean updateFormTemplate(ObjectId objectId, FormTemplateDataBo formTemplateDataBo) {
        FormTemplateData formTemplateData = MongoUtil.findById(objectId, FormTemplateData.class);
        checkDataLock(formTemplateData);
        Query query = MongoUtil.getQueryById(objectId);
        Update update = MongoUtil.getBaseUpdate()
            .set("formTemplateContent", formTemplateDataBo.getFormTemplateContent())
            .set("formTemplateVersion", formTemplateDataBo.getFormTemplateVersion());
        try {
            return  MongoUtil.updateFirst(query, update, FormTemplateData.class);
        }catch (RuntimeException e) {
            log.error("表单模板更新失败，失败原因{}", e.getMessage());
            throw new ServiceException("修改异常");
        }
    }

    @Override
    public Boolean deleteFormTemplate(ObjectId id) {
        FormTemplateData formTemplateData = MongoUtil.findById(id, FormTemplateData.class);
        if (ObjectUtil.isNull(formTemplateData)) {
            throw new ServiceException("表单模板不存在");
        }
        Query query = new Query(Criteria.where(MongoConstants.ID).is(id));
        Update update = new Update().set(MongoConstants.IS_DELETED, true);
        try {
            MongoUtil.updateFirst(query, update, FormTemplateData.class);
            return true;
        } catch (Exception e) {
            log.error("表单模板删除失败，失败原因{}", e.getMessage());
            throw new ServiceException("删除异常");
        }
    }

    @Override
    public Boolean updateFormTemplateStatus(ObjectId objectId, Boolean status) {
        FormTemplateData formTemplateData = MongoUtil.findById(objectId, FormTemplateData.class);
        MongoUtil.checkDataExist(formTemplateData);
        Query query = MongoUtil.getQueryById(objectId);
        Update update = MongoUtil.getBaseUpdate().set("locked", status);
        try {
            MongoUtil.updateFirst(query, update, FormTemplateData.class);
            return true;
        }catch (Exception e) {
            log.error("表单模板更新失败，失败原因{}", e.getMessage());
            throw new ServiceException("修改异常");
        }
    }


    /**
     * 检查数据是否被锁定
     *
     * @param formTemplateData 表单模板数据
     */
    private void checkDataLock(FormTemplateData formTemplateData) {
        MongoUtil.checkDataExist(formTemplateData);
        if (formTemplateData.getLocked()) {
            throw new ServiceException("表单模板已被锁定，无法修改");
        }
    }


}
