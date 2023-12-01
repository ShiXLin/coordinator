<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="流程模型ID" prop="modelId">
        <el-input
          v-model="queryParams.modelId"
          placeholder="请输入流程模型ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="节点Key" prop="nodeKey">
        <el-input
          v-model="queryParams.nodeKey"
          placeholder="请输入节点Key"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="节点名称" prop="nodeName">
        <el-input
          v-model="queryParams.nodeName"
          placeholder="请输入节点名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="表单Key" prop="formKey">
        <el-input
          v-model="queryParams.formKey"
          placeholder="请输入表单Key"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['workflow:flowConfig:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['workflow:flowConfig:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['workflow:flowConfig:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['workflow:flowConfig:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="flowConfigList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="流程配置主表主键" align="center" prop="id" v-if="true"/>
      <el-table-column label="流程模型ID" align="center" prop="modelId" />
      <el-table-column label="节点Key" align="center" prop="nodeKey" />
      <el-table-column label="节点名称" align="center" prop="nodeName" />
      <el-table-column label="表单Key" align="center" prop="formKey" />
      <el-table-column label="应用类型" align="center" prop="appType" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['workflow:flowConfig:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleRule(scope.row)"
            v-hasPermi="['workflow:flowConfig:edit']"
          >规则</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['workflow:flowConfig:remove']"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 修改流程规则对话框 -->
    <el-dialog :title="title" :visible.sync="ruleOpen" width="600px" append-to-body>
        <el-tabs tab-position="top" v-model="activeName" :value="'form'" @tab-click="changeTab">
          <el-tab-pane label="表单配置" name="form" >
            <el-table :header-cell-style="{background:'#f5f6f6'}" :data="customRuleList" border style="width: 100%">
              <el-table-column prop="title" show-overflow-tooltip label="表单字段">
                <template slot-scope="scope">
                   <span v-if="scope.row.colCode" style="color: #c75450"> * </span>
                  <span>{{ scope.row.colName }}</span>
                </template>
              </el-table-column>
              <el-table-column prop="readOnly" label="只读" width="80">
                <template slot="header" slot-scope="scope">
                  <el-radio label="1" v-model="permSelect" @change="allSelect('1')">只读</el-radio>
                </template>
                <template slot-scope="scope">
                  <el-radio v-model="scope.row.attribute" label="1" :name="scope.row.colCode"></el-radio>
                </template>
              </el-table-column>
              <el-table-column prop="editable" label="可编辑" width="90">
                <template slot="header" slot-scope="scope">
                  <el-radio label="2" v-model="permSelect" @change="allSelect('2')">可编辑</el-radio>
                </template>
                <template slot-scope="scope">
                  <el-radio v-model="scope.row.attribute" label="2" :name="scope.row.colCode"></el-radio>
                </template>
              </el-table-column>
              <el-table-column prop="hide" label="隐藏" width="80">
                <template slot="header" slot-scope="scope">
                  <el-radio label="0" v-model="permSelect" @change="allSelect('0')">隐藏</el-radio>
                </template>
                <template slot-scope="scope">
                  <el-radio v-model="scope.row.attribute" label="0" :name="scope.row.colCode"></el-radio>
                </template>
              </el-table-column>
            </el-table>
          </el-tab-pane>

          <el-tab-pane label="操作权限" name="operate">
            <el-table :header-cell-style="{background:'#f5f6f6'}" :data="operateRuleList" border style="width: 100%">
              <el-table-column prop="title" show-overflow-tooltip label="表单字段">
                <template slot-scope="scope">
                   <span v-if="scope.row.id" style="color: #c75450"> * </span>
                  <span>{{ scope.row.opeName }}</span>
                </template>
              </el-table-column>
              <el-table-column prop="hide" label="关闭" width="100">
                <template slot="header" slot-scope="scope">
                  <el-switch v-model="operateSelect" :active-value="'1'" :inactive-value="'0'" active-text="关闭"
                   inactive-text="开启" @change="allOperate"></el-switch>
                </template>
                <template slot-scope="scope">
                  <el-switch ref="elswitch" v-model="scope.row.isEnable" :active-value="'1'"
                   :inactive-value="'0'" active-text="关闭" inactive-text="开启" @change="changeOperate(scope.row)"></el-switch>
                </template>
              </el-table-column>
            </el-table>
          </el-tab-pane >

        </el-tabs>
        <div slot="footer" class="dialog-footer">
          <el-button :loading="buttonLoading" type="primary" @click="submitRuleForm">确 定</el-button>
          <el-button @click="cancel">取 消</el-button>
        </div>
    </el-dialog>
  </div>
</template>

<script>
import { listFlowConfig, getFlowConfig, delFlowConfig, addFlowConfig,
         updateFlowConfig, getConfigRule, updateConfigRule } from "@/api/workflow/flowConfig";

export default {
  name: "FlowConfig",
  props: {
    flowConfigData: {
      type: Object,
      required: true
    }
  },
  data() {
    return {
      // 按钮loading
      buttonLoading: false,
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 流程配置主表格数据
      flowConfigList: [],
      //自定义业务表单规则数据
      customRuleList: [],
      //操作按钮规则数据
      operateRuleList: [],
      permSelect: '',
      operateSelect: 0,
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        modelId: undefined,
        nodeKey: undefined,
        nodeName: undefined,
        formKey: undefined,
        appType: undefined
      },
      ruleOpen: false,
      // 表单参数
      form: {},
      activeName:'', //获取当然tabname
      // 表单校验
      rules: {
        id: [
          { required: true, message: "流程配置主表主键不能为空", trigger: "blur" }
        ],
        modelId: [
          { required: true, message: "流程模型ID不能为空", trigger: "blur" }
        ],
        nodeKey: [
          { required: true, message: "节点Key不能为空", trigger: "blur" }
        ],
        nodeName: [
          { required: true, message: "节点名称不能为空", trigger: "blur" }
        ],
        formKey: [
          { required: true, message: "表单Key不能为空", trigger: "blur" }
        ],
        appType: [
          { required: true, message: "应用类型不能为空", trigger: "change" }
        ]
      }
    };
  },
  created() {
    localStorage.setItem("dataName", "master");
    this.getList();
  },
  methods: {
    /** 查询流程配置主表列表 */
    getList() {
      this.loading = true;
      this.queryParams.modelId = this.flowConfigData.modelId;
      console.log("getList this.queryParams",this.queryParams);
      listFlowConfig(this.queryParams).then(response => {
        this.flowConfigList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    allSelect(type) {
      this.permSelect = type
      this.customRuleList.forEach(f => f.attribute = type)
    },
    allOperate() {
      if(this.operateSelect === '0') {
        this.operateRuleList.forEach(f => f.isEnable = "0");
      }
      else if( this.operateSelect === '1') {
        this.operateRuleList.forEach(f => f.isEnable = "1");
      }
    },
    changeOperate(row) {
      console.log("changeOperate row=",row);
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 取消按钮
    ruleCancel() {
      this.ruleOpen = false;
      this.ruleReset();
    },
    // 表单重置
    reset() {
      this.form = {
        id: undefined,
        modelId: undefined,
        nodeKey: undefined,
        nodeName: undefined,
        formKey: undefined,
        appType: undefined
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加流程配置主表";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.loading = true;
      this.reset();
      const id = row.id || this.ids
      getFlowConfig(id).then(response => {
        this.loading = false;
        this.form = response.data;
        this.open = true;
        this.title = "修改流程配置主表";
      });
    },
    /** 修改规则操作 */
    handleRule(row) {
      this.loading = true;
      console.log("handleRule row=",row);
      getConfigRule(row).then(response => {
        this.loading = false;
        console.log("getConfigRule response=",response);
        this.customRuleList = response.data.customRuleVoList;
        this.operateRuleList = response.data.operateRuleVoList;
        this.activeName = "form";
        this.ruleOpen = true;
        this.title = "修改节点规则";
      });
    },
    changeTab(tab, event) {
      console.log("changeTab tab=",tab);
      if(tab.name === 'form') {

      }
    },
    /** 提交按钮 */
    submitRuleForm() {
      this.buttonLoading = true;
      let ruleVo = {
        customRuleVoList: this.customRuleList,
        operateRuleVoList: this.operateRuleList
      }
      updateConfigRule(ruleVo).then(response => {
        this.$modal.msgSuccess("修改成功");
        this.ruleOpen = false;
        this.getList();
      }).finally(() => {
        this.buttonLoading = false;
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$modal.confirm('是否确认删除流程配置主表编号为"' + ids + '"的数据项？').then(() => {
        this.loading = true;
        return delFlowConfig(ids);
      }).then(() => {
        this.loading = false;
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {
      }).finally(() => {
        this.loading = false;
      });
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('workflow/flowConfig/export', {
        ...this.queryParams
      }, `flowConfig_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>

<style lang="less" scoped>
::v-deep .el-switch__core {
      //width: 44px;
       width: 60px !important;
      height: 24px;
      border-radius: 100px;
      border: none;
    }
    ::v-deep .el-switch__core::after {
      width: 20px;
      height: 20px;
      top: 2px;
    }
    ::v-deep .el-switch.is-checked .el-switch__core::after {
      margin-left: -21px;
    }
    /*关闭时文字位置设置*/
    ::v-deep .el-switch__label--right {
      position: absolute;
      z-index: 1;
      right: 6px;
      margin-left: 0px;
      color: rgba(255, 255, 255, 0.9019607843137255);
      span {
        font-size: 12px;
      }
    }
    /* 激活时另一个文字消失 */
    ::v-deep .el-switch__label.is-active {
      display: none;
    }
    /*开启时文字位置设置*/
    ::v-deep .el-switch__label--left {
      position: absolute;
      z-index: 1;
      left: 5px;
      margin-right: 0px;
      color: rgba(255, 255, 255, 0.9019607843137255);
      span {
        font-size: 12px;
      }
    }
</style>
