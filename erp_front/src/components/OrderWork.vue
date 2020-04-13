<template>
  <b-container fluid>
    <b-row>
      <b-table
        striped
        hover
        small
        :fields="fields"
        :items="appDocument.order_item"
        :filter="filter"
        :filterIncludedFields="filterOn"
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
const namespaced = "order";

export default {
  props:{
    display: String,
  },
  data() {
    return {
      search: "",
      filter: this.display,
      filterOn: ["work_progress"],
      fields: [
        { key: "index", label: "x" },
        "jwn",
        "SKU_code",
        "SKU_customer",
        "print_type",
        "order_qty",
        "work_qty",
        "due_date",
        "work_type",
        "work_progress"
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
