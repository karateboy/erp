<template>
  <div>
    <b-row>
      <b-col cols="12" lg="4">
        <b-form-group
          label="Filter"
          label-cols="2"
          label-align="right"
          label-size="lg"
          label-for="filterInput"
          class="mb-0"
        >
          <b-input-group size="lg">
            <b-form-input
              v-model="filter"
              type="search"
              id="filterInput"
              placeholder="Type to Search"
            ></b-form-input>
            <b-input-group-append>
              <b-button :disabled="!filter" @click="filter = ''">Clear</b-button>
            </b-input-group-append>
          </b-input-group>
        </b-form-group>
      </b-col>

      <b-col cols="12" lg="4">
        <div>
          Sorting By:
          <b>{{ sortBy }}</b>, Sort Direction:
          <b>{{ sortDesc ? 'Descending' : 'Ascending' }}</b>
        </div>
      </b-col>
    </b-row>

    <b-table
      striped
      hover
      small
      show-empty
      foot-clone
      no-footer-sorting
      head-variant="dark"
      :fields="fields"
      :items="list"
      :sort-by.sync="sortBy"
      :sort-desc.sync="sortDesc"
      :filter="filter"
      :filterIncludedFields="filterOn"
    >
      <!-- A virtual column -->
      <template v-slot:cell(index)="data">{{ data.index+1 }}</template>

      <!-- A virtual column -->
      <template v-slot:cell(shortcut)="data">
        <a :href="`/product/customer/${data.item.customer_code}`">Product</a> |
        <a :href="`/order/customer/${data.item.customer_code}`">Order</a> |
        <a :href="`/work/customer/${data.item.customer_code}`">Work</a>
      </template>

      <!-- A virtual column -->
      <template v-slot:cell(customer_code)="data">
        <!-- `data.value` is the value after formatted by the Formatter -->
        <a :href="`${databasePath}/${data.value}`">{{ data.value }}</a>
      </template>
    </b-table>
  </div>
</template>



<!--
	***

	VUE scripts

	***
-->
<script>
import { mapActions, mapGetters } from "vuex";
const namespaced = "customer";

export default {
  data() {
    return {
      //for bootstrap vue tables
      filter: null,
      filterOn: [],
      sortBy: "account_type",
      sortDesc: false,

      databasePath: this.$route.path,
      fields: [
        { key: "index", label: "x" },
        { key: "account_type", label: "Type", sortable: true },
        { key: "shortcut", label: "P|O|W" },
        { key: "customer_code", label: "รหัสลูกค้า", sortable: true },
        { key: "customer_name", label: "ชื่อ" },
        { key: "payment_term" }
      ],
      fieldsThai: [
        "รหัสสินค้า",
        "2",
        "รายการสินค้า",
        "4",
        "ชนิดกระดาษ",
        "สินค้าคงเหลือ",
        "ตระกร้า",
        "รูปแบบการพิมพ์"
      ]
    };
  },
  mounted() {
    this.$store.dispatch(namespaced + "/FETCH_LIST", this.databasePath);
  },
  methods: {
    ...mapActions(namespaced, ["FETCH_LIST"])
  },
  computed: {
    ...mapGetters(namespaced, ["list"])
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
