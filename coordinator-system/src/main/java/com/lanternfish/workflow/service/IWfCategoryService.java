package com.lanternfish.workflow.service;

import com.lanternfish.common.core.domain.PageQuery;
import com.lanternfish.common.core.page.TableDataInfo;
import com.lanternfish.workflow.domain.WfCategory;
import com.lanternfish.workflow.domain.vo.WfAppTypeVo;
import com.lanternfish.workflow.domain.vo.WfCategoryVo;

import java.util.Collection;
import java.util.List;

import javax.validation.constraints.NotNull;

/**
 * 流程分类Service接口
 *
 * @author KonBAI
 * @date 2022-01-15
 */
public interface IWfCategoryService {
    /**
     * 查询单个
     *
     * @return WfCategoryVo
     */
    WfCategoryVo queryById(Long categoryId);

    /**
     * 查询列表
     *
     * @param category 流程分类信息
     * @return TableDataInfo<WfCategoryVo>
     */
    TableDataInfo<WfCategoryVo> queryPageList(WfCategory category, PageQuery pageQuery);

    /**
     * 查询列表
     *
     * @param category 流程分类信息
     * @return List<WfCategoryVo>
     */
    List<WfCategoryVo> queryList(WfCategory category);

    /**
     * 新增流程分类
     *
     * @param category 流程分类信息
     * @return 结果
     */
    int insertCategory(WfCategory category);

    /**
     * 编辑流程分类
     *
     * @param category 流程分类信息
     * @return 结果
     */
    int updateCategory(WfCategory category);

    /**
     * 校验并删除数据
     *
     * @param ids     主键集合
     * @param isValid 是否校验,true-删除前校验,false-不校验
     * @return 结果
     */
    int deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    /**
     * 校验分类编码是否唯一
     *
     * @param category 流程分类
     * @return 结果
     */
    boolean checkCategoryCodeUnique(WfCategory category);

    /**
     * 根据分类编码查询分类信息
     * @param code 分类编码
     * @return List<WfAppTypeVo>
     */
    List<WfAppTypeVo> queryInfoByCode(@NotNull(message = "主键不能为空") String code);
}
