package com.lanternfish.system.service;

import com.lanternfish.common.core.domain.PageQuery;
import com.lanternfish.common.core.page.TableDataInfo;
import com.lanternfish.system.domain.bo.SysOssConfigBo;
import com.lanternfish.system.domain.vo.SysOssConfigVo;

import java.util.Collection;

/**
 * 对象存储配置Service接口
 *
 * @author Liam
 * @author 孤舟烟雨
 * @date 2021-08-13
 */
public interface ISysOssConfigService {

    /**
     * 项目启动时，初始化参数到缓存，加载配置类
     */
    void init();

    /**
     * 查询单个
     */
    SysOssConfigVo queryById(Long ossConfigId);

    /**
     * 查询列表
     */
    TableDataInfo<SysOssConfigVo> queryPageList(SysOssConfigBo bo, PageQuery pageQuery);


    /**
     * 根据新增业务对象插入对象存储配置
     *
     * @param bo 对象存储配置新增业务对象
     * @return
     */
    Boolean insertByBo(SysOssConfigBo bo);

    /**
     * 根据编辑业务对象修改对象存储配置
     *
     * @param bo 对象存储配置编辑业务对象
     * @return
     */
    Boolean updateByBo(SysOssConfigBo bo);

    /**
     * 校验并删除数据
     *
     * @param ids     主键集合
     * @param isValid 是否校验,true-删除前校验,false-不校验
     * @return
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    /**
     * 启用停用状态
     */
    int updateOssConfigStatus(SysOssConfigBo bo);

}
