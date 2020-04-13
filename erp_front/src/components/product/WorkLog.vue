<template>
  <b-container fluid>
    <b-row>
      <b-table
        striped
        hover
        borderless
        small
        :fields="fields"
        :items="records"
        :sort-by.sync="sortBy"
        :sort-desc.sync="sortDesc"
      >
        <!-- A virtual column -->
        <template v-slot:cell(index)="dataList">{{ dataList.index+1 }}</template>
      </b-table>
    </b-row>
  </b-container>
</template>



<!--
	***
	VUE scripts
	***
-->
<script>
export default {
  props: {
    records: Array
  },
  data() {
    return {
      sortBy: "datetime",
      sortDesc: true,
      fields: [
        { key: "index", label: "x" },
        "station",
        "station_group",
        "station_id",
        "status",
        "description",
        "username",
        "queue_datetime",
        "start_datetime",
        "end_datetime",
      ]
    };
  },
};
</script>



<!--
	***
	BELOW IS STYLING OF WEBPAGE SCOPED
	***
!-->
<style scoped>
</style>
