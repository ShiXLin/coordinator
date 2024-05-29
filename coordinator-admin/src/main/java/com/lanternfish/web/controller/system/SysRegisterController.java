package com.lanternfish.web.controller.system;

import cn.dev33.satoken.annotation.SaIgnore;
import com.lanternfish.common.core.controller.BaseController;
import com.lanternfish.common.core.domain.R;
import com.lanternfish.common.core.domain.model.RegisterBody;
import com.lanternfish.system.service.ISysConfigService;
import com.lanternfish.system.service.ISysRegisterService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 注册验证
 *
 * @author Liam
 */
@Validated
@RequiredArgsConstructor
@RestController
public class SysRegisterController extends BaseController {

    private final ISysRegisterService registerService;
    private final ISysConfigService configService;

    /**
     * 用户注册
     */
    @SaIgnore
    @PostMapping("/register")
    public R<Void> register(@Validated @RequestBody RegisterBody user) {
        if (!("true".equals(configService.selectConfigByKey("sys.account.registerUser")))) {
            return R.fail("当前系统没有开启注册功能！");
        }
        registerService.register(user);
        return R.ok();
    }
}
