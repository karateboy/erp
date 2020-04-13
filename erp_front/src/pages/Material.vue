<template>
  <b-container fluid>
    <h1>Material {{pageView}} {{ $route.fullPath }}</h1>
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
// import DocumentView from "@/components/MaterialDocument.vue";
import ListView from "@/components/MaterialList.vue";

export default {
  components: {
    // "app-document-view": DocumentView,
    "app-list-view": ListView
  },
  data() {
    return {
      pageView: []
    };
  },
  mounted() {
    if (
      this.$route.fullPath === "/material" ||
      (this.$route.fullPath.includes("/material/customer/") &&
        this.$route.params.id.length > 0)
    ) {
      this.pageView = "list";
    // } else if (
    //   this.$route.fullPath.includes("/material/") &&
    //   this.$route.params.id.length > 0
    // ) {
    //   this.pageView = "document";
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
  display: flex;
  background: black;
  color: white;
  font-weight: bold;
}
</style>
