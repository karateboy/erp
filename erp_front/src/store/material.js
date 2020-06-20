import { ApiService } from '@/api/apiServices'

const moduleApi = {
    list(type, params) {
        return ApiService.query("/material", {
            params: params
        });
    },
    // query(type, params) {
    //     return ApiService.query("/customer" + (type === "feed" ? "/feed" : ""), {
    //         params: params
    //     });
    // },
    // get(slug) {
    //     return ApiService.get("/customer", slug);
    // },
    // create(params) {
    //     return ApiService.post("/customer", { article: params });
    // },
    // update(slug, params) {
    //     return ApiService.update("/customer", slug, { article: params });
    // },
    // destroy(slug) {
    //     return ApiService.delete(`/customer/${slug}`);
    // }
};

const namespaced = true;

const state = {
    list: []
};

const getters = {
    list: state => {
        return state.list;
    }
};

const mutations = {
    SET_LIST(state, data) {
        state.list = data;
    },
};

const actions = {
    FETCH_LIST({ commit }) {
        moduleApi.list("")
        .then(response => {
            let productList = []
            for (let c of response.data) {
                productList.push(c)
            }
            commit('SET_LIST', productList);
        })
    },
};

export default {
    namespaced,
    state,
    getters,
    actions,
    mutations
};