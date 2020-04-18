<template>
  <b-container fluid>
    <b-row>
      <b-col lg="6" sm="10">
        <custom-input v-model="appDocument._id" index="id" label="id"></custom-input>
      </b-col>
      <b-col lg="6" sm="10">
        <custom-input v-model="appDocument.display" index="display" label="display"></custom-input>
      </b-col>
      <b-col lg="6" sm="10">
        <custom-input
          v-model="appDocument.customer_code"
          index="customer_code"
          label="customer_code"
        ></custom-input>
      </b-col>
      <b-col lg="6" sm="10">
        <custom-input
          v-model="appDocument.customer_name"
          index="customer_name"
          label="customer_name"
        ></custom-input>
      </b-col>
    </b-row>
    <b-card no-body>
      <b-tabs v-model="tabIndex" small card fill>
        <b-tab title="Detail">
          <b-row>
            <b-col lg="6" sm="10">
              <custom-input
                v-model="appDocument.payment_term"
                index="payment_term"
                label="payment_term"
              ></custom-input>
            </b-col>
            <b-col lg="6" sm="10">
              <custom-input v-model="appDocument.sales" index="sales" label="sales"></custom-input>
            </b-col>
            <b-col lg="6" sm="10">
              <custom-input
                v-model="appDocument.create_date"
                index="create_date"
                label="create_date"
              ></custom-input>
            </b-col>
            <b-col lg="6" sm="10">
              <custom-input
                v-model="appDocument.account_type"
                index="account_type"
                label="account_type"
              ></custom-input>
            </b-col>
            <b-col lg="6" sm="10">
              <custom-input v-model="appDocument.subPack" index="subPack" label="subPack"></custom-input>
            </b-col>
            <b-col lg="6" sm="10">
              <custom-input
                v-model="appDocument.modify_datetime"
                index="modify_datetime"
                label="modify_datetime"
              ></custom-input>
            </b-col>
            <b-col lg="6" sm="10">
              <custom-input
                v-model="appDocument.modify_datetime"
                index="modify_datetime"
                label="modify_datetime"
              ></custom-input>
            </b-col>
            <b-col lg="6" sm="10">
              {{appDocument.product_codes}}
            </b-col>
          </b-row>
          <b-row>
            <b-col lg="12" sm="12">
              <b-table hover small show-empty head-variant="dark" :items="appDocument.address"></b-table>
            </b-col>
          </b-row>
          <b-row>
            <b-col lg="12" sm="12">
              <customer-product-code :codes="appDocument.product_codes"></customer-product-code>
            </b-col>
          </b-row>
        </b-tab>
        <b-tab title="Product Summary"></b-tab>
        <b-tab title="Order Summary"></b-tab>
        <b-tab title="Material Summary"></b-tab>
        <b-tab title="Change Log">
          <b-row>
            <b-col lg="12" sm="12">
              <b-table hover small show-empty head-variant="dark" :items="appDocument.change_log"></b-table>
            </b-col>
          </b-row>
        </b-tab>
        <b-tab title="JSON">
          <vue-json-pretty :data="appDocument"></vue-json-pretty>
        </b-tab>
      </b-tabs>
    </b-card>
  </b-container>
</template>
<script lang="ts">
import { mapActions, mapGetters } from "vuex";
const namespaced = "customer";
const VueJsonPretty = require("vue-json-pretty");
import CustomInput from "@/components/form/CustomInput.vue";
import CustomerProductCode from "@/components/form/CustomerProductCode.vue";

import Vue from 'vue'
export default Vue.extend({
    components: {
    VueJsonPretty,
    CustomInput,
    CustomerProductCode
  },
  data() {
    return {
      state: [],
      tabIndex: 0
    };
  },
  mounted() {
    // console.log("Fetch Document", this.databasePath);
    this.$store.dispatch(namespaced + "/FETCH_DOCUMENT", this.$route.path);
    // this.$store.dispatch("customer/FETCH_DOCUMENT", this.$route.path);
  },
  methods: {
    ...mapActions(namespaced, ["FETCH_DOCUMENT"]),
    // ...mapActions(["customer/FETCH_DOCUMENT"])
    print123: function() {
      alert("123");
      console.log("123");
    }
  },
  computed: {
    ...mapGetters(namespaced, ["appDocument"])
  }
})
</script>