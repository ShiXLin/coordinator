package com.lanternfish.workflow.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.lanternfish.common.utils.StringUtils;
import com.lanternfish.common.core.page.TableDataInfo;
import com.lanternfish.common.core.domain.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.lanternfish.workflow.domain.bo.WfOperateRuleBo;
import com.lanternfish.workflow.domain.vo.WfOperateRuleVo;
import com.lanternfish.workflow.domain.WfOperateRule;
import com.lanternfish.workflow.mapper.WfOperateRuleMapper;
import com.lanternfish.workflow.service.IWfOperateRuleService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 流程操作规则Service业务层处理
 *
 * @author Liam
 * @date 2023-11-23
 */
@RequiredArgsConstructor
@Service
public class WfOperateRuleServiceImpl implements IWfOperateRuleService {

    private final WfOperateRuleMapper baseMapper;

    /**
     * 查询流程操作规则
     */
    @Override
    public WfOperateRuleVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询流程操作规则列表
     */
    @Override
    public TableDataInfo<WfOperateRuleVo> queryPageList(WfOperateRuleBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<WfOperateRule> lqw = buildQueryWrapper(bo);
        Page<WfOperateRuleVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询流程操作规则列表
     */
    @Override
    public List<WfOperateRuleVo> queryList(WfOperateRuleBo bo) {
        LambdaQueryWrapper<WfOperateRule> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<WfOperateRule> buildQueryWrapper(WfOperateRuleBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<WfOperateRule> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getConfigId() != null, WfOperateRule::getConfigId, bo.getConfigId());
        lqw.eq(StringUtils.isNotBlank(bo.getOpeType()), WfOperateRule::getOpeType, bo.getOpeType());
        lqw.like(StringUtils.isNotBlank(bo.getOpeName()), WfOperateRule::getOpeName, bo.getOpeName());
        lqw.eq(StringUtils.isNotBlank(bo.getIsEnable()), WfOperateRule::getIsEnable, bo.getIsEnable());
        lqw.eq(bo.getSort() != null, WfOperateRule::getSort, bo.getSort());
        return lqw;
    }

    /**
     * 新增流程操作规则
     */
    @Override
    public Boolean insertByBo(WfOperateRuleBo bo) {
        WfOperateRule add = BeanUtil.toBean(bo, WfOperateRule.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改流程操作规则
     */
    @Override
    public Boolean updateByBo(WfOperateRuleBo bo) {
        WfOperateRule update = BeanUtil.toBean(bo, WfOperateRule.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(WfOperateRule entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除流程操作规则
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }
}
