<template>
  <b-container fluid>
    <b-row>
      <b-table
        striped
        hover
        borderless
        small
        :fields="fields"
        :items="appDocument.change_log"
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
  props: {
    printing_seq: Array
  },
  data() {
    return {
      sortBy: "datetime",
      sortDesc: true,
      fields: [
        { key: "index", label: "x" },
        "change_detail",
        "change_type",
        "datetime",
        "entry",
        "filename"
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
