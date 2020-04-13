<template>
  <b-card border-variant="info" header-tag="header" :header="appDocument.paper_code">
    <b-card-text>Roll/Sheet/KG {{ appDocument.product_width }} x {{ appDocument.product_length }}</b-card-text>
    <b-card border-variant="primary" header="Printing">
      <b-card-text>{{appDocument.cuts}}</b-card-text>
      <b-card-text>{{ appDocument.product_width }} x {{ appDocument.product_length }} {{ appDocument.unit }}</b-card-text>
      <b-card
        img-src="https://picsum.photos/600/300/?image=25"
        img-alt="Image"
        img-bottom
        style="max-width: 20rem;"
        border-variant="success"
      >
        <b-card-text>{{ appDocument.prints }} {{ appDocument.unit }}</b-card-text>
        <b-card-text>{{ appDocument.unit_width }} x {{ appDocument.unit_length }}</b-card-text>
      </b-card>
    </b-card>
  </b-card>
</template>

<script>
import { mapGetters } from "vuex";
const namespaced = "product";

export default {
  mounted() {
    this.$store.appDocument;
  },
  computed: {
    ...mapGetters(namespaced, ["appDocument"])
  }
};
</script>

<style scoped>
div {
  background: cccccc;
}
</style>