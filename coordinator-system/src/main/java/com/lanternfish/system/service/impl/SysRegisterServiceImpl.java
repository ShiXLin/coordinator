package com.lanternfish.system.service.impl;

import cn.dev33.satoken.secure.BCrypt;
import com.lanternfish.common.constant.CacheConstants;
import com.lanternfish.common.constant.Constants;
import com.lanternfish.common.core.domain.event.LogininforEvent;
import com.lanternfish.common.core.domain.entity.SysUser;
import com.lanternfish.common.core.domain.model.RegisterBody;
import com.lanternfish.common.enums.UserType;
import com.lanternfish.common.exception.user.CaptchaException;
import com.lanternfish.common.exception.user.CaptchaExpireException;
import com.lanternfish.common.exception.user.UserException;
import com.lanternfish.common.utils.MessageUtils;
import com.lanternfish.common.utils.ServletUtils;
import com.lanternfish.common.utils.StringUtils;
import com.lanternfish.common.utils.redis.RedisUtils;
import com.lanternfish.common.utils.spring.SpringUtils;
import com.lanternfish.system.service.ISysConfigService;
import com.lanternfish.system.service.ISysRegisterService;
import com.lanternfish.system.service.ISysUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 注册校验方法
 *
 * @author Liam
 */
@RequiredArgsConstructor
@Service
public class SysRegisterServiceImpl implements ISysRegisterService {

    private final ISysUserService userService;
    private final ISysConfigService configService;

    @Override
    public void register(RegisterBody registerBody) {
        String username = registerBody.getUsername();
        String password = registerBody.getPassword();
        // 校验用户类型是否存在
        String userType = UserType.getUserType(registerBody.getUserType()).getUserType();

        boolean captchaEnabled = configService.selectCaptchaEnabled();
        // 验证码开关
        if (captchaEnabled) {
            validateCaptcha(username, registerBody.getCode(), registerBody.getUuid());
        }
        SysUser sysUser = new SysUser();
        sysUser.setUserName(username);
        sysUser.setNickName(username);
        sysUser.setPassword(BCrypt.hashpw(password));
        sysUser.setUserType(userType);

        if (!userService.checkUserNameUnique(sysUser)) {
            throw new UserException("user.register.save.error", username);
        }
        boolean regFlag = userService.registerUser(sysUser);
        if (!regFlag) {
            throw new UserException("user.register.error");
        }
        recordLoginInfo(username, Constants.REGISTER, MessageUtils.message("user.register.success"));
    }

    /**
     * 校验验证码
     *
     * @param username 用户名
     * @param code     验证码
     * @param uuid     唯一标识
     */
    private void validateCaptcha(String username, String code, String uuid) {
        String verifyKey = CacheConstants.CAPTCHA_CODE_KEY + StringUtils.defaultString(uuid, "");
        String captcha = RedisUtils.getCacheObject(verifyKey);
        RedisUtils.deleteObject(verifyKey);
        if (captcha == null) {
            recordLoginInfo(username, Constants.REGISTER, MessageUtils.message("user.jcaptcha.expire"));
            throw new CaptchaExpireException();
        }
        if (!code.equalsIgnoreCase(captcha)) {
            recordLoginInfo(username, Constants.REGISTER, MessageUtils.message("user.jcaptcha.error"));
            throw new CaptchaException();
        }
    }

    /**
     * 记录登录信息
     *
     * @param username 用户名
     * @param status   状态
     * @param message  消息内容
     */
    private void recordLoginInfo(String username, String status, String message) {
        LogininforEvent logininforEvent = new LogininforEvent();
        logininforEvent.setUsername(username);
        logininforEvent.setStatus(status);
        logininforEvent.setMessage(message);
        logininforEvent.setRequest(ServletUtils.getRequest());
        SpringUtils.context().publishEvent(logininforEvent);
    }

}
