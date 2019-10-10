<template>
  <div>
    <Row>
      <Card>
      </Card>
    </Row>
  </div>
</template>
<style scoped>
</style>
<script>
import config from "@/config";

import { getOwnerlessImage } from "@/api/data";
const baseUrl =
  process.env.NODE_ENV === "development"
    ? config.baseUrl.dev
    : config.baseUrl.pro;
export default {
  name: "processImage",
  mounted() {
    getOwnerlessImage()
      .then(resp => {
        this.imageList.splice(0, this.imageList.length);
        for (let img of resp.data) {
          this.imageList.push(img);
        }
      })
      .catch(err => {
        alert(err);
      });
  },
  data() {
    return {
      imageList: [],
      formItem: {},
      rules: {}
    };
  },
  computed: {},
  methods: {
    handleSubmit() {
      this.$refs.newDoc.validate(valid => {
        if (valid) {
          this.newDoc();
        }
      });
    },
    newDoc() {},
    showPdfReport(idx) {
      let url = '';
      window.open(url);
    }
  }
};
</script>
