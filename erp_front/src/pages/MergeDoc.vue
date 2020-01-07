<template>
  <div>
    <strong>Merge Document!</strong>
  </div>
</template>
<script lang="ts">
import Vue from "vue";
import axios from "axios";
export default Vue.extend({
  mounted() {
    axios
      .get("/ownerless-image")
      .then(res => {
        const ret = res.data;
        console.log(ret);
      })
      .catch(err => alert(err));
  },
  data() {
    return {
      tags:[],
      ownerlessImages:[]
    };
  }
});
</script>