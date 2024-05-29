package com.lanternfish.system.domain.vo;

import com.lanternfish.common.core.domain.entity.SysUser;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author Liam
 * @date 2024-5-27
 * @apiNote
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserContactInfoVo  implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 用户名称首字母或汉字首字拼音首字母
     */
    private String prefixUserName;

    /**
     * 用户信息
     */
    private List<SysUser> sysUserList;
}
