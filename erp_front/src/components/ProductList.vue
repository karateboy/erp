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

      <b-col cols="12" lg="5">
        <div>
          Sorting By:
          <b>{{ sortBy }}</b>, Sort Direction:
          <b>{{ sortDesc ? 'Descending' : 'Ascending' }}</b>
        </div>
      </b-col>
      <b-col cols="12" lg="3">
        <b-form-group
          label-cols-sm="3"
          label-cols-md="3"
          label-cols-lg="2"
          label-align-sm="right"
          label-size="md"
          label-for="perPageSelect"
          class="mb-0"
        >
          <b-form-select v-model="perPage" id="perPageSelect" size="sm" :options="pageOptions"></b-form-select>
        </b-form-group>
      </b-col>
    </b-row>
    <b-row>
      <b-col cols="12">
        <b-pagination
          v-model="currentPage"
          :total-rows="list.length"
          :per-page="perPage"
          pills
          limit="10"
          align="fill"
          size="sm"
          class="my-0"
        >
          <template v-slot:first-text>
            <b-icon icon="skip-end-fill"></b-icon>
          </template>
          <template v-slot:prev-text>
            <b-icon icon="skip-start-fill"></b-icon>
          </template>
          <template v-slot:next-text>
            <b-icon icon="skip-backward-fill"></b-icon>
          </template>
          <template v-slot:last-text>
            <b-icon icon="skip-forward-fill"></b-icon>
          </template>
        </b-pagination>
      </b-col>
    </b-row>
    <b-table
      striped
      hover
      small
      footClone
      show-empty
      :fields="fields"
      :items="list"
      :sort-by.sync="sortBy"
      :sort-desc.sync="sortDesc"
      :current-page="currentPage"
      :per-page="perPage"
      :filter="filter"
      :filterIncludedFields="filterOn"
    >
      <!-- A virtual column -->
      <template v-slot:cell(name)="data">
        <span>
          <a
            :href="`/product/${data.item.ptn}`"
          >{{data.item.product_code }}-{{ data.item.product_number }} {{ data.item.product_spec }}</a>
        </span>
        <span class="right">{{ data.item.ext_ref}}</span>
      </template>

      <template v-slot:cell(index)="data">{{ data.index+1 }}</template>
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

const namespaced = "product";

export default {
  data() {
    return {
      filter: null,
      filterOn: [
        "name",
        "product_code",
        "product_number",
        "ext_ref",
        "product_name"
      ],
      totalRows: 1,
      currentPage: 1,
      perPage: 100,
      pageOptions: [100, 500, 2000],
      sortBy: "",
      sortDesc: true,
      // dataList:"",
      // dataListLength:"",

      databasePath: this.$route.path,
      fields: [
        { key: "index", label: "x" },
        { key: "name", label: "รหัสสินค้า"},
        { key: "product_name", variant : "info" },
        { key: "colors" },
        { key: "paper_code", sortable: true},
        { key: "stock", sortable: true },
        { key: "cell" },
        { key: "print_type", sortable: true }
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
    console.log(this.databasePath);
    if (this.databasePath === "/product") {
      console.log("List All");
      this.$store.dispatch(namespaced + "/FETCH_LIST", this.databasePath);
    }
    if (this.databasePath.includes("/product/customer/")) {
      console.log("List " + this.databasePath);
      this.$store.dispatch(namespaced + "/FETCH_FILTERED_LIST", this.databasePath);
    }
    if (this.databasePath.includes("/customer")) {
      this.sortBy = "product_number";
      this.sortDesc = false;
    }
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
span.right {
  float: right;
}
</style>
