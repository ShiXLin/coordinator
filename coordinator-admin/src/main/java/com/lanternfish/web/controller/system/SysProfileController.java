package com.lanternfish.web.controller.system;

import cn.dev33.satoken.secure.BCrypt;
import cn.hutool.core.io.FileUtil;
import com.lanternfish.common.annotation.Log;
import com.lanternfish.common.config.CoordinatorConfig;
import com.lanternfish.common.constant.Constants;
import com.lanternfish.common.core.controller.BaseController;
import com.lanternfish.common.core.domain.R;
import com.lanternfish.common.core.domain.entity.SysUser;
import com.lanternfish.common.core.domain.model.LoginUser;
import com.lanternfish.common.core.service.CommonService;
import com.lanternfish.common.enums.BusinessType;
import com.lanternfish.common.helper.LoginHelper;
import com.lanternfish.common.utils.StringUtils;
import com.lanternfish.common.utils.file.FileUploadUtils;
import com.lanternfish.common.utils.file.MimeTypeUtils;
import com.lanternfish.framework.config.ServerConfig;
import com.lanternfish.system.service.ISysUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 个人信息 业务处理
 *
 * @author Liam
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/system/user/profile")
public class SysProfileController extends BaseController {

	@Autowired
    private ServerConfig serverConfig;
	@Autowired CommonService commonService;

    private final ISysUserService userService;

    @Value(value = "${coordinator.profile}")
    private String uploadpath;

    @Value(value = "${nbcio.localfilehttp}")
    private String localfilehttp;

    /**
     * 本地：local minio：minio 阿里：alioss
     */
    @Value(value="${coordinator.uploadtype}")
    private String uploadtype;

    /**
     * 个人信息
     */
    @GetMapping
    public R<Map<String, Object>> profile() {
        SysUser user = userService.selectUserById(getUserId());
        Map<String, Object> ajax = new HashMap<>();
        ajax.put("user", user);
        ajax.put("roleGroup", userService.selectUserRoleGroup(user.getUserName()));
        ajax.put("postGroup", userService.selectUserPostGroup(user.getUserName()));
		ajax.put("auths", userService.selectAuthUserListByUserId(user.getUserId()));
        return R.ok(ajax);
    }

    /**
     * 修改用户
     */
    @Log(title = "个人信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public R<Void> updateProfile(@RequestBody SysUser user) {
        if (StringUtils.isNotEmpty(user.getPhonenumber()) && !userService.checkPhoneUnique(user)) {
            return R.fail("修改用户'" + user.getUserName() + "'失败，手机号码已存在");
        }
        if (StringUtils.isNotEmpty(user.getEmail()) && !userService.checkEmailUnique(user)) {
            return R.fail("修改用户'" + user.getUserName() + "'失败，邮箱账号已存在");
        }
        user.setUserId(getUserId());
        user.setUserName(null);
        user.setPassword(null);
        user.setAvatar(null);
        user.setDeptId(null);
        if (userService.updateUserProfile(user) > 0) {
            return R.ok();
        }
        return R.fail("修改个人信息异常，请联系管理员");
    }

    /**
     * 重置密码
     *
     * @param newPassword 旧密码
     * @param oldPassword 新密码
     */
    @Log(title = "个人信息", businessType = BusinessType.UPDATE)
    @PutMapping("/updatePwd")
    public R<Void> updatePwd(String oldPassword, String newPassword) {
        SysUser user = userService.selectUserById(LoginHelper.getUserId());
        String userName = user.getUserName();
        String password = user.getPassword();
        if (!BCrypt.checkpw(oldPassword, password)) {
            return R.fail("修改密码失败，旧密码错误");
        }
        if (BCrypt.checkpw(newPassword, password)) {
            return R.fail("新密码不能与旧密码相同");
        }

        if (userService.resetUserPwd(userName, BCrypt.hashpw(newPassword)) > 0) {
            return R.ok();
        }
        return R.fail("修改密码异常，请联系管理员");
    }

    /**
     * 头像上传
     *
     * @param avatarfile 用户头像
     */
    @Log(title = "用户头像", businessType = BusinessType.UPDATE)
    @PostMapping(value = "/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public R<Map<String, Object>> avatar(@RequestPart("avatarfile") MultipartFile avatarfile) {
        Map<String, Object> ajax = new HashMap<>();
        if (!avatarfile.isEmpty()) {
            String extension = FileUtil.extName(avatarfile.getOriginalFilename());
            if (!StringUtils.equalsAnyIgnoreCase(extension, MimeTypeUtils.IMAGE_EXTENSION)) {
                return R.fail("文件格式不正确，请上传" + Arrays.toString(MimeTypeUtils.IMAGE_EXTENSION) + "格式");
            }
            if(Constants.UPLOAD_TYPE_LOCAL.equals(uploadtype)) {
            	 // 上传文件路径
                String filePath = CoordinatorConfig.getAvatarPath();
                // 上传并返回新文件名称
                String fileName = null;
				try {
					fileName = localfilehttp + FileUploadUtils.upload(filePath, avatarfile);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

                if (userService.updateUserAvatar(getUsername(), fileName)) {
	                ajax.put("imgUrl", fileName);
					// 更新缓存用户头像
	                LoginUser loginUser = commonService.getLoginUser();
	                SysUser sysuser = commonService.getSysUserByUserName(loginUser.getUsername());
	                sysuser.setAvatar(fileName);
	                return R.ok(ajax);
	            }
            }
            else {

            }

        }
        return R.fail("上传图片异常，请联系管理员");
    }
}
