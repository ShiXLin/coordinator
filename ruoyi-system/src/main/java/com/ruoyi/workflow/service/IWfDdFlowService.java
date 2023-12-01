package com.ruoyi.workflow.service;

import com.ruoyi.workflow.domain.WfDdFlow;
import com.ruoyi.workflow.domain.vo.WfDdFlowVo;
import com.ruoyi.workflow.domain.bo.WfDdFlowBo;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.core.domain.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 钉钉流程Service接口
 *
 * @author nbacheng
 * @date 2023-11-29
 */
public interface IWfDdFlowService {

    /**
     * 查询钉钉流程
     */
    WfDdFlowVo queryById(Long id);

    /**
     * 查询钉钉流程列表
     */
    TableDataInfo<WfDdFlowVo> queryPageList(WfDdFlowBo bo, PageQuery pageQuery);

    /**
     * 查询钉钉流程列表
     */
    List<WfDdFlowVo> queryList(WfDdFlowBo bo);

    /**
     * 新增钉钉流程
     */
    Boolean insertByBo(WfDdFlowBo bo);

    /**
     * 修改钉钉流程
     */
    Boolean updateByBo(WfDdFlowBo bo);

    /**
     * 校验并批量删除钉钉流程信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
