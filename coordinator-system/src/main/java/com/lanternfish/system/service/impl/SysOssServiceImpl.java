package com.lanternfish.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.lanternfish.common.constant.Constants;
import com.lanternfish.common.core.domain.model.LoginUser;
import com.lanternfish.common.core.service.OssService;
import com.lanternfish.common.utils.StringUtils;
import com.lanternfish.common.core.page.TableDataInfo;
import com.lanternfish.common.core.domain.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import com.lanternfish.system.domain.bo.SysOssBo;
import com.lanternfish.system.domain.vo.SysOssVo;
import com.lanternfish.system.domain.SysOss;
import com.lanternfish.system.mapper.SysOssMapper;
import com.lanternfish.system.service.ISysOssService;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

/**
 * OSS对象存储Service业务层处理
 *
 * @author Liam
 * @date 2024-04-23
 */
@RequiredArgsConstructor
@Service
public class SysOssServiceImpl implements ISysOssService , OssService {
    private static final Logger log = LoggerFactory.getLogger(SysOssServiceImpl.class);

    private final SysOssMapper baseMapper;

    private final FastFileStorageClient fastFileStorageClient;

    private final static String PATH_SEGMENTATION = "/";
    @Value("${coordinator.httpPrefix}")
    private String httpPrefix;
    @Value("${fdfs.web-server-url}")
    private String webServerUrl;

    /**
     * 查询OSS对象存储
     */
    @Override
    public SysOssVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询OSS对象存储列表
     */
    @Override
    public TableDataInfo<SysOssVo> queryPageList(SysOssBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<SysOss> lqw = buildQueryWrapper(bo);
        Page<SysOssVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询OSS对象存储列表
     */
    @Override
    public List<SysOssVo> queryList(SysOssBo bo) {
        LambdaQueryWrapper<SysOss> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<SysOss> buildQueryWrapper(SysOssBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<SysOss> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.isNotBlank(bo.getFileName()), SysOss::getFileName, bo.getFileName());
        lqw.like(StringUtils.isNotBlank(bo.getOriginalName()), SysOss::getOriginalName, bo.getOriginalName());
        lqw.eq(StringUtils.isNotBlank(bo.getFileSuffix()), SysOss::getFileSuffix, bo.getFileSuffix());
        lqw.eq(StringUtils.isNotBlank(bo.getUrl()), SysOss::getUrl, bo.getUrl());
        lqw.eq(bo.getIsDelete() != null, SysOss::getIsDelete, bo.getIsDelete());
        return lqw;
    }

    /**
     * 新增OSS对象存储
     */
    @Override
    public Boolean insertByBo(SysOssBo bo) {
        SysOss add = BeanUtil.toBean(bo, SysOss.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改OSS对象存储
     */
    @Override
    public Boolean updateByBo(SysOssBo bo) {
        SysOss update = BeanUtil.toBean(bo, SysOss.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(SysOss entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除OSS对象存储
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }

    @Override
    public String selectUrlByIds(String ossIds) {
        if (StringUtils.isNotBlank(ossIds)) {
            String[] ids = ossIds.split(",");
            List<SysOss> ossList = baseMapper.selectBatchIds(Arrays.asList(ids));
            if (ossList != null && ossList.size() > 0) {
                List<String> urlList = ossList.stream().map(SysOss::getUrl).collect(Collectors.toList());
                return StringUtils.join(urlList, ",");
            }
        }
        return null;
    }

    @Override
    @Async
    public Future<String> uploadFile(MultipartFile file, LoginUser loginUser) {
        String url;
        try {
            //文件上传
            String fileSuffix = Objects.requireNonNull(file.getOriginalFilename()).substring(file.getOriginalFilename().lastIndexOf(".") + 1);
            StorePath storePath = fastFileStorageClient.uploadFile(file.getInputStream(), file.getSize(), fileSuffix, null);
            url = httpPrefix + webServerUrl + Constants.PATH_SEGMENTATION + storePath.getFullPath();

            //保存文件信息
            SysOssBo sysOssBo = SysOssBo.builder()
                .fileName(storePath.getPath().substring(storePath.getPath().lastIndexOf(Constants.PATH_SEGMENTATION) + 1))
                .originalName(file.getOriginalFilename())
                .fileSuffix(fileSuffix)
                .url(url)
                .isDelete(Constants.DEL_FLAG_0).build();
            sysOssBo.setCreateBy(loginUser.getUsername());
            sysOssBo.setUpdateBy(loginUser.getUsername());
            insertByBo(sysOssBo);
            log.info("文件上传成功，文件名：{}，url：{}", file.getOriginalFilename(), url);
        } catch (IOException e) {
            log.error("文件上传失败，原因：{}", e.getMessage());
            throw new RuntimeException(e);
        }
        return new AsyncResult<>(url);
    }
}
