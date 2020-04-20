<template>
  <b-container fluid>
    <h1>Order {{pageView}} {{ $route.fullPath }}</h1>
    <app-list-view v-if="pageView === 'list'"></app-list-view>
    <app-document-view v-else-if="pageView === 'document'"></app-document-view>
  </b-container>
</template>



<!--
	***
	VUE scripts
	***
-->
<script>
import DocumentView from "@/components/OrderDocument.vue";
import ListView from "@/components/OrderList.vue";

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
      this.$route.fullPath === "/order" ||
      (this.$route.fullPath.includes("/order/customer/") &&
        this.$route.params.id.length > 0)
    ) {
      this.pageView = "list";
    } else if (
      this.$route.fullPath.includes("/order/") &&
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
  font-weight: bold;
  padding: 0 15px;
}
</style>
