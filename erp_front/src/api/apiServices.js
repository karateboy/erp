import Vue from "vue";
import axios from "axios";
import VueAxios from "vue-axios";

export const ApiService = {
  init() {
    Vue.use(VueAxios, axios);
    Vue.axios.defaults.baseURL = process.env.NODE_ENV === 'development' ? "http://localhost:9000/" : "/";
  },

  //   setHeader() {
  //     Vue.axios.defaults.headers.common[
  //       "Authorization"
  //     ] = `Token ${JwtService.getToken()}`;
  //   },

  query(resource, params) {
    console.log(`[API Services] query GET resource:[`, resource, `] params=`, params)
    return Vue.axios.get(resource, params.params).catch(error => {
      throw new Error(`[RWV] ApiService ${error}`);
    });
  },

  get(resource, slug = "") {
    console.log(`[API Services] GET resource:[`, resource, `]`)
    return Vue.axios.get(`${resource}/${slug}`).catch(error => {
      throw new Error(`[RWV] ApiService ${error}`);
    });
  },

  post(resource, params) {
    console.log(`[API Services] POST resource:[`, resource, `] params=`, params)
    return Vue.axios.post(`${resource}`, params);
  },

  update(resource, slug, params) {
    console.log(`[API Services] update PUT resource:[`, resource, `] params=`, params)
    return Vue.axios.put(`${resource}/${slug}`, params);
  },

  put(resource, params) {
    console.log(`[API Services] PUT resource:[`, resource, `] params=`, params)
    return Vue.axios.put(`${resource}`, params);
  },

  delete(resource) {
    console.log(`[API Services] DELETE resource:[`, resource, `]`)
    return Vue.axios.delete(resource).catch(error => {
      throw new Error(`[RWV] ApiService ${error}`);
    });
  }
};

export default ApiService;
