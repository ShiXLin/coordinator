package com.lanternfish.form.domain.vo;

import com.lanternfish.common.annotation.Translation;
import com.lanternfish.common.constant.TransConstant;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.bson.types.ObjectId;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author Liam
 * @date 2024-4-24
 * @apiNote
 */
@Data
public class FormTemplateDataVo {

    @Schema(description = "表单模板id")
    @Translation(type= TransConstant.OBJECT_ID_TO_STRING)
    private ObjectId id;

    @Schema(description = "表单模板key")
    private String formTemplateKey;

    @Schema(description = "表单模板名称")
    private String formTemplateName;

    @Schema(description = "表单模板创建人id")
    private Long createUser;

    @Schema(description = "表单模板创建人名称")
    @Translation(type = TransConstant.USER_ID_TO_NAME, mapper = "createUser")
    private String createUserName;

    @Schema(description = "表单模板更新人id")
    private Long updateUser;

    @Schema(description = "表单模板更新人名称")
    @Translation(type = TransConstant.USER_ID_TO_NAME, mapper = "updateUser")
    private String updateUserName;

    @Schema(description = "表单模板创建时间")
    private LocalDateTime createTime;

    @Schema(description = "表单模板更新时间")
    private LocalDateTime updateTime;

    @Schema(description = "表单模板是否删除")
    @Translation(type = TransConstant.BOOLEAN_TO_CHINESE_BOOLEAN)
    private Boolean isDeleted;

    @Schema(description = "表单模板是否锁定")
    @Translation(type = TransConstant.BOOLEAN_TO_CHINESE_BOOLEAN)
    private Boolean locked;

    @Schema(description = "表单模板版本")
    private BigDecimal formTemplateVersion;

}
