package com.lanternfish.common.translation.impl;

import com.lanternfish.common.annotation.TranslationType;
import com.lanternfish.common.constant.TransConstant;
import com.lanternfish.common.core.service.DictService;
import com.lanternfish.common.translation.TranslationInterface;
import com.lanternfish.common.utils.StringUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * 字典翻译实现
 *
 * @author Liam
 */
@Component
@AllArgsConstructor
@TranslationType(type = TransConstant.DICT_TYPE_TO_LABEL)
public class DictTypeTranslationImpl implements TranslationInterface<String> {

    private final DictService dictService;

    @Override
    public String translation(Object key, String other) {
        if (key instanceof String && StringUtils.isNotBlank(other)) {
            return dictService.getDictLabel(other, key.toString());
        }
        return null;
    }
}
