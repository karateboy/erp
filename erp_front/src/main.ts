/*!

 =========================================================
 * Vue Light Bootstrap Dashboard - v2.0.0 (Bootstrap 4)
 =========================================================

 * Product Page: http://www.creative-tim.com/product/light-bootstrap-dashboard
 * Copyright 2019 Creative Tim (http://www.creative-tim.com)
 * Licensed under MIT (https://github.com/creativetimofficial/light-bootstrap-dashboard/blob/master/LICENSE.md)

 =========================================================

 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

 */
import Vue from 'vue'
import VueRouter from 'vue-router'
import { BootstrapVue, BootstrapVueIcons } from 'bootstrap-vue'
import axios from 'axios'
import App from './App.vue'
import LightBootstrap from './light-bootstrap-main'
import routes from './routes/routes'
//import './registerServiceWorker'
import store from './store/index'
import 'bootstrap/dist/css/bootstrap.css'
import 'bootstrap-vue/dist/bootstrap-vue.css'
import { library } from '@fortawesome/fontawesome-svg-core'
import {
  faLayerGroup, faCompass, faAngleDown, faAngleRight,
  faSearchPlus, faDownload, faSquare, faCheckSquare, faFileExcel, faHome,
  faAddressCard, faUserCog, faTasks, faUser
} from '@fortawesome/free-solid-svg-icons'
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome'

axios.defaults.withCredentials = true
axios.defaults.baseURL = process.env.NODE_ENV === 'development' ? "http://localhost:9000/" : "/";

library.add(faLayerGroup, faCompass, faAngleDown, faAngleRight, faSearchPlus,
  faDownload, faSquare, faCheckSquare, faFileExcel, faHome, faAddressCard,
  faUserCog, faTasks, faUser)
// plugin setup
Vue.component('font-awesome-icon', FontAwesomeIcon)
Vue.use(BootstrapVue)
Vue.use(BootstrapVue)
Vue.use(VueRouter)
Vue.use(LightBootstrap)

// configure router
const router = new VueRouter({
  routes, // short for routes: routes
  linkActiveClass: 'nav-item active',
  scrollBehavior: (to) => {
    if (to.hash) {
      return { selector: to.hash }
    } else {
      return { x: 0, y: 0 }
    }
  }
})

router.beforeEach((to, from, next) => {
  if (to.name == 'login' || store.state.isAuthenticated)
    next()
  else
    next({ name: 'login' })
})
/* eslint-disable no-new */
new Vue({
  el: '#app',
  render: h => h(App),
  store,
  router
})
