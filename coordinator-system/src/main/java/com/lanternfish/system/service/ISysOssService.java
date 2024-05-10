package com.lanternfish.system.service;

import com.lanternfish.system.domain.SysOss;
import com.lanternfish.system.domain.vo.SysOssVo;
import com.lanternfish.system.domain.bo.SysOssBo;
import com.lanternfish.common.core.page.TableDataInfo;
import com.lanternfish.common.core.domain.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * OSS对象存储Service接口
 *
 * @author Liam
 * @date 2024-04-23
 */
public interface ISysOssService {

    /**
     * 查询OSS对象存储
     */
    SysOssVo queryById(Long id);

    /**
     * 查询OSS对象存储列表
     */
    TableDataInfo<SysOssVo> queryPageList(SysOssBo bo, PageQuery pageQuery);

    /**
     * 查询OSS对象存储列表
     */
    List<SysOssVo> queryList(SysOssBo bo);

    /**
     * 新增OSS对象存储
     */
    Boolean insertByBo(SysOssBo bo);

    /**
     * 修改OSS对象存储
     */
    Boolean updateByBo(SysOssBo bo);

    /**
     * 校验并批量删除OSS对象存储信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

}
