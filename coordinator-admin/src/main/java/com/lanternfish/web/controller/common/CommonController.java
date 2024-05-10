package com.lanternfish.web.controller.common;

import com.lanternfish.common.base.BaseMap;
import com.lanternfish.common.core.domain.R;
import com.lanternfish.common.core.domain.model.LoginUser;
import com.lanternfish.common.core.service.OssService;
import com.lanternfish.common.helper.LoginHelper;
import com.lanternfish.common.utils.StringUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;

/**
 * 通用请求处理
 * <p>
 * todo update the method of file upload and download
 *
 * @author Liam
 */
@RestController
@RequestMapping("/common")
@RequiredArgsConstructor
@Tag(name = "文件上传", description = "通用文件上传")
public class CommonController {


    private final OssService ossService;


    /**
     * fastdfs 上传文件
     */
    @PostMapping(path = "/uploadFile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "上传文件", description = "上传文件")
    @ResponseBody
    public R<BaseMap> uploadFile(@Parameter(description = "需要上传的文件,不能重名", required = true) List<MultipartFile> fileList) throws Exception {
        try {
            ConcurrentHashMap<String, Future<String>> mutiProcessMap = new ConcurrentHashMap<>();
            LoginUser loginUser = LoginHelper.getLoginUser();
            fileList.forEach(file -> {
                if (Objects.nonNull(file) && StringUtils.isNotBlank(file.getOriginalFilename())) {
                    mutiProcessMap.put(file.getOriginalFilename(), ossService.uploadFile(file, loginUser));
                }
            });
            BaseMap resultMap = new BaseMap();
            mutiProcessMap.forEach((fileName, urlFuture) -> {
                try {
                    resultMap.put(fileName, urlFuture.get());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
            return R.ok(resultMap);
        } catch (Exception e) {
            e.printStackTrace();
            return R.fail(e.getMessage());
        }
    }

    /**
     * fastdfs 上传单个大文件
     */
    @PostMapping(path = "/uploadBigFile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "上传单个大文件", description = "上传文件")
    @ResponseBody
    public R<BaseMap> uploadBigFile(@Parameter(description = "需要上传的文件,大小超过100MB", required = true) MultipartFile file) throws Exception {

        return R.ok(null);
    }


}
