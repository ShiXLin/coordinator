export default {
  start: {
    type: "start",
    content: "所有人",
    properties: { title: '发起人', initiator: 'ALL' }
  },
  approver: {
    type: "approver",
    content: "请设置审批人",
    properties: { title: '审批人' }
  },
  copy:{
    type: 'copy',
    content: '发起人自选',
    properties: {
      title: '抄送人',
      menbers: [],
      userOptional: true
    }
  },
  condition: {
    type: "condition",
    content: "请设置条件",
    properties: { title: '条件', conditions: [], initiator: null }
  },
  concurrent: {
    type: "concurrent",
    content: "并行任务(同时进行)",
    properties: { title: '分支', concurrents: [], initiator: null }
  },
  delay: {
    type: "delay",
    content: "等待0分钟",
    properties: { title: '延时处理' }
  },
  trigger: {
    type: "trigger",
    content: "请设置触发器",
    properties: { title: '触发器' }
  },
  branch: { type: "branch", content: "", properties: {} },
  empty: { type: "empty", content: "", properties: {} }
}
