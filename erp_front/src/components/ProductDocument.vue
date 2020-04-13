<template>
  <b-container fluid>
    <b-row>
      <b-col cols="12" lg="9">
        <b-card border-variant="primary" :header="appDocument.pcns">
          <b-form-group
            label-cols="4"
            label-cols-lg="2"
            label-size="md"
            label="รายการสินค้า"
            label-for="input-md"
          >
            <b-form-textarea id="input-sm2" size="sm" readonly v-model="appDocument.product_name"></b-form-textarea>
          </b-form-group>
          <b-form-group
            label-cols="4"
            label-cols-lg="2"
            label-size="md"
            label="SKU Customer"
            label-for="input-md"
          >
            <b-form-input id="input-sm" size="sm" readonly v-model="appDocument.ext_ref"></b-form-input>
          </b-form-group>

          <b-form-group
            label-cols="4"
            label-cols-lg="2"
            label-size="md"
            label="ชนิดกระดาษ"
            label-for="input-md"
          >
            <b-form inline>
              <b-input
                id="inline-form-input1"
                input-cols="3"
                readonly
                size="sm"
                v-model="appDocument.paper_id"
              ></b-input>
              <b-input
                id="inline-form-input2"
                input-cols="9"
                width="100"
                readonly
                size="sm"
                v-model="appDocument.paper_code"
              ></b-input>
            </b-form>
          </b-form-group>

          <b-form-group
            label-cols="4"
            label-cols-lg="2"
            label-size="md"
            label="Notes"
            label-for="input-md"
          >
            <b-form-textarea id="input-sm" size="sm" rows="8" readonly v-model="appDocument.note"></b-form-textarea>
          </b-form-group>
        </b-card>
        <br />
        <color-sequence></color-sequence>
      </b-col>
      <b-col cols="12" lg="3">
        <b-row>
          <b-col cols="12">
            <b-card border-variant="secondary" header="Quotation">
              <b-row>
                <b-col>Pricing</b-col>
                <b-col>{{ appDocument.unit_price }}</b-col>
              </b-row>
              <b-row>
                <b-col>Qouation</b-col>
                <b-col>xxxx</b-col>
              </b-row>
            </b-card>
          </b-col>
        </b-row>
        <br />
        <b-row>
          <printing-substrate></printing-substrate>
        </b-row>
      </b-col>
    </b-row>
    <br />

    <production></production>
    <br />

    <b-card no-body>
      <b-tabs v-model="tabIndex" small card fill>
        <b-tab title="Active Order">
          <order-history></order-history>
        </b-tab>
        <b-tab title="Order History">
          <order-history></order-history>
        </b-tab>
        <b-tab title="Invoice">
          <storage-record></storage-record>
        </b-tab>
        <b-tab title="Shipping">
          <shipping-record :records="appDocument.shipping_record"></shipping-record>
          <!-- <shipping-record></shipping-record> -->
        </b-tab>
        <b-tab title="Inventory">
          <storage-record></storage-record>
        </b-tab>
        <b-tab title="Logs">
          <change-log></change-log>
        </b-tab>
        <b-tab title="JSON">
          <vue-json-pretty :data="appDocument"></vue-json-pretty>
        </b-tab>
      </b-tabs>
    </b-card>
  </b-container>
</template>


<!--
	***

	VUE scripts

	***
-->
<script>
import { mapActions, mapGetters } from "vuex";

const namespaced = "product";

import ColorSequence from "./product/ColorSequence";
import Production from "./product/Production";
import ChangeLog from "./product/ChangeLog";
import OrderHistory from "./product/OrderHistory";
import StorageRecord from "./product/StorageRecord";
import ShippingRecord from "./product/ShippingRecord";
import PrintingSubstrate from "./product/PrintingSubstrate";
import VueJsonPretty from "vue-json-pretty";

export default {
  components: {
    VueJsonPretty,
    "color-sequence": ColorSequence,
    production: Production,
    "change-log": ChangeLog,
    "order-history": OrderHistory,
    "storage-record": StorageRecord,
    "shipping-record": ShippingRecord,
    "printing-substrate": PrintingSubstrate
  },
  data() {
    return {
      databasePath: this.$route.path,
      search: "",
      tabIndex: 0,
      list: {
        fields: [
          "รหัสสินค้า",
          "2",
          "รายการสินค้า",
          "4",
          "ชนิดกระดาษ",
          "สินค้าคงเหลือ",
          "ตระกร้า",
          "รูปแบบการพิมพ์"
        ]
      }
    };
  },
  mounted() {
    // console.log("Fetch Document", this.databasePath);
    this.$store.dispatch(namespaced + "/FETCH_DOCUMENT", this.databasePath);
  },
  methods: {
    ...mapActions(namespaced, ["FETCH_DOCUMENT"])
  },
  computed: {
    ...mapGetters(namespaced, ["appDocument"])
  }
};
</script>



<!--
	***

	BELOW IS STYLING OF WEBPAGE SCOPED
	
	***
!-->
<style scoped>
</style>
