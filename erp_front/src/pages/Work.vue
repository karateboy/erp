<template>
<div>
    <b-container fluid>
    <b-row class="no-gutters">
      <b-col  class="d-print-none">
        <h1>Work {{pageView}} {{ $route.fullPath }}</h1>
      </b-col>
    </b-row>
    <b-row>
      <b-col>
        <app-list-view v-if="pageView === 'list'"></app-list-view>
        <app-document-view v-else-if="pageView === 'document'"></app-document-view>
      </b-col>
    </b-row>
  </b-container>
</div>
</template>



<!--
	***
	VUE scripts
	***
-->
<script>
import DocumentView from "@/components/WorkDocument.vue";
import ListView from "@/components/WorkList.vue";

export default {
  components: {
    "app-document-view": DocumentView,
    "app-list-view": ListView
  },
  data() {
    return {
      pageView: []
    };
  },
  mounted() {
    if (
      this.$route.fullPath === "/work" ||
      (this.$route.fullPath.includes("/work/customer/") &&
        this.$route.params.id.length > 0)
    ) {
      this.pageView = "list";
    } else if (
      this.$route.fullPath.includes("/work/") &&
      this.$route.params.id.length > 0
    ) {
      this.pageView = "document";
    } else {
      this.pageView = "list";
    }
  }
};
</script>



<!--
	***
	BELOW IS STYLING OF WEBPAGE SCOPED
	***
	!-->
<style scoped>
h1 {
  /* background: black;
  color: white; */
  font-weight: bold;
  padding: 0 15px;
}
</style>
