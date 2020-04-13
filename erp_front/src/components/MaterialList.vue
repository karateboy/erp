<template>
  <div>
    <div>
      Sorting By:
      <b>{{ sortBy }}</b>, Sort Direction:
      <b>{{ sortDesc ? 'Descending' : 'Ascending' }}</b>
    </div>
    <b-col lg="4" class="my-1">
      <b-form-group
        label="Filter"
        label-cols-sm="3"
        label-align-sm="right"
        label-size="sm"
        label-for="filterInput"
        class="mb-0"
      >
        <b-input-group size="sm">
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
    <b-col sm="2" md="3" class="my-1">
      <b-form-group
        label="Per page"
        label-cols-sm="4"
        label-cols-md="3"
        label-cols-lg="2"
        label-align-sm="right"
        label-size="sm"
        label-for="perPageSelect"
        class="mb-0"
      >
        <b-form-select v-model="perPage" id="perPageSelect" size="sm" :options="pageOptions"></b-form-select>
      </b-form-group>
    </b-col>

    <b-col sm="4" md="4" class="my-1">
      <b-pagination
        v-model="currentPage"
        :total-rows="list.length"
        :per-page="perPage"
        align="fill"
        size="sm"
        class="my-0"
      ></b-pagination>
    </b-col>

    <b-table
      striped
      hover
      borderless
      small
      dark
      footClone
      show-empty
      v-bind:fields="fields"
      v-bind:items="list"
      v-bind:sort-by.sync="sortBy"
      v-bind:sort-desc.sync="sortDesc"
      :current-page="currentPage"
      :per-page="perPage"
      :filter="filter"
      :filterIncludedFields="filterOn"
    >
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

const namespaced = "material";
export default {
  data() {
    return {
      filter: null,
      filterOn: [],
      totalRows: 1,
      currentPage: 1,
      perPage: 100,
      pageOptions: [100, 500, 2000],
      sortBy: "",
      sortDesc: true,

      databasePath: this.$route.path,
      fields: [
        { key: "index", label: "x" },
        { key: "id", label: "-" },
        { key: "parent", label: "-" },
        { key: "material_code", label: "-" },
        { key: "material_type", label: "-" },
        { key: "material_name", label: "-" },
        { key: "material_desc", label: "-" }
        // {key: "entry_datetime", formatter: value=>{ return value.slice(0,10);} },
      ]
    };
  },
  mounted() {
    this.$store.dispatch(namespaced+"/FETCH_LIST");

    if (this.databasePath.includes("/customer")) {
      this.sortBy = "customer_code";
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
table {
  flex-grow: 1;
}
div {
}
</style>
