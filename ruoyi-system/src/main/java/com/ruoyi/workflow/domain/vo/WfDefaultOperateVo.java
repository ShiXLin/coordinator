package com.ruoyi.workflow.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.ruoyi.common.annotation.ExcelDictFormat;
import com.ruoyi.common.convert.ExcelDictConvert;
import lombok.Data;
import java.util.Date;



/**
 * 流程默认操作视图对象 wf_default_operate
 *
 * @author nbacheng
 * @date 2023-11-23
 */
@Data
@ExcelIgnoreUnannotated
public class WfDefaultOperateVo {

    private static final long serialVersionUID = 1L;

    /**
     * 流程默认操作主键
     */
    @ExcelProperty(value = "流程默认操作主键")
    private Long id;

    /**
     * 操作类型
     */
    @ExcelProperty(value = "操作类型", converter = ExcelDictConvert.class)
    @ExcelDictFormat(dictType = "sys_flow_oper_type")
    private String opeType;

    /**
     * 操作名称
     */
    @ExcelProperty(value = "操作名称")
    private String opeName;

    /**
     * 是否启用1-启用0-关闭默认
     */
    @ExcelProperty(value = "是否启用1-启用0-关闭默认")
    private String isEnable;

    /**
     * 排序
     */
    @ExcelProperty(value = "排序")
    private Long sort;


}
