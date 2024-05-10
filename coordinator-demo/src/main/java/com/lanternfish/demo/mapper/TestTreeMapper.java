package com.lanternfish.demo.mapper;

import com.lanternfish.common.annotation.DataColumn;
import com.lanternfish.common.annotation.DataPermission;
import com.lanternfish.common.core.mapper.BaseMapperPlus;
import com.lanternfish.demo.domain.TestTree;
import com.lanternfish.demo.domain.vo.TestTreeVo;

/**
 * 测试树表Mapper接口
 *
 * @author Liam
 * @date 2021-07-26
 */
@DataPermission({
    @DataColumn(key = "deptName", value = "dept_id"),
    @DataColumn(key = "userName", value = "user_id")
})
public interface TestTreeMapper extends BaseMapperPlus<TestTreeMapper, TestTree, TestTreeVo> {

}
