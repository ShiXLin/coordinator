package com.ruoyi.workflow.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.core.domain.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.ruoyi.workflow.domain.bo.WfDefaultOperateBo;
import com.ruoyi.workflow.domain.vo.WfDefaultOperateVo;
import com.ruoyi.workflow.domain.WfDefaultOperate;
import com.ruoyi.workflow.mapper.WfDefaultOperateMapper;
import com.ruoyi.workflow.service.IWfDefaultOperateService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 流程默认操作Service业务层处理
 *
 * @author nbacheng
 * @date 2023-11-23
 */
@RequiredArgsConstructor
@Service
public class WfDefaultOperateServiceImpl implements IWfDefaultOperateService {

    private final WfDefaultOperateMapper baseMapper;

    /**
     * 查询流程默认操作
     */
    @Override
    public WfDefaultOperateVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询流程默认操作列表
     */
    @Override
    public TableDataInfo<WfDefaultOperateVo> queryPageList(WfDefaultOperateBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<WfDefaultOperate> lqw = buildQueryWrapper(bo);
        Page<WfDefaultOperateVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询流程默认操作列表
     */
    @Override
    public List<WfDefaultOperateVo> queryList(WfDefaultOperateBo bo) {
        LambdaQueryWrapper<WfDefaultOperate> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<WfDefaultOperate> buildQueryWrapper(WfDefaultOperateBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<WfDefaultOperate> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(bo.getOpeType()), WfDefaultOperate::getOpeType, bo.getOpeType());
        lqw.like(StringUtils.isNotBlank(bo.getOpeName()), WfDefaultOperate::getOpeName, bo.getOpeName());
        lqw.eq(StringUtils.isNotBlank(bo.getIsEnable()), WfDefaultOperate::getIsEnable, bo.getIsEnable());
        lqw.eq(bo.getSort() != null, WfDefaultOperate::getSort, bo.getSort());
        return lqw;
    }

    /**
     * 新增流程默认操作
     */
    @Override
    public Boolean insertByBo(WfDefaultOperateBo bo) {
        WfDefaultOperate add = BeanUtil.toBean(bo, WfDefaultOperate.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改流程默认操作
     */
    @Override
    public Boolean updateByBo(WfDefaultOperateBo bo) {
        WfDefaultOperate update = BeanUtil.toBean(bo, WfDefaultOperate.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(WfDefaultOperate entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除流程默认操作
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }
}
