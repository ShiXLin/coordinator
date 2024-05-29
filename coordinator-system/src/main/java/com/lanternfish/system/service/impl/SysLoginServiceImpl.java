package com.lanternfish.system.service.impl;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.secure.BCrypt;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.lanternfish.common.constant.CacheConstants;
import com.lanternfish.common.constant.Constants;
import com.lanternfish.common.core.domain.dto.RoleDTO;
import com.lanternfish.common.core.domain.entity.SysUser;
import com.lanternfish.common.core.domain.event.LogininforEvent;
import com.lanternfish.common.core.domain.model.LoginUser;
import com.lanternfish.common.enums.DeviceType;
import com.lanternfish.common.enums.LoginType;
import com.lanternfish.common.enums.UserStatus;
import com.lanternfish.common.exception.user.CaptchaException;
import com.lanternfish.common.exception.user.CaptchaExpireException;
import com.lanternfish.common.exception.user.UserException;
import com.lanternfish.common.helper.LoginHelper;
import com.lanternfish.common.utils.DateUtils;
import com.lanternfish.common.utils.MessageUtils;
import com.lanternfish.common.utils.ServletUtils;
import com.lanternfish.common.utils.StringUtils;
import com.lanternfish.common.utils.redis.RedisUtils;
import com.lanternfish.common.utils.spring.SpringUtils;
import com.lanternfish.system.mapper.SysUserMapper;
import com.lanternfish.system.service.ISysConfigService;
import com.lanternfish.system.service.ISysLoginService;
import com.lanternfish.system.service.ISysPermissionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.function.Supplier;

/**
 * 登录校验方法
 *
 * @author Liam
 */
@RequiredArgsConstructor
@Slf4j
@Service
public class SysLoginServiceImpl implements ISysLoginService {

    private final SysUserMapper userMapper;
    private final ISysConfigService configService;
    private final ISysPermissionService permissionService;

    @Value("${user.password.maxRetryCount}")
    private Integer maxRetryCount;

    @Value("${user.password.lockTime}")
    private Integer lockTime;


    @Override
    public String login(String username, String password, String code, String uuid) {
        boolean captchaEnabled = configService.selectCaptchaEnabled();
        // 验证码开关
        if (captchaEnabled) {
            validateCaptcha(username, code, uuid);
        }
        SysUser user = loadUserByUsername(username);
        checkLogin(LoginType.PASSWORD, username, () -> !BCrypt.checkpw(password, user.getPassword()));
        // 此处可根据登录用户的数据不同 自行创建 loginUser
        LoginUser loginUser = buildLoginUser(user);
        // 生成token
        LoginHelper.loginByDevice(loginUser, DeviceType.PC);

        recordLoginInfo(username, Constants.LOGIN_SUCCESS, MessageUtils.message("user.login.success"));
        recordLoginInfo(user.getUserId(), username);
        return StpUtil.getTokenValue();
    }


    @Override
    public String smsLogin(String phoneNumber, String smsCode) {
        // 通过手机号查找用户
        SysUser user = loadUserByPhoneNumber(phoneNumber);

        checkLogin(LoginType.SMS, user.getUserName(), () -> !validateSmsCode(phoneNumber, smsCode));
        // 此处可根据登录用户的数据不同 自行创建 loginUser
        LoginUser loginUser = buildLoginUser(user);
        // 生成token
        LoginHelper.loginByDevice(loginUser, DeviceType.APP);

        recordLoginInfo(user.getUserName(), Constants.LOGIN_SUCCESS, MessageUtils.message("user.login.success"));
        recordLoginInfo(user.getUserId(), user.getUserName());
        return StpUtil.getTokenValue();
    }


    @Override
    public String emailLogin(String email, String emailCode) {
        // 通过手机号查找用户
        SysUser user = loadUserByEmail(email);

        checkLogin(LoginType.EMAIL, user.getUserName(), () -> !validateEmailCode(email, emailCode));
        // 此处可根据登录用户的数据不同 自行创建 loginUser
        LoginUser loginUser = buildLoginUser(user);
        // 生成token
        LoginHelper.loginByDevice(loginUser, DeviceType.APP);

        recordLoginInfo(user.getUserName(), Constants.LOGIN_SUCCESS, MessageUtils.message("user.login.success"));
        recordLoginInfo(user.getUserId(), user.getUserName());
        return StpUtil.getTokenValue();
    }


    @Override
    public String wechatLogin(String wechatOpenId) {
        return loginByOpenId(wechatOpenId, SysUser::getWechatOpenId);
    }

    @Override
    public String dingTalkLogin(String dingTalkOpenId) {
        return loginByOpenId(dingTalkOpenId, SysUser::getDingTalkOpenId);
    }


    @Override
    public void logout() {
        try {
            LoginUser loginUser = LoginHelper.getLoginUser();
            StpUtil.logout();
            recordLoginInfo(loginUser.getUsername(), Constants.LOGOUT, MessageUtils.message("user.logout.success"));
        } catch (NotLoginException ignored) {
        }
    }

    /**
     * 记录登录信息
     *
     * @param username 用户名
     * @param status   状态
     * @param message  消息内容
     */
    public void recordLoginInfo(String username, String status, String message) {
        LogininforEvent logininforEvent = new LogininforEvent();
        logininforEvent.setUsername(username);
        logininforEvent.setStatus(status);
        logininforEvent.setMessage(message);
        logininforEvent.setRequest(ServletUtils.getRequest());
        SpringUtils.context().publishEvent(logininforEvent);
    }

    /**
     * 校验短信验证码
     *
     * @param phoneNumber 手机号码
     * @param smsCode     短信验证码
     * @return 是否成功
     */
    private boolean validateSmsCode(String phoneNumber, String smsCode) {
        String code = RedisUtils.getCacheObject(CacheConstants.CAPTCHA_CODE_KEY + phoneNumber);
        if (StringUtils.isBlank(code)) {
            recordLoginInfo(phoneNumber, Constants.LOGIN_FAIL, MessageUtils.message("user.jcaptcha.expire"));
            throw new CaptchaExpireException();
        }
        return code.equals(smsCode);
    }

    /**
     * 校验邮箱验证码
     *
     * @param email     邮箱
     * @param emailCode 验证码
     * @return 是否成功
     */
    private boolean validateEmailCode(String email, String emailCode) {
        String code = RedisUtils.getCacheObject(CacheConstants.CAPTCHA_CODE_KEY + email);
        if (StringUtils.isBlank(code)) {
            recordLoginInfo(email, Constants.LOGIN_FAIL, MessageUtils.message("user.jcaptcha.expire"));
            throw new CaptchaExpireException();
        }
        return code.equals(emailCode);
    }

    /**
     * 校验验证码
     *
     * @param username 用户名
     * @param code     验证码
     * @param uuid     唯一标识
     */
    public void validateCaptcha(String username, String code, String uuid) {
        String verifyKey = CacheConstants.CAPTCHA_CODE_KEY + StringUtils.defaultString(uuid, "");
        String captcha = RedisUtils.getCacheObject(verifyKey);
        RedisUtils.deleteObject(verifyKey);
        if (captcha == null) {
            recordLoginInfo(username, Constants.LOGIN_FAIL, MessageUtils.message("user.jcaptcha.expire"));
            throw new CaptchaExpireException();
        }
        if (!code.equalsIgnoreCase(captcha)) {
            recordLoginInfo(username, Constants.LOGIN_FAIL, MessageUtils.message("user.jcaptcha.error"));
            throw new CaptchaException();
        }
    }


    private String loginByOpenId(String openId, SFunction<SysUser, ?> column) {
        SysUser user = userMapper.selectOne(new LambdaQueryWrapper<SysUser>().select(column, SysUser::getStatus).eq(column, openId));
        checkUserStatus(user, openId, user.getUserName());
        // 此处可根据登录用户的数据不同 自行创建 loginUser
        LoginUser loginUser = buildLoginUser(user);
        // 生成token
        LoginHelper.loginByDevice(loginUser, DeviceType.PC);
        recordLoginInfo(user.getUserName(), Constants.LOGIN_SUCCESS, MessageUtils.message("user.login.success"));
        recordLoginInfo(user.getUserId(), user.getUserName());
        return StpUtil.getTokenValue();
    }

    private void checkUserStatus(SysUser user, String notExistsMsg, String disabledMsg) {
        if (ObjectUtil.isNull(user)) {
            log.info("登录用户：{} 不存在.", notExistsMsg);
            throw new UserException("user.not.exists", notExistsMsg);
        } else if (UserStatus.DISABLE.getCode().equals(user.getStatus())) {
            log.info("登录用户：{} 已被停用.", disabledMsg);
            throw new UserException("user.blocked", disabledMsg);
        }
    }

    private SysUser loadUserByUsername(String username) {
        SysUser user = userMapper.selectOne(new LambdaQueryWrapper<SysUser>().select(SysUser::getUserName, SysUser::getStatus).eq(SysUser::getUserName, username));
        checkUserStatus(user, username, username);
        return userMapper.selectUserByUserName(username);
    }

    /**
     * 通过手机号码加载用户
     *
     * @param phoneNumber 手机号码
     * @return 用户
     */
    private SysUser loadUserByPhoneNumber(String phoneNumber) {
        SysUser user = userMapper.selectOne(new LambdaQueryWrapper<SysUser>().select(SysUser::getPhonenumber, SysUser::getStatus).eq(SysUser::getPhonenumber, phoneNumber));
        checkUserStatus(user, phoneNumber, phoneNumber);
        return userMapper.selectUserByPhonenumber(phoneNumber);
    }

    /**
     * 通过邮箱加载用户
     *
     * @param email 邮箱
     * @return 用户
     */
    private SysUser loadUserByEmail(String email) {
        SysUser user = userMapper.selectOne(new LambdaQueryWrapper<SysUser>().select(SysUser::getPhonenumber, SysUser::getStatus).eq(SysUser::getEmail, email));
        checkUserStatus(user, email, email);
        return userMapper.selectUserByEmail(email);
    }


    /**
     * 构建登录用户
     *
     * @param user 系统用户数据对象
     * @return 登陆用户
     */
    public LoginUser buildLoginUser(SysUser user) {
        LoginUser loginUser = new LoginUser();
        BeanUtil.copyProperties(user, loginUser);
        loginUser.setMenuPermission(permissionService.getMenuPermission(user));
        loginUser.setRolePermission(permissionService.getRolePermission(user));
        loginUser.setDeptName(ObjectUtil.isNull(user.getDept()) ? "" : user.getDept().getDeptName());
        loginUser.setRoles(BeanUtil.copyToList(user.getRoles(), RoleDTO.class));
        return loginUser;
    }

    /**
     * 记录登录信息
     *
     * @param userId   用户id
     * @param username 用户账号
     */
    public void recordLoginInfo(Long userId, String username) {
        SysUser sysUser = new SysUser();
        sysUser.setUserId(userId);
        sysUser.setLoginIp(ServletUtils.getClientIP());
        sysUser.setLoginDate(DateUtils.getNowDate());
        sysUser.setUpdateBy(username);
        userMapper.updateById(sysUser);
    }

    /**
     * 登录校验
     *
     * @param loginType 登陆方式
     * @param username  用户名
     * @param supplier  登陆结果
     */
    private void checkLogin(LoginType loginType, String username, Supplier<Boolean> supplier) {
        String errorKey = CacheConstants.PWD_ERR_CNT_KEY + username;
        String loginFail = Constants.LOGIN_FAIL;

        // 获取用户登录错误次数(可自定义限制策略 例如: key + username + ip)
        Integer errorNumber = RedisUtils.getCacheObject(errorKey);
        // 锁定时间内登录 则踢出
        if (ObjectUtil.isNotNull(errorNumber) && errorNumber.equals(maxRetryCount)) {
            recordLoginInfo(username, loginFail, MessageUtils.message(loginType.getRetryLimitExceed(), maxRetryCount, lockTime));
            throw new UserException(loginType.getRetryLimitExceed(), maxRetryCount, lockTime);
        }

        if (supplier.get()) {
            // 是否第一次
            errorNumber = ObjectUtil.isNull(errorNumber) ? 1 : errorNumber + 1;
            // 达到规定错误次数 则锁定登录
            if (errorNumber.equals(maxRetryCount)) {
                RedisUtils.setCacheObject(errorKey, errorNumber, Duration.ofMinutes(lockTime));
                recordLoginInfo(username, loginFail, MessageUtils.message(loginType.getRetryLimitExceed(), maxRetryCount, lockTime));
                throw new UserException(loginType.getRetryLimitExceed(), maxRetryCount, lockTime);
            } else {
                // 未达到规定错误次数 则递增
                RedisUtils.setCacheObject(errorKey, errorNumber);
                recordLoginInfo(username, loginFail, MessageUtils.message(loginType.getRetryLimitCount(), errorNumber));
                throw new UserException(loginType.getRetryLimitCount(), errorNumber);
            }
        }

        // 登录成功 清空错误次数
        RedisUtils.deleteObject(errorKey);
    }

}
