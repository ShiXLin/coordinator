package com.lanternfish.common.translation.impl;

import cn.hutool.core.util.NumberUtil;
import com.lanternfish.common.annotation.TranslationType;
import com.lanternfish.common.constant.TransConstant;
import com.lanternfish.common.translation.TranslationInterface;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * 金额翻译实现
 *
 * @author Liam
 */
@Component
@AllArgsConstructor
@TranslationType(type = TransConstant.NUMBER_TO_THOUSANDS_STRING_MONEY)
public class NumberToThousandsStringMoneyImpl implements TranslationInterface<String> {


    @Override
    public String translation(Object key, String other) {

        if (key == null) {
            return "0.00";
        }

        if (!NumberUtil.isNumber(key.toString())) {
            return "0.00";
        }

        BigDecimal bigDecimal = new BigDecimal(key.toString());
        return NumberUtil.decimalFormatMoney(bigDecimal.doubleValue());
    }
}
