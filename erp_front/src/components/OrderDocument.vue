<template>
  <div>
    <b-row>
      <b-col lg="7" sm="12">
        <b-form-group
          id="customer"
          label-cols-sm="4"
          label-cols-lg="2"
          label="customer"
          label-for="input-horizontal"
        >
          <b-form-input id="input-horizontal" v-model="appDocument.customer"></b-form-input>
        </b-form-group>

        <b-form-group
          id="order_number"
          label-cols-sm="4"
          label-cols-lg="2"
          label="order_number"
          label-for="input-horizontal"
        >
          <b-form-input id="input-horizontal" v-model="appDocument.order_number"></b-form-input>
        </b-form-group>

        <b-form-group
          id="product_codes"
          label-cols-sm="4"
          label-cols-lg="2"
          label="product_codes"
          label-for="input-horizontal"
        >
          <b-button v-for="code in appDocument.product_codes" :key="code" variant="success">{{code}}</b-button>

          <!-- tag add implemetations moved under customer settings -->
          <!-- <b-form-tags         
            input-id="tags-basic"
            disabled
            v-model="appDocument.product_codes"
            class="mb-2"
          ></b-form-tags>-->
        </b-form-group>
      </b-col>

      <b-col lg="5" sm="6">
        <b-form-group id="order_note" label="order_note" label-for="input-horizontal">
          <b-form-textarea input-id="tags-basic" v-model="appDocument.order_note" rows="4"></b-form-textarea>
        </b-form-group>
      </b-col>

      <b-col sm="12" lg="12">
        <order-entry-detail></order-entry-detail>
      </b-col>

      <b-col sm="12" lg="12">
        <b-card no-body>
          <b-tabs v-model="tabIndex" small card fill>
            <b-tab title="NEW">
              <b-card no-body>
                <b-tabs pills small card>
                  <b-tab
                    v-for="product in appDocument.product_codes"
                    :key="product"
                    :title="product"
                  >
                    <order-entry :product="product"></order-entry>
                  </b-tab>
                </b-tabs>
              </b-card>
            </b-tab>
            <b-tab :title="`ALL: ${appDocument.order_item_count}`">
              <order-work></order-work>
            </b-tab>
            <b-tab title="Detail">
              <b-col>
                <b-row>
                  <b-col>{{appDocument._id}}</b-col>
                </b-row>
                <b-row>
                  <b-col>ID</b-col>
                  <b-col>{{appDocument.jon}}</b-col>
                </b-row>

                <b-row>
                  <b-col>Progress</b-col>
                  <b-col>{{appDocument.order_progress}}</b-col>
                </b-row>
                <b-row>
                  <b-col>Revision</b-col>
                  <b-col>{{appDocument.order_revision}}</b-col>
                </b-row>
                <b-row>
                  <b-col>Items</b-col>
                  <b-col>{{appDocument.order_item_completed}} / {{appDocument.order_item_count}}</b-col>
                </b-row>
                <b-row>
                  <b-col>Order Datetime</b-col>
                  <b-col>{{appDocument.order_datetime}}</b-col>
                </b-row>
                <b-row>
                  <b-col>Ack Datetime</b-col>
                  <b-col>{{appDocument.ack_datetime}}</b-col>
                </b-row>
                <b-row>
                  <b-col>Order Entry</b-col>
                  <b-col>{{appDocument.order_clerk}}</b-col>
                </b-row>
              </b-col>
            </b-tab>
            <b-tab title="Shipping"></b-tab>
            <b-tab title="Audit Log">
              <b-list-group>
                <div v-for="log in appDocument.order_log" :key="log.seq">
                  <b-list-group-item href="#" class="flex-column align-items-start">
                    <div class="d-flex w-100 justify-content-between">
                      <small>{{log}}</small>
                    </div>
                  </b-list-group-item>
                </div>
              </b-list-group>
            </b-tab>
            <b-tab title="JSON">
              <vue-json-pretty :data="appDocument"></vue-json-pretty>
            </b-tab>
          </b-tabs>
        </b-card>
      </b-col>
    </b-row>
  </div>
</template>


<!--
	***
	VUE scripts
	***
-->
<script>
import { mapActions, mapGetters } from "vuex";

const namespaced = "order";

import OrderWork from "./OrderWork.vue";
import OrderEntry from "./form/OrderEntry.vue";
import OrderEntryDetail from "./form/OrderEntryDetail.vue";
import VueJsonPretty from "vue-json-pretty";

export default {
  components: {
    "order-work": OrderWork,
    "order-entry": OrderEntry,
    OrderEntryDetail,
    VueJsonPretty
  },
  data() {
    return {
      tabIndex: 0
    };
  },
  mounted() {
    console.log("Fetch Document", this.$route.path);
    this.$store.dispatch(namespaced + "/FETCH_DOCUMENT", "/order");
  },
  methods: {
    ...mapActions(namespaced, ["FETCH_DOCUMENT"])
  },
  computed: {
    ...mapGetters(namespaced, ["appDocument"]),
    orderItemWorking: function() {
      return (
        this.appDocument.order_item_count -
        this.appDocument.order_item_completed
      );
    }
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
