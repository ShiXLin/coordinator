package com.lanternfish.workflow.service;

import com.lanternfish.workflow.domain.WfDefaultOperate;
import com.lanternfish.workflow.domain.vo.WfDefaultOperateVo;
import com.lanternfish.workflow.domain.bo.WfDefaultOperateBo;
import com.lanternfish.common.core.page.TableDataInfo;
import com.lanternfish.common.core.domain.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 流程默认操作Service接口
 *
 * @author Liam
 * @date 2023-11-23
 */
public interface IWfDefaultOperateService {

    /**
     * 查询流程默认操作
     */
    WfDefaultOperateVo queryById(Long id);

    /**
     * 查询流程默认操作列表
     */
    TableDataInfo<WfDefaultOperateVo> queryPageList(WfDefaultOperateBo bo, PageQuery pageQuery);

    /**
     * 查询流程默认操作列表
     */
    List<WfDefaultOperateVo> queryList(WfDefaultOperateBo bo);

    /**
     * 新增流程默认操作
     */
    Boolean insertByBo(WfDefaultOperateBo bo);

    /**
     * 修改流程默认操作
     */
    Boolean updateByBo(WfDefaultOperateBo bo);

    /**
     * 校验并批量删除流程默认操作信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
