<template>
  <div>
    <b-form>
      <b-form-group label="ID:">{{doc._id}}</b-form-group>
      <b-form-group label="Tags:">{{doc.tags}}</b-form-group>
      <b-form-group label="Date time:">{{displayLocalTime(doc.dateTime)}}</b-form-group>
      <div class="flex-wrap flex-column">
      <div v-for="id in doc.images" :key="id">
        <b-img :src="imageUrl(id)" fluid thumbnail />
      </div>
    </div>
    </b-form>
  </div>
</template>
<script lang="ts">
import Vue from "vue";
import axios from "axios";
import moment from "moment";
import moment_tz from "moment-timezone";
export default Vue.extend({
  props: ["_id"],
  mounted() {
    this.loadDefaultTags();
    this.loadDocument();
  },
  data() {
    let tags: string[] = [];
    return {
      doc: {
        _id: "",
        tags: [],
        date: 0,
        text: "",
        images: []
      },
      tags,
      docReady: false
    };
  },
  methods: {
    onSubmit() {},
    loadDefaultTags() {
      axios
        .get("/tags")
        .then(res => {
          const ret = res.data;
          this.tags.splice(0, this.tags.length);
          for (let id of ret) {
            this.tags.push(id);
          }
        })
        .catch(err => alert(err));
    },
    loadDocument() {
      axios
        .get(`/doc/${this._id}`)
        .then(res => {
          const ret = res.data;
          this.doc = Object.assign({}, this.doc, ret);
          this.docReady = true;
        })
        .catch(err => alert(err));
    },
    displayLocalTime(dt:number) {
      return moment(dt)
        .tz("Asia/Bangkok")
        .format("lll");
    },
    imageUrl(id:string) {
      return process.env.NODE_ENV === "development"
        ? `http://localhost:9000/image/${id}`
        : `/image/${id}`;
    },
  }
});
</script>