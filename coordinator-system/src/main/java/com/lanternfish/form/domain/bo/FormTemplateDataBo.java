package com.lanternfish.form.domain.bo;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * @author Liam
 * @date 2024-4-24
 * @apiNote
 * 表单模板数据交互对象
 */
@Data
public class FormTemplateDataBo implements Serializable {

    /**
     * id
     */
    private String id;
    /**
     * 表单模型名称
     */
    @NotNull(message = "表单模型名称不能为空")
    private String formTemplateName;
    /**
     * 表单模型key
     */
    @NotNull(message = "表单模型key不能为空")
    private String formTemplateKey;
    /**
     * 创建人
     */
    private Long createUser;
    /**
     * 更新人
     */
    private Long updateUser;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
    /**
     * 表单模型内容
     */
    @NotNull(message = "表单模型内容不能为空")
    private List<Map<String, Object>> formTemplateContent;
    /**
     * 是否删除
     */
    @NotNull(message = "是否删除不能为空")
    private Boolean isDeleted;
    /**
     * 版本号
     */
    @NotNull(message = "版本号不能为空")
    @Pattern(regexp = "\\d+\\.0", message = "版本号格式不正确")
    private BigDecimal formTemplateVersion;
}
