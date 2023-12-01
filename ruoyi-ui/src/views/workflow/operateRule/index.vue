<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="流程配置主表ID" prop="configId">
        <el-input
          v-model="queryParams.configId"
          placeholder="请输入流程配置主表ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="操作类型" prop="opeType">
        <el-select v-model="queryParams.opeType" placeholder="请选择操作类型" clearable>
          <el-option
            v-for="dict in dict.type.wf_oper_type"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="操作名称" prop="opeName">
        <el-input
          v-model="queryParams.opeName"
          placeholder="请输入操作名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="是否启用1-启用0-关闭默认" prop="isEnable">
        <el-select v-model="queryParams.isEnable" placeholder="请选择是否启用1-启用0-关闭默认" clearable>
          <el-option
            v-for="dict in dict.type.wf_oper_status"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="排序" prop="sort">
        <el-input
          v-model="queryParams.sort"
          placeholder="请输入排序"
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
          v-hasPermi="['workflow:operateRule:add']"
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
          v-hasPermi="['workflow:operateRule:edit']"
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
          v-hasPermi="['workflow:operateRule:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['workflow:operateRule:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="operateRuleList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="流程操作主键" align="center" prop="id" v-if="true"/>
      <el-table-column label="流程配置主表ID" align="center" prop="configId" />
      <el-table-column label="操作类型" align="center" prop="opeType">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.wf_oper_type" :value="scope.row.opeType"/>
        </template>
      </el-table-column>
      <el-table-column label="操作名称" align="center" prop="opeName" />
      <el-table-column label="是否启用1-启用0-关闭默认" align="center" prop="isEnable">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.wf_oper_status" :value="scope.row.isEnable"/>
        </template>
      </el-table-column>
      <el-table-column label="排序" align="center" prop="sort" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['workflow:operateRule:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['workflow:operateRule:remove']"
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

    <!-- 添加或修改流程操作规则对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="流程配置主表ID" prop="configId">
          <el-input v-model="form.configId" placeholder="请输入流程配置主表ID" />
        </el-form-item>
        <el-form-item label="操作类型" prop="opeType">
          <el-select v-model="form.opeType" placeholder="请选择操作类型">
            <el-option
              v-for="dict in dict.type.wf_oper_type"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="操作名称" prop="opeName">
          <el-input v-model="form.opeName" placeholder="请输入操作名称" />
        </el-form-item>
        <el-form-item label="是否启用1-启用0-关闭默认" prop="isEnable">
          <el-select v-model="form.isEnable" placeholder="请选择是否启用1-启用0-关闭默认">
            <el-option
              v-for="dict in dict.type.wf_oper_status"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="排序" prop="sort">
          <el-input v-model="form.sort" placeholder="请输入排序" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button :loading="buttonLoading" type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listOperateRule, getOperateRule, delOperateRule, addOperateRule, updateOperateRule } from "@/api/workflow/operateRule";

export default {
  name: "OperateRule",
  dicts: ['wf_oper_type', 'wf_oper_status'],
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
      // 流程操作规则表格数据
      operateRuleList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        configId: undefined,
        opeType: undefined,
        opeName: undefined,
        isEnable: undefined,
        sort: undefined,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        id: [
          { required: true, message: "流程操作主键不能为空", trigger: "blur" }
        ],
        configId: [
          { required: true, message: "流程配置主表ID不能为空", trigger: "blur" }
        ],
        opeType: [
          { required: true, message: "操作类型不能为空", trigger: "change" }
        ],
        opeName: [
          { required: true, message: "操作名称不能为空", trigger: "blur" }
        ],
        isEnable: [
          { required: true, message: "是否启用1-启用0-关闭默认不能为空", trigger: "change" }
        ],
        sort: [
          { required: true, message: "排序不能为空", trigger: "blur" }
        ],
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询流程操作规则列表 */
    getList() {
      this.loading = true;
      listOperateRule(this.queryParams).then(response => {
        this.operateRuleList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        id: undefined,
        configId: undefined,
        opeType: undefined,
        opeName: undefined,
        isEnable: undefined,
        sort: undefined,
        createBy: undefined,
        createTime: undefined,
        updateBy: undefined,
        updateTime: undefined
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
      this.title = "添加流程操作规则";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.loading = true;
      this.reset();
      const id = row.id || this.ids
      getOperateRule(id).then(response => {
        this.loading = false;
        this.form = response.data;
        this.open = true;
        this.title = "修改流程操作规则";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.buttonLoading = true;
          if (this.form.id != null) {
            updateOperateRule(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            }).finally(() => {
              this.buttonLoading = false;
            });
          } else {
            addOperateRule(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            }).finally(() => {
              this.buttonLoading = false;
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$modal.confirm('是否确认删除流程操作规则编号为"' + ids + '"的数据项？').then(() => {
        this.loading = true;
        return delOperateRule(ids);
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
      this.download('workflow/operateRule/export', {
        ...this.queryParams
      }, `operateRule_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
