import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    isAuthenticated: false,
    userInfo: {
      _id: "",
      groups: []
    }
  },
  mutations: {
    updateUserInfo: (state, payload) => {
      state.isAuthenticated = true;
      state.userInfo._id = payload._id
      state.userInfo.groups.splice(0, state.userInfo.groups.length);
      for (let group of payload.groups) {
        state.userInfo.groups.push(group)
      }
    }
  },
  actions: {
    updateUserInfo({ commit }) {

    }
  },
  modules: {
  }
})
