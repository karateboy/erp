<template>
  <b-container fluid>
    <b-row>
      <b-table
        striped
        hover
        borderless
        small
        :fields="fields"
        :items="appDocument.order_history"
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
import { mapGetters } from "vuex";
const namespaced = "product";

export default {
  data() {
    return {
      sortBy: "jwn",
      sortDesc: true,
      fields: [
        { key: "index", label: "x" },
        "jon",
        "jwn",
        "order_qty",
        "orderdue",
        "work_id"
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
</style>
