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
      show-empty
      foot-clone
      no-footer-sorting
      head-variant="dark"
      :fields="fields"
      :items="list"
      :sort-by.sync="sortBy"
      :sort-desc.sync="sortDesc"
      :current-page="currentPage"
      :per-page="perPage"
      :filter="filter"
      :filterIncludedFields="filterOn"
    >
      <template v-slot:cell(jon)="data">
        <!-- `data.value` is the value after formatted by the Formatter -->
        <a :href="`${databasePath}/${data.value}`">{{ data.value }}</a>
      </template>
      <template v-slot:cell(customer)="data">
        <!-- `data.value` is the value after formatted by the Formatter -->
        <a :href="`${databasePath}/customer/${data.value}`">{{ data.value }}</a>
      </template>
      <!-- A virtual column -->
      <template v-slot:cell(index)="dataList">{{ dataList.index+1 }}</template>
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

const namespaced = "order";

export default {
  data() {
    return {
      search: [],
      pageList: [],
      filter: null,
      filterOn: ["order_number", "customer", "order_datetime", "jon"],
      totalRows: 1,
      currentPage: 1,
      perPage: 500,
      pageOptions: [500, 1000, 2000],
      sortBy: "order_datetime",
      sortDesc: true,

      databasePath: this.$route.path,
      fields: [
        { key: "index", label: "x" },
        {
          key: "order_datetime",
          formatter: value => {
            return value.slice(0, 10);
          }
        },
        { key: "jon" },
        { key: "customer" },
        { key: "order_number" },
        { key: "order_item_count" },
        { key: "order_item_completed" },
        { key: "order_progress" }
      ]
    };
  },
  mounted() {
    console.log(this.$route.fullPath);
    if (this.databasePath === "/order") {
      console.log("List All");
      this.$store.dispatch(namespaced + "/FETCH_LIST", this.$route.path);
    }
    if (this.databasePath.includes("/order/customer/")) {
      console.log("List " + this.$route.path);
      this.$store.dispatch(namespaced + "/FETCH_FILTERED_LIST", this.$route.path);
      // this.pageList = store.filtered_list;
    }
    // if (this.databasePath.includes("/customer")) {
    //   this.sortBy = "order_datetime";
    //   this.sortDesc = true;
    // }
  },
  methods: {
    ...mapActions(namespaced, ["FETCH_LIST", "FETCH_FILTERED_LIST"])
  },
  computed: {
    ...mapGetters(namespaced, ["list", "filtered_list"])
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
