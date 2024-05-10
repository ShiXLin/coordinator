package com.lanternfish.system.domain.bo;

import com.lanternfish.common.core.domain.BaseEntity;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * OSS对象存储分页查询对象 sys_oss
 *
 * @author Liam
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
public class SysOssBo extends BaseEntity {

    /**
     * ossId
     */
    private Long id;

    /**
     * 文件名
     */
    private String fileName;

    /**
     * 原名
     */
    private String originalName;

    /**
     * 文件后缀名
     */
    private String fileSuffix;

    /**
     * URL地址
     */
    private String url;

    /**
     * 是否删除 1是 2否
     */
    private Integer isDelete;

}
