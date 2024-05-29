package com.lanternfish.system.service;

/**
 * @author Liam
 * @date 2024-5-29
 * @apiNote 处理登陆业务接口
 */
public interface ISysLoginService {

    /**
     * 登录验证
     *
     * @param username 用户名
     * @param password 密码
     * @param code     验证码
     * @param uuid     唯一标识
     * @return token
     */
    String login(String username, String password, String code, String uuid);

    /**
     * 手机短信验证码登陆
     *
     * @param phoneNumber 手机号
     * @param smsCode     短信验证码
     * @return token
     */
    String smsLogin(String phoneNumber, String smsCode);

    /**
     * 邮箱登陆
     *
     * @param email     邮箱
     * @param emailCode 验证码
     * @return token
     */
    String emailLogin(String email, String emailCode);

    /**
     * 微信扫码登陆
     *
     * @param wechatOpenId 微信openid
     * @return token
     */
    String wechatLogin(String wechatOpenId);

    /**
     * 钉钉扫码登陆
     *
     * @param dingTalkOpenId 钉钉openid
     * @return token
     */
    String dingTalkLogin(String dingTalkOpenId);

    /**
     * 退出登录
     */
    void logout();

}
