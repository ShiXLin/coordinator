package com.lanternfish.workflow.service;

import com.lanternfish.common.core.domain.PageQuery;
import com.lanternfish.common.core.page.TableDataInfo;
import com.lanternfish.flowable.core.domain.ProcessQuery;
import com.lanternfish.workflow.domain.vo.WfDeployVo;

import java.util.List;

/**
 * @author KonBAI
 * @createTime 2022/6/30 9:03
 */
public interface IWfDeployService {

    TableDataInfo<WfDeployVo> queryPageList(ProcessQuery processQuery, PageQuery pageQuery);

    TableDataInfo<WfDeployVo> queryPublishList(String processKey, PageQuery pageQuery);

    void updateState(String definitionId, String stateCode);

    String queryBpmnXmlById(String definitionId);

    void deleteByIds(List<String> deployIds);
}
