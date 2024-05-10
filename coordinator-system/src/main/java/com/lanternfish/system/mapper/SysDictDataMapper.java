package com.lanternfish.system.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lanternfish.common.constant.UserConstants;
import com.lanternfish.common.core.domain.entity.SysDictData;
import com.lanternfish.common.core.mapper.BaseMapperPlus;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 字典表 数据层
 *
 * @author Liam
 */
public interface SysDictDataMapper extends BaseMapperPlus<SysDictDataMapper, SysDictData, SysDictData> {

    default List<SysDictData> selectDictDataByType(String dictType) {
        return selectList(
            new LambdaQueryWrapper<SysDictData>()
                .eq(SysDictData::getStatus, UserConstants.DICT_NORMAL)
                .eq(SysDictData::getDictType, dictType)
                .orderByAsc(SysDictData::getDictSort));
    }

    @Select("SELECT * FROM sys_dict_data WHERE dict_type = #{ dictType}")
    List<SysDictData> selectDictDataListByDictType(@Param("dictType") String dictType);
}
