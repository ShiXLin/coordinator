package com.lanternfish.form.domain.bo;

import com.lanternfish.common.annotation.Translation;
import com.lanternfish.common.constant.TransConstant;
import lombok.Data;
import org.bson.types.ObjectId;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * @author Liam
 * @date 2024-4-25
 * @apiNote
 *   表单实例数据业务对象
 */
@Data
public class FormInstanceDataBo {

    private String id;

    /**
     * 表单模板ID
     */
    @NotNull(message = "表单模板不能为空")
    private String formTemplateId;

    /**
     * 表单数据
     */
    @NotNull(message = "表单数据不能为空")
    private Map<String,Object> formData;

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
     * 是否删除
     */
    private Boolean isDelete;



}
