<template>
  <div class="page">
    <section class="page__content" v-if="mockData">
      <Process
        ref="processDesign"
        :conf="mockData.processData"
        tabName="processDesign"
        @startNodeChange="onStartChange"/>
    </section>
    <div class="publish">
      <el-button size="mini" type="primary" @click="preview"><i class="el-icon-view"></i>预览</el-button>
      <el-button size="mini" type="primary" @click="publish"><i class="el-icon-s-promotion"></i>发布</el-button>
    </div>
  </div>
</template>

<script>
// @ is an alias to /src
import Process from "./components/Process";
import { GET_MOCK_CONF } from '@/api/workflow/imdd'
const beforeUnload = function (e) {
  var confirmationMessage = '离开网站可能会丢失您编辑得内容';
  (e || window.event).returnValue = confirmationMessage;     // Gecko and Trident
  return confirmationMessage;                                // Gecko and WebKit
}

export default {
  name: "Home",
  props: {
    title: {
      type: String,
      default: '补卡申请'
    }
  },
  data() {
    return {
      mockData: null, // 可选择诸如 $route.param，Ajax获取数据等方式自行注入
      activeStep: "processDesign", // 激活的步骤面板
      steps: [
        { label: "流程设计", key: "processDesign" }
      ]
    };
  },
  beforeRouteEnter(to, from, next){
    window.addEventListener('beforeunload', beforeUnload)
    next()
  },
  beforeRouteLeave(to, from, next){
    window.removeEventListener('beforeunload', beforeUnload)
    next()
  },
  computed:{
    translateX () {
      return `translateX(${this.steps.findIndex(t => t.key === this.activeStep) * 100}%)`
    }
  },
  mounted() {
    GET_MOCK_CONF().then(data => this.mockData = data);
  },
  methods: {
    changeSteps(item) {
      this.activeStep = item.key;
    },
    preview() {
      
    },
    publish() {
      const getCmpData = name => this.$refs[name].getData()
      // processDesign 返回的是Promise 因为要做校验
      const p3 = getCmpData('processDesign')
      Promise.all([p3])
      .then(res => {
        const param = {
          processData: res[2].formData
        }
        this.sendToServer(param)
      })
      .catch(err => {
        err.target && (this.activeStep = err.target)
        err.msg && this.$message.warning(err.msg)
      })
    },
    sendToServer(param){
      this.$notify({
        title: '数据已整合完成',
        message: '请在控制台中查看数据输出',
        position: 'bottom-right'
      });
      console.log('配置数据', param)
    },
    exit() {
      this.$confirm('离开此页面您得修改将会丢失, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.$message({
            type: 'success',
            message: '模拟返回!'
          });
        }).catch(() => { });
    },
    /**
     * 同步基础设置发起人和流程节点发起人
     */
    onInitiatorChange (val, labels) {
      const processCmp = this.$refs.processDesign
      const startNode = processCmp.data
      startNode.properties.initiator = val['dep&user']
      startNode.content =  labels  || '所有人'
      processCmp.forceUpdate()
    },
    /**
     * 监听 流程节点发起人改变 并同步到基础设置 发起人数据
     */
    onStartChange(node){
      const basicSetting = this.$refs.basicSetting
      basicSetting.formData.initiator = { 'dep&user': node.properties.initiator }
    }
  },
  components: {
    Process,
  }
};
</script>
<style lang="stylus" scoped>
.publish {
    position: absolute;
    top: 15px;
    right: 20px;
    z-index: 1000;

    i {
      margin-right: 6px;
    }

    button {
      border-radius: 15px;
    }
  }

</style>
