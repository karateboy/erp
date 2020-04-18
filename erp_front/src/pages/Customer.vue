<template>
  <b-container fluid>
    <b-row>
      <b-col>
        <h1>Customer {{pageView}} @ {{ $route.fullPath }}</h1>
      </b-col>
    </b-row>
    <b-row v-if="pageView === 'list'">
      <b-col>
        <list-view></list-view>
      </b-col>
    </b-row>
    <b-row v-if="pageView === 'document'">
      <b-col>
        <document-view></document-view>
      </b-col>
    </b-row>
  </b-container>
</template>
<style scoped>
h1 {
  font-weight: bold;
  padding: 0 15px;
}
</style>

<script lang="ts">
import Vue from "vue";
import DocumentView from "@/components/CustomerDocument.vue";
import ListView from "@/components/CustomerList.vue";

export default Vue.extend({
  components: {
    DocumentView,
    ListView
  },
  data() {
    return {
      pageView: "list"
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
    }
  }
});
</script>