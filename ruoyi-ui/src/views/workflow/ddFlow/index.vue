<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="流程名称" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入流程名称"
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
          v-hasPermi="['workflow:ddFlow:add']"
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
          v-hasPermi="['workflow:ddFlow:edit']"
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
          v-hasPermi="['workflow:ddFlow:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['workflow:ddFlow:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="ddFlowList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="钉钉流程主键" align="center" prop="id" v-if="true"/>
      <el-table-column label="流程名称" align="center" prop="name" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['workflow:ddFlow:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-brush"
            @click="handleDesigner(scope.row)"
            v-hasPermi="['workflow:ddFlow:edit']"
          >设计</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['workflow:ddFlow:remove']"
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

    <!-- 添加或修改钉钉流程对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="流程名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入流程名称" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button :loading="buttonLoading" type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <el-dialog :title="designerData.name" :visible.sync="designerOpen" append-to-body fullscreen>
      <ding-designer
        :key="designerOpen"
        style="border:1px solid rgba(0, 0, 0, 0.1)"
        ref="ddDesigner"
        v-loading="designerData.loading"
        :designerData = "flowJsonData"
        @save="onSaveDesigner"
      />
    </el-dialog>
  </div>
</template>

<script>
import { listDdFlow, getDdFlow, delDdFlow, addDdFlow, updateDdFlow } from "@/api/workflow/ddFlow";
import DingDesigner from '@/components/DingDesigner';

export default {
  name: "DdFlow",
  components: {
    DingDesigner
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
      // 钉钉流程表格数据
      ddFlowList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        name: undefined,
      },
      designerOpen: false,
      designerData: {
        loading: false,
        id: null,
        name: "",
        flowData: "",
      },
      flowJsonData: {},
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        id: [
          { required: true, message: "钉钉流程主键不能为空", trigger: "blur" }
        ],
        name: [
          { required: true, message: "流程名称不能为空", trigger: "blur" }
        ],
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询钉钉流程列表 */
    getList() {
      this.loading = true;
      listDdFlow(this.queryParams).then(response => {
        this.ddFlowList = response.rows;
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
        name: undefined,
        flowData: undefined,
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
      this.title = "添加钉钉流程";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.loading = true;
      this.reset();
      const id = row.id || this.ids
      getDdFlow(id).then(response => {
        this.loading = false;
        this.form = response.data;
        this.open = true;
        this.title = "修改钉钉流程";
      });
    },
    /** 钉钉设计按钮操作 */
    handleDesigner(row) {
       console.log("handleDesigner row",row);
       if (row.id) {
          let flowData = JSON.parse(row.flowData);
          console.log("handleDesigner flowData",flowData)
          if(flowData != null) {
            if(flowData.hasOwnProperty("processData")) {
             this.flowJsonData.processData = flowData.processData;
            }
            else {
               this.flowJsonData.processData = null;
            }
          }
          else {
             this.flowJsonData.processData = null;
          }
          this.designerData.id = row.id;
          this.designerData.name = row.name || "";
          this.designerOpen = true;
       }
    },
     /** 钉钉流程更新 */
    onSaveDesigner(flowData) {
      this.designerData.flowData = JSON.stringify(flowData);
      this.designerData.loading = true;
      console.log("onSaveDesigner this.designerData",this.designerData);
      updateDdFlow(this.designerData).then(() => {
        this.designerOpen = false;
        this.getList();
      }).finally(() => {
        this.designerData.loading = false;
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.buttonLoading = true;
          if (this.form.id != null) {
            updateDdFlow(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            }).finally(() => {
              this.buttonLoading = false;
            });
          } else {
            addDdFlow(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除钉钉流程编号为"' + ids + '"的数据项？').then(() => {
        this.loading = true;
        return delDdFlow(ids);
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
      this.download('workflow/ddFlow/export', {
        ...this.queryParams
      }, `ddFlow_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
