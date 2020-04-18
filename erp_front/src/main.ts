import Vue from 'vue'
import 'bootstrap/dist/css/bootstrap.css'
import 'bootstrap-vue/dist/bootstrap-vue.css'
import { BootstrapVue, BootstrapVueIcons } from 'bootstrap-vue'
import axios from 'axios'
import App from './App.vue'
import LightBootstrap from './light-bootstrap-main'
import router from './routes'
import store from './store/index'
import { library } from '@fortawesome/fontawesome-svg-core'
import {
  faLayerGroup, faCompass, faAngleDown, faAngleRight,
  faSearchPlus, faDownload, faSquare, faCheckSquare, faFileExcel, faHome,
  faAddressCard, faUserCog, faTasks, faUser, faVoteYea
} from '@fortawesome/free-solid-svg-icons'
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome'
import apiService from "./api/apiServices"

axios.defaults.withCredentials = true
axios.defaults.baseURL = process.env.NODE_ENV === 'development' ? "http://localhost:9000/" : "/";

library.add(faLayerGroup, faCompass, faAngleDown, faAngleRight, faSearchPlus,
  faDownload, faSquare, faCheckSquare, faFileExcel, faHome, faAddressCard,
  faUserCog, faTasks, faUser, faVoteYea)
// plugin setup
Vue.component('font-awesome-icon', FontAwesomeIcon)
Vue.use(BootstrapVue)
Vue.use(BootstrapVueIcons)
Vue.use(LightBootstrap)

apiService.init()
router.beforeEach((to, from, next) => {
  if (store.state.isAuthenticated)
    next()
  else {
    if (to.name == 'login')
      next()
    else
      next({ name: 'login' })
  }
})
/* eslint-disable no-new */
new Vue({
  render: h => h(App),
  store,
  router
}).$mount("#app")
