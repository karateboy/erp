<template>
  <b-container fluid>
    <b-row>
      <b-table
        striped
        hover
        small
        :fields="fields"
        :items="records"
        :sort-by.sync="sortBy"
        :sort-desc.sync="sortDesc"
      >
        <!-- A virtual column -->
        <template v-slot:cell(index)="data">{{ data.index+1 }}</template>
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
  props:{
    records: Array, 
  },
  data() {
    return {
      sortBy: "packaging_datetime",
      sortDesc: true,
      fields: [
        { key: "index", label: "x" },
        { key: "suid", label: "SUID" },
        { key: "jtInvoice_Number", label: "Invoice" },
        { key: "jwn", label: "JWN" },
        { key: "order_qty", label: "order_qty" },
        { key: "order_unit", label: "order_unit" },
        { key: "station_id", label: "station_id" },
        { key: "personal", label: "personal" },
        { key: "packaging_datetime", label: "packaging_datetime" },
        { key: "truck_id", label: "truck_id" },
        { key: "status", label: "status" }
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
