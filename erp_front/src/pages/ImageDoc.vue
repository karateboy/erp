<template>
  <div>
    <b-form>
      <b-form-group label-cols="4" label-cols-lg="2" label-size="lg" label="ID" label-for="ID">
        <b-form-input id="ID" size="lg" readonly :value="_id" />
      </b-form-group>
      <b-form-group label-cols="4" label-cols-lg="2" label-size="lg" label="Tags" label-for="tags">
        <b-form-tags id="tags" v-model="doc.tags" />
      </b-form-group>
      <b-form-group
        label-cols="4"
        label-cols-lg="2"
        label-size="lg"
        label="Created:"
        label-for="dateTime"
      >
        <b-form-input id="dateTime" :value="displayLocalTime(doc.dateTime)" readonly />
      </b-form-group>
      <b-form-file
        name="image"
        v-model="file"
        placeholder="Choose a file or drop it here to upload..."
        drop-placeholder="Drop file here..."
        @input="startUpload"
      ></b-form-file>
      <b-form-group>
        <b-button variant="info" @click.prevent="updateDoc">Update</b-button>
      </b-form-group>
      <b-container fluid>
        <b-row v-for="(param, idx) in doc.imageParam" :key="idx">
          <b-col>
            <div v-if="isPdf(param.fileName)" class="embed-responsive embed-responsive-1by1">
              <iframe class="embed-responsive-item" :src="fileUrl(param._id)"></iframe>
            </div>
            <div v-else-if="isExcel(param.fileName)">
              <a class="btn btn-info" :href="fileNameUrl(param.fileName)" download target="_blank">
                <font-awesome-icon icon="file-excel" size="lg" />&nbsp; Excel File
              </a>
            </div>
            <b-img v-else :src="fileUrl(param._id)" fluid thumbnail />
          </b-col>
        </b-row>
      </b-container>
    </b-form>
  </div>
</template>
<style>
.dragUpload {
  height: 50px;
  background-color: aqua;
}
</style>
<script lang="ts">
import Vue from "vue";
import axios from "axios";
import moment from "moment";
import { ImageParam, NewDocParam } from "./types";

interface upload {
  active: boolean;
}

export default Vue.extend({
  props: ["_id"],
  mounted() {
    this.loadDocument();
  },
  computed: {
    uploadUrl() {
      let baseUrl =
        process.env.NODE_ENV === "development" ? "http://localhost:9000/" : "/";
      return `${baseUrl}doc/image/${this._id}`;
    }
  },
  data() {
    let file: File | null = null;
    return {
      doc: {
        _id: "",
        tags: Array<string>(),
        date: 0,
        text: "",
        images: Array<string>(),
        imageParam: Array<ImageParam>()
      },
      file
    };
  },
  methods: {
    loadDocument() {
      axios
        .get(`/doc/${this._id}`)
        .then(res => {
          const ret = res.data;
          this.doc = Object.assign(this.doc, ret);
          /*
          this.doc.tags.splice(0, this.doc.tags.length);
          for (let tag of ret.tags) {
            this.doc.tags.push(tag);
          }
          this.doc.text = ret.text;
          this.doc.images.splice(0, this.doc.images.length);
          for (let imageId of ret.images) {
            this.doc.images.push(imageId);
          }
          this.doc.imageParam.splice(0, this.doc.imageParam.length);
          */
          this.populateImageParam();
        })
        .catch(err => alert(err));
    },
    displayLocalTime(dt: number) {
      return moment(dt).format("lll");
    },
    imageUrl(id: string) {
      return process.env.NODE_ENV === "development"
        ? `http://localhost:9000/image/${id}`
        : `/image/${id}`;
    },
    populateImageParam() {
      axios
        .get(`/imageParam?idList=${this.doc.images.join()}`)
        .then(res => {
          const ret = res.data;
          console.log(ret);
          for (let param of ret) {
            this.doc.imageParam.push(param);
          }
        })
        .catch(err => alert(err));
    },
    isImage(fileName: string): boolean {
      if (
        fileName.endsWith("jpg") ||
        fileName.endsWith("jpeg") ||
        fileName.endsWith("png") ||
        fileName.endsWith("bmp")
      )
        return true;
      else return false;
    },
    isPdf(fileName: string): boolean {
      return fileName.endsWith("pdf");
    },
    isExcel(fileName: string): boolean {
      return fileName.endsWith("xlsx");
    },
    fileUrl(id: string) {
      return process.env.NODE_ENV === "development"
        ? `http://localhost:9000/image/${id}`
        : `/image/${id}`;
    },
    fileNameUrl(fileName: string) {
      return process.env.NODE_ENV === "development"
        ? `http://localhost:9000/file/${fileName}`
        : `/image/${fileName}`;
    },
    updateDoc() {
      axios.post("/doc", this.doc).then(res => {
        const ret = res.data;
        if (ret.ok) this.$bvModal.msgBoxOk("Success!").catch(err => alert(err));
        else this.$bvModal.msgBoxOk("Failed!").catch(err => alert(err));
      });
    },
    startUpload() {
      let formData = new FormData();
      let file = (this.file as unknown) as File;
      formData.append("image", file);
      axios
        .post(this.uploadUrl, formData, {
          headers: {
            "Content-Type": "multipart/form-data"
          }
        })
        .then(res => {
          const ret = res.data;
          if (ret.ok){
            this.$bvModal.msgBoxOk("Success!").catch(err => alert(err));
            this.loadDocument();
          }
            
        });
    }
  }
});
</script>