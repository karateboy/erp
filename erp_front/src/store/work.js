import { ApiService } from '@/api/apiServices'

const moduleApi = {
    list(collection, params) {
        return ApiService.query(collection, {
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
    list: [],
    filtered_list:[],
    appDocument:[]
};

const getters = {
    list: state => {
        return state.list;
    },

    appDocument: state => {
        return state.appDocument;
    },

    filtered_list: state => {
        return state.filtered_list;
    }
};

const mutations = {
    SET_LIST(state, data) {
        state.list.splice(0, state.list.length);
        state.list = data;
        // for(let item of data){
        //    state.list.push(item)
        // }
     },
    SET_FILTERED_LIST(state, data) {
        state.list.splice(0, state.list.length);
        state.list = data;
        // for(let item of data){
        //    state.list.push(item)
        // }
    },
    SET_DOCUMENT(state, data) {
        // console.log(data);
        state.appDocument = data;
        // state.list.splice(0, state.appDocument.length);
        // for(let item of data){
        //    state.appDocument.push(item)
        // }
     },

};

const actions = {
    FETCH_LIST({ commit }, db) {
        moduleApi.list(db, "")
        .then(response => {
            let productList = []
            for (let c of response.data) {
                productList.push(c)
            }
            commit('SET_LIST', productList);
        })
    },
    FETCH_FILTERED_LIST({ commit }, db) {
        console.log(db);
        moduleApi.list(db, "")
            .then(response => {
                commit('SET_FILTERED_LIST', response.data);
            })
    },
    FETCH_DOCUMENT({ commit }, db) {
        moduleApi.list(db, "")
            .then(response => {
                commit('SET_DOCUMENT', response.data);
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