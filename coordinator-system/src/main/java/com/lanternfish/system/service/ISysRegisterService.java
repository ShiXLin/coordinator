package com.lanternfish.system.service;

import com.lanternfish.common.core.domain.model.RegisterBody;

/**
 * @author Liam
 * @date 2024-5-29
 * @apiNote
 */
public interface ISysRegisterService {
    /**
     * 注册
     * @param registerBody 注册内容
     */
    void register(RegisterBody registerBody);
}
