package com.lanternfish.common.config;

import lombok.Data;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 读取项目相关配置
 *
 * @author Liam
 */

@Data
@Component
@ConfigurationProperties(prefix = "coordinator")
public class CoordinatorConfig {

    /**
     * 项目名称
     */
    private String name;

    /**
     * 版本
     */
    private String version;

    /**
     * 版权年份
     */
    private String copyrightYear;

    /**
     * 实例演示开关
     */
    private boolean demoEnabled;

    /** 上传类型  本地：local, Minio：minio, 阿里云：alioss */
    private static String uploadType;

    /** 上传路径 */
    private static String profile;

    /**
     * 缓存懒加载
     */
    private boolean cacheLazy;

    /**
     * 获取地址开关
     */
    @Getter
    private static boolean addressEnabled;

    public void setAddressEnabled(boolean addressEnabled) {
        CoordinatorConfig.addressEnabled = addressEnabled;
    }

	 /**
     * 获取导入上传路径
     */
    public static String getImportPath()
    {
        return getProfile() + "/import";
    }

    /**
     * 获取头像上传路径
     */
    public static String getAvatarPath()
    {
        return getProfile() + "/avatar";
    }

    /**
     * 获取下载路径
     */
    public static String getDownloadPath()
    {
        return getProfile() + "/download/";
    }

    /**
     * 获取上传路径
     */
    public static String getUploadPath()
    {
        return getProfile() + "/upload";
    }

	public static String getProfile() {
		return profile;
	}

	public void setProfile(String profile) {
		CoordinatorConfig.profile = profile;
	}

	public static String getUploadType() {
		return uploadType;
	}

	public void setUploadType(String uploadtype) {
		CoordinatorConfig.uploadType = uploadtype;
	}
}
