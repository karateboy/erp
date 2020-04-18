import DashboardLayout from '../layout/DashboardLayout.vue'
// GeneralViews
import NotFound from '../pages/NotFoundPage.vue'

// Admin pages
import Overview from 'src/pages/Overview.vue'
import Notifications from 'src/pages/Notifications.vue'
import MergeDoc from "../pages/MergeDoc.vue"
import SearchDoc from "../pages/SearchDoc.vue"
import Login from "../pages/Login.vue"
import UserManagement from "../pages/UserManagement.vue"

//Junitay
import CustomerPage from '../pages/Customer.vue';
import ProductPage from '../pages/Product.vue';
import OrderPage from '../pages/Order.vue';
import WorkPage from '../pages/Work.vue';
import MaterialPage from '../pages/Material.vue';

import Vue from 'vue'
import VueRouter from 'vue-router'
Vue.use(VueRouter)

const routes = [
  {
    path: "/login",
    name: "login",
    component: Login
  },
  {
    path: '/',
    component: DashboardLayout,
    children: [
      {
        path: '',
        name: 'Overview',
        component: Overview
      },
      {
        path: 'merge',
        name: 'MergeDoc',
        component: MergeDoc
      },
      {
        path: 'search',
        name: 'SearchDoc',
        component: SearchDoc
      },
      {
        path: 'notifications',
        name: 'Notifications',
        component: Notifications
      },
      {
        path: 'userManagement',
        name: 'UserManagement',
        component: UserManagement
      },
      {
        path: 'customer',
        name: 'Customer',
        component: CustomerPage,
        children: [
          {
            path: ':id',
            name: 'CustomerDocument',
          }
        ]
      },
      {
        path: 'product',
        name: "Product",
        component: ProductPage,
        children: [
          {
            path: 'customer/:id',
            name: 'ProductCustomerList',
          },
          {
            path: ':id',
            name: 'ProductDocument',
          }
        ]
      },
      {
        path: 'order',
        name: "Order",
        component: OrderPage,
        children: [
          {
            path: 'customer/:id',
            name: 'OrderCustomerList',
          },
          {
            path: ':id',
            name: 'OrderDocument',
          }
        ]
      },
      {
        path: 'work',
        name: "Work",
        component: WorkPage,
        children: [
          {
            path: 'customer/:id',
            name: 'WorkCustomerList',
          },
          {
            path: ':id',
            name: 'WorkDocument',
          }
        ]
      },
      {
        path: 'material',
        name: "Material",
        component: MaterialPage,
        children: [
          {
            path: 'customer/:id',
            name: 'MaterialCustomerList',
          },
          {
            path: ':id',
            name: 'MaterialDocument',
          }
        ]
      }
    ]
  },
  /*{ path: '*', component: NotFound }*/
]


const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

export default router

