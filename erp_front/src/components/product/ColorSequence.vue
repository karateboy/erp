<template>
  <b-card border-variant="danger" header="Printing">
    <b-container fluid>
      <b-row>
        <b-col class="primary">
          <span variant="primary">{{ appDocument.print_type}} = {{ appDocument.colors}} colors</span>
        </b-col>
        <b-col class="bg-info">{{ appDocument.laminate}} {{ appDocument.tape_code}}</b-col>
        <b-col class="bg-info">{{ appDocument.dicut}} {{ appDocument.dicut_plate }}</b-col>
      </b-row>
      <b-row>
        <b-table
          striped
          hover
          small
          v-bind:fields="fields"
          v-bind:items="appDocument.printing_seq"
        >
          <!-- A virtual column -->
          <template v-slot:cell(index)="dataList">{{ dataList.index+1 }}</template>
        </b-table>
      </b-row>
    </b-container>
  </b-card>
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
  // props: {
  //   printing_seq: Array
  // },
  data() {
    return {
      search: "",
      fields: [
        { key: "index", label: "x" },
        "ink",
        "mesh",
        "ruling",
        "seq",
        "stencil_id"
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
