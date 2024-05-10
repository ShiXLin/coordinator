package com.lanternfish.form.domain;

import com.lanternfish.common.core.domain.mongodb.BaseMongoData;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @author Liam
 * @date 2024-4-24
 * @apiNote
 */
@Data
@Document("from_template_document")
@EqualsAndHashCode(callSuper = true)
public class FormTemplateData extends BaseMongoData {

    /**
     * 模板名称
     */
    private String formTemplateName;

    /**
     * 模板key
     */
    private String formTemplateKey;

    /**
     * 模板内容
     */
    private List<Map<String, Object>> formTemplateContent;

    /**
     * 是否锁定
     */
    private Boolean locked;


    /**
     * 模板版本
     */
    private BigDecimal formTemplateVersion;

}
