package com.lanternfish.form.domain;

import com.lanternfish.common.core.domain.mongodb.BaseMongoData;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

/**
 * @author Liam
 * @date 2024-4-25
 * @apiNote
 *   表单实例数据业务对象
 */
@Data
@Document("form_instance_data")
@EqualsAndHashCode(callSuper = true)
public class FormInstanceData extends BaseMongoData {


    /**
     * 表单模板ID
     */
    private String formTemplateId;

    /**
     * 表单数据
     */
    private Map<String,Object> formData;


}
