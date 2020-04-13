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
      <template v-slot:cell(jwn)="data">
        <!-- `data.value` is the value after formatted by the Formatter -->
        <a :href="`${databasePath}/${data.value}?${data.item.ptn}`">{{ data.value }}</a>
      </template>
      <template v-slot:cell(ptn)="data">
        <!-- `data.value` is the value after formatted by the Formatter -->
        <a :href="`/product/${data.value}`">{{data.item.pcns}}</a>
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
const namespaced = "work";

export default {
  data() {
    return {
      filter: null,
      filterOn: [],
      totalRows: 1,
      currentPage: 1,
      perPage: 100,
      pageOptions: [100, 500, 2000],
      sortBy: "jwn",
      sortDesc: true,

      databasePath: this.$route.path,
      fields: [
        { key: "index", label: "x" },
        {
          key: "entry_datetime",
          formatter: value => {
            return value.slice(0, 10);
          }
        },
        { key: "jwn" },
        { key: "ptn" },
        { key: "ext_ref" },
        { key: "print_type" },
        { key: "order_number" },
        { key: "order_qty" },
        { key: "work_qty" },
        { key: "order_due" },
        { key: "work_progress" }
      ]
    };
  },
  mounted() {
    console.log(this.databasePath);
    if (this.databasePath === "/work") {
      // console.log("List All");
      this.$store.dispatch(namespaced + "/FETCH_LIST", this.databasePath);
    }
    if (this.databasePath.includes("/work/customer/")) {
      // console.log("List " + this.databasePath);
      this.$store.dispatch(namespaced + "/FETCH_FILTERED_LIST", this.databasePath);
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
table {
  flex-grow: 1;
}
</style>