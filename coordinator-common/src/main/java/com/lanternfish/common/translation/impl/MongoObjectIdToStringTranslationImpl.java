package com.lanternfish.common.translation.impl;

import com.lanternfish.common.annotation.TranslationType;
import com.lanternfish.common.constant.TransConstant;
import com.lanternfish.common.translation.TranslationInterface;
import lombok.AllArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;

/**
 * @author Liam
 * @date 2024-4-25
 * @apiNote
 */
@Component
@AllArgsConstructor
@TranslationType(type = TransConstant.OBJECT_ID_TO_STRING)
public class MongoObjectIdToStringTranslationImpl implements TranslationInterface<String> {

    @Override
    public String translation(Object key, String other) {
        if (key instanceof ObjectId) {
            return key.toString();
        }
        return null;
    }
}
