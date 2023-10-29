const state = {
  nodeMap: new Map(),
  selectedNode: {},
  design:{},
}

const mutations = {
  selectedNode(state, val) {
    state.selectedNode = val
  },
  loadForm(state, val) {
    state.design = val
  },
}

export default {
  namespaced: true,
  state,
  mutations
}
