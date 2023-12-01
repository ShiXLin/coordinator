package com.ruoyi.workflow.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.ruoyi.common.annotation.ExcelDictFormat;
import com.ruoyi.common.convert.ExcelDictConvert;
import lombok.Data;
import java.util.Date;



/**
 * 钉钉流程视图对象 wf_dd_flow
 *
 * @author nbacheng
 * @date 2023-11-29
 */
@Data
@ExcelIgnoreUnannotated
public class WfDdFlowVo {

    private static final long serialVersionUID = 1L;

    /**
     * 钉钉流程主键
     */
    @ExcelProperty(value = "钉钉流程主键")
    private Long id;

    /**
     * 流程名称
     */
    @ExcelProperty(value = "流程名称")
    private String name;

    /**
     * 流程JSON数据
     */
    @ExcelProperty(value = "流程JSON数据")
    private String flowData;


}
