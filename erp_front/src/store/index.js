import Vue from 'vue'
import Vuex from 'vuex'
import customer from "./customer"
import order from "./order"
import material from "./material"
import product from "./product"
import work from "./work"
Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    isAuthenticated: false,
    userInfo: {
      _id: "",
      name: "",
      groups: []
    }
  },
  mutations: {
    updateUserInfo: (state, payload) => {
      state.isAuthenticated = true;
      state.userInfo._id = payload._id
      state.userInfo.name = payload.name
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
    customer,
    material,
    order,
    product,
    work
  }
})
