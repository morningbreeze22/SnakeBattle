export default {
  state: {
    is_record: false,
    a_steps:"",
    b_steps:"",
    record_loser: ""
  },
  getters: {
  },
  mutations: {
    updateIsRecord(state, is_record){
        state.is_record = is_record;
    },
    updateSteps(state,steps){
      state.a_steps=steps.a_steps;
      state.b_steps = steps.b_steps;
    },
    updateRecordLoser(state,loser){
      state.record_loser = loser;
    }
    
  },
  actions: {
  },
  modules: {
  }
}
