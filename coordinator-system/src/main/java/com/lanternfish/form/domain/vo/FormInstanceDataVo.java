package com.lanternfish.form.domain.vo;

import com.lanternfish.common.annotation.Translation;
import com.lanternfish.common.constant.TransConstant;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.bson.types.ObjectId;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * @author Liam
 * @date 2024-4-25
 * @apiNote
 *   表单实例数据业务对象
 */
@Data
public class FormInstanceDataVo {

    @Schema(description = "表单模板id")
    @Translation(type= TransConstant.OBJECT_ID_TO_STRING)
    private ObjectId id;


    @Schema(description = "表单模板id")
    @Translation(type= TransConstant.OBJECT_ID_TO_STRING)
    private ObjectId formTemplateId;


    @Schema(description = "表单数据")
    private Map<String,Object> formData;


    @Schema(description = "创建人id")
    private Long createUser;

    @Schema(description = "创建人名称")
    @Translation(type = TransConstant.USER_ID_TO_NAME, mapper = "createUser")
    private String createUserName;


    @Schema(description = "更新人")
    private Long updateUser;

    @Schema(description = "更新人名称")
    @Translation(type = TransConstant.USER_ID_TO_NAME, mapper = "updateUser")
    private String updateUserName;


    @Schema(description = "创建时间")
    private LocalDateTime createTime;


    @Schema(description = "更新时间")
    private LocalDateTime updateTime;


    @Schema(description = "是否删除")
    private Boolean isDelete;

}
