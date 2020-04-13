<template>
  <b-container fluid>
    <b-row>
      <b-table
        striped
        hover
        small
        :fields="fields"
        :items="appDocument.storage_record"
        :sort-by.sync="sortBy"
        :sort-desc.sync="sortDesc"
      >
        <!-- A virtual column -->
        <template v-slot:cell(index)="data">{{ data.index+1 }}</template>
        <template v-slot:cell(stockIn)="data">
          {{ data.item.stock_in.jwn }}
          <br />
          {{ data.item.stock_in.datetime }}
        </template>
        <template v-slot:cell(stockOut)="data">
          {{ data.item.stock_out.jwn }}
          <br />
          {{ data.item.stock_out.datetime }}
        </template>
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
import { mapGetters } from "vuex";
const namespaced = "product";

export default {
  data() {
    return {
      sortBy: "suid",
      sortDesc: true,
      fields: [
        { key: "index", label: "x" },
        { key: "suid", label: "x" },
        { key: "stockIn", label: "Stock In Info" },
        "stock_qty",
        "process_by",
        { key: "stockOut", label: "Stock Out Info" },
        "store_cell",
        "progress"
        // "stock_in",
        // "stock_out",
        // "store_log"
      ]
    };
  },
  mounted() {
    this.$store.appDocument;
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
table {
  -moz-border-radius: 5px !important;
  border-collapse: collapse !important;
  border: none !important;
}
table th,
table td {
  border: none !important;
}
table th:first-child {
  -moz-border-radius: 5px 0 0 0 !important;
}
table th:last-child {
  -moz-border-radius: 0 5px 0 0 !important;
}
table tr:last-child td:first-child {
  -moz-border-radius: 0 0 0 5px !important;
}
table tr:last-child td:last-child {
  -moz-border-radius: 0 0 5px 0 !important;
}
table tr:hover td {
  background-color: #ddd !important;
}
</style>
