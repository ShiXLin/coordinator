package com.lanternfish.common.translation.impl;

import com.lanternfish.common.annotation.TranslationType;
import com.lanternfish.common.constant.TransConstant;
import com.lanternfish.common.core.service.DeptService;
import com.lanternfish.common.translation.TranslationInterface;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * 部门翻译实现
 *
 * @author Liam
 */
@Component
@AllArgsConstructor
@TranslationType(type = TransConstant.DEPT_ID_TO_NAME)
public class DeptNameTranslationImpl implements TranslationInterface<String> {

    private final DeptService deptService;

    @Override
    public String translation(Object key, String other) {
        return deptService.selectDeptNameByIds(key.toString());
    }
}
