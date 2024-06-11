package com.lanternfish.workflow.mapper;

import com.lanternfish.common.core.mapper.BaseMapperPlus;
import com.lanternfish.workflow.domain.WfCategory;
import com.lanternfish.workflow.domain.vo.WfAppTypeVo;
import com.lanternfish.workflow.domain.vo.WfCategoryVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * 流程分类Mapper接口
 *
 * @author KonBAI
 * @date 2022-01-15
 */
@Repository
public interface WfCategoryMapper extends BaseMapperPlus<WfCategoryMapper, WfCategory, WfCategoryVo> {
    /**
     * 查询流程分类列表
     *
     * @param code 流程分类编码
     * @return app_type
     */
   String selectAppTypeByCode(@Param("code") String code);

    /**
     * 查询流程分类字典信息
     * @param code 流程分类编码
     * @return 流程分类集
     */
   WfAppTypeVo selectAppTypeVoByCode(@Param("code") String code);
}
