<template>
  <b-container fluid>
    <h1>Customer {{pageView}} @ {{ $route.fullPath }}</h1>
    <list-view v-if="pageView === 'list'"></list-view>
    <document-view v-else-if="pageView === 'document'"></document-view>
  </b-container>
</template>



<!--
	***
	VUE scripts
	***
-->
<script>
import DocumentView from "@/components/CustomerDocument.vue";
import ListView from "@/components/CustomerList.vue";

export default {
  components: {
    DocumentView,
    ListView
  },
  data() {
    return {
      pageView: "invalid"
    };
  },
  mounted() {
    if (this.$route.fullPath === "/customer") {
      this.pageView = "list";
    } else if (
      this.$route.fullPath.includes("/customer/") &&
      this.$route.params.id.length > 0
    ) {
      this.pageView = "document";
    } else {
      this.pageView = "invalid";
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
  font-weight: bold;
  padding: 0 15px;
}
</style>
