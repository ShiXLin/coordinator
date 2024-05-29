package com.lanternfish.system.service;

import com.lanternfish.common.core.domain.entity.SysUser;

import java.util.Set;

/**
 * @author Liam
 * @date 2024-5-29
 * @apiNote
 */
public interface ISysPermissionService {

    /**
     * 获取角色数据权限
     *
     * @param user 用户信息
     * @return 角色权限信息
     */
    Set<String> getRolePermission(SysUser user);

    /**
     * 获取菜单数据权限
     *
     * @param user 用户信息
     * @return 菜单权限信息
     */
    Set<String> getMenuPermission(SysUser user);
}
