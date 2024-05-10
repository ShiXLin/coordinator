package com.lanternfish.common.translation.impl;

import cn.hutool.core.util.ObjectUtil;
import com.lanternfish.common.annotation.TranslationType;
import com.lanternfish.common.constant.TransConstant;
import com.lanternfish.common.exception.ServiceException;
import com.lanternfish.common.translation.TranslationInterface;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author Liam
 * @date 2024-4-25
 * @apiNote
 */
@Component
@AllArgsConstructor
@TranslationType(type = TransConstant.BOOLEAN_TO_CHINESE_BOOLEAN)
public class BooleanToDeleteOrNotTranslationImpl implements TranslationInterface<String> {

    @Override
    public String translation(Object key, String other) {
        if (key instanceof Boolean) {
            return (Boolean) key ? "是" : "否";
        } else {
            throw new ServiceException("translation error, key is invalid");
        }
    }
}
