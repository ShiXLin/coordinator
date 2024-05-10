package com.lanternfish.common.core.service;

import com.lanternfish.common.core.domain.model.LoginUser;
import org.springframework.web.multipart.MultipartFile;

import java.util.concurrent.Future;

/**
 * 通用 OSS服务
 *
 * @author Liam
 */
public interface OssService {

    /**
     * 通过ossId查询对应的url
     *
     * @param ossIds ossId串逗号分隔
     * @return url串逗号分隔
     */
    String selectUrlByIds(String ossIds);

    Future<String> uploadFile(MultipartFile file, LoginUser loginUser);

}
