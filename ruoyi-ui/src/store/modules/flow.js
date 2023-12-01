const state = {
  nodeMap: new Map(),
  selectedNode: {},
  design:{},
  processConditions: [], // processConditions ���ڴ�������ͼ��Ҫ������
  formItemList: [], // ���̽ڵ��Ȩ�޿��ơ�������б�
}

const mutations = {
  selectedNode(state, val) {
    state.selectedNode = val
  },
  loadForm(state, val) {
    state.design = val
  },
  //����mutations�еķ����ĵ�һ������һ����state�������������ж�state�е�״̬�Ĳ���
  //�ڶ��������ǿ�ѡ���������ڵ��ø� mutations ������ʱ�򴫲�
  initPConditions ( state, data ) {
    state.processConditions = data
  },
  addPCondition ( state, data ) {
    if ( data.formId === null || data.formId === undefined ) return
    if ( !hasCondition( state, data.formId ) ) {
      state.processConditions.unshift( data )
    }
  },
  delPCondition ( state, formId ) {
    if ( formId === null || formId === undefined ) return
    let index = hasCondition( state, formId, true )
    let cons = state.processConditions
    index > -1 && cons.splice( index, 1 )
  },
  //  * ������е�����
  clearPCondition ( state ) {
    state.processConditions = []
  },
  updateFormItemList ( state, list ) {
    state.formItemList = list
  }
}

export default {
  namespaced: true,
  state,
  mutations
}
