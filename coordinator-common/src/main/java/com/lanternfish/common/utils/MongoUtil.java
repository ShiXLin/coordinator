package com.lanternfish.common.utils;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import com.lanternfish.common.constant.MongoConstants;
import com.lanternfish.common.core.domain.mongodb.BaseMongoData;
import com.lanternfish.common.core.domain.mongodb.BaseQueryForMongo;
import com.lanternfish.common.core.page.TableDataInfo;
import com.lanternfish.common.exception.ServiceException;
import com.lanternfish.common.helper.LoginHelper;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Liam
 * @date 2024-4-24
 * @apiNote 使用模板方法的模式 封装mongo操作 k: mongo表名
 * v: vo类
 */
@Component
public class MongoUtil {

    private static MongoTemplate mongoTemplate;
    private static final Logger log = LoggerFactory.getLogger(MongoUtil.class);

    @Autowired
    public MongoUtil(MongoTemplate mongoTemplate) {
        MongoUtil.mongoTemplate = mongoTemplate;
    }


    /**
     * 查询mongo数据
     *
     * @param baseQueryForMongo  查询条件
     * @param mongoClass mongo表名
     * @param voClass    vo类
     * @return 返回分页数据
     */
    public static <k, v> TableDataInfo<v> getMongoTableDataInfo(BaseQueryForMongo baseQueryForMongo, Class<k> mongoClass, Class<v> voClass) {
        TableDataInfo<v> tableDataInfo = TableDataInfo.build();
        try {
            Criteria criteria = new Criteria();
            if (!mongoTemplate.collectionExists(mongoClass)) {
                throw new ServiceException("数据表不存在");
            }
            criteria.and(MongoConstants.IS_DELETED).is(false);

            baseQueryForMongo.build(criteria);

            Query query = new Query(criteria);

            long count = mongoTemplate.count(query, mongoClass);
            tableDataInfo.setTotal(count);

            if (MongoConstants.ORDER_BY_TYPE_ASC.equals(baseQueryForMongo.getSortOrder())) {
                query.with(Sort.by(Sort.Direction.ASC, baseQueryForMongo.getSortColumn()));
            } else if (MongoConstants.ORDER_BY_TYPE_DESC.equals(baseQueryForMongo.getSortOrder())) {
                query.with(Sort.by(Sort.Direction.DESC, baseQueryForMongo.getSortColumn()));
            }
            PageRequest pageable = PageRequest.of(baseQueryForMongo.getPageNum() - 1, baseQueryForMongo.getPageSize());
            query.with(pageable);
            List<k> mongoDataList = mongoTemplate.find(query, mongoClass);

            List<v> tableDataList = mongoDataList.stream()
                .map(mongoData -> BeanUtil.copyProperties(mongoData, voClass))
                .collect(Collectors.toList());
            tableDataInfo.setRows(tableDataList);

        } catch (Exception e) {
            log.error("查询失败，失败原因{}", e.getMessage());
            throw new ServiceException("查询失败");
        }
        return tableDataInfo;
    }

    /**
     * 根据id查询mongo对象
     *
     * @param objectId mongo对象id
     * @return mongo对象
     */
    public static Query getQueryById(ObjectId objectId) {
        return new Query(Criteria.where(MongoConstants.ID).is(objectId));
    }

    /**
     * 获取mongo更新对象
     *
     * @return mongo更新对象
     */
    public static Update getBaseUpdate() {
        return new Update()
            .set(MongoConstants.UPDATE_USER, LoginHelper.getUsername())
            .set(MongoConstants.UPDATE_TIME, LocalDateTime.now());
    }

    /**
     * 填充mongo对象数据
     *
     * @param mongoData mongo对象
     */
    public static void fill(BaseMongoData mongoData) {
        Long userId = LoginHelper.getUserId();
        if (ObjectUtil.isNull(mongoData)) {
            throw new ServiceException("数据不能为空");
        }
        Long createUser = mongoData.getCreateUser();
        if (ObjectUtil.isNull(createUser)) {
            mongoData.setCreateUser(userId);
            mongoData.setCreateTime(LocalDateTime.now());
        }
        mongoData.setUpdateUser(userId);
        mongoData.setUpdateTime(LocalDateTime.now());
    }

    /**
     * 检查数据是否存在
     *
     * @param mongoData mongo对象
     */
    public static void checkDataExist(BaseMongoData mongoData) {
        if (ObjectUtil.isNull(mongoData)) {
            throw new ServiceException("数据不存在");
        }
        if (mongoData.getIsDeleted()) {
            throw new ServiceException("数据已删除");
        }
    }

    /**
     * 保存mongo对象
     *
     * @param query      查询条件
     * @param update     更新条件
     * @param mongoClass mongo表名
     * @return 保存成功返回true，否则抛出异常
     */
    public static <k>  Boolean updateFirst(Query query, Update update, Class<k> mongoClass) {
        mongoTemplate.updateFirst(query, update, mongoClass);
        return true;
    }

    /**
     * 根据id查询mongo对象
     *
     * @param objectId   mongo对象id
     * @param mongoClass mongo映射类
     * @return mongo对象
     */
    public static <k> k findById(ObjectId objectId, Class<k> mongoClass) {
        return mongoTemplate.findById(objectId, mongoClass);
    }

    /**
     * 保存mongo数据
     *
     * @param mongoData mongo数据
     *
     * @return 保存成功返回true，否则抛出异常
     */
    public static <k> Boolean save(k mongoData) {
        mongoTemplate.save(mongoData);
        return true;
    }
}
