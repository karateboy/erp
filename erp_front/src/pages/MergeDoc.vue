<template>
  <div>
    <b-modal
      id="detailModal"
      :title="`Tags: ${selectedImageParam.tags.join()}`"
      size="xl"
      header-bg-variant="info"
      centered
      scrollable
      ok-only
    >
      <b-img
        v-if="isImage(selectedImageParam.fileName)"
        :src="fileUrl(selectedImageParam._id)"
        fluid
        thumbnail
      />
    </b-modal>
    <b-form @submit="onSubmit" @reset="onReset">
      <b-form-group label="Existing tags:">
        <b-form-checkbox-group id="existing-tags" v-model="form.tags" :options="tags"></b-form-checkbox-group>
        <b-form-tags v-model="form.tags" class="mb-2"></b-form-tags>
      </b-form-group>
      <b-button type="submit" variant="primary">Submit</b-button>&nbsp;
      <b-button type="reset" variant="danger">Reset</b-button>
      <b-form-checkbox-group id="imageIdGroup" v-model="form.mergeImageId">
        <b-container fluid>
          <b-row v-for="(row, idx) in rows" :key="idx">
            <b-col v-for="param in row" :key="param._id">
              <b-form-checkbox :value="param._id" />
              {{param.tags}}
              <b-img
                v-if="isImage(param.fileName)"
                :src="fileUrl(param._id)"
                fluid
                thumbnail
                v-b-popover.hover.top="'Click for detail!'"
                @click="showDetail(param)"
              />
              <div v-else-if="isPdf(param.fileName)" class="embed-responsive embed-responsive-1by1">
                <iframe class="embed-responsive-item" :src="fileUrl(param._id)"></iframe>
              </div>
              <div v-else-if="isExcel(param.fileName)">
                <a
                  class="btn btn-info"
                  :href="fileNameUrl(param.fileName)"
                  download
                  target="_blank"
                >
                  <font-awesome-icon icon="file-excel" size="lg" />&nbsp; Excel File
                </a>
              </div>
            </b-col>
          </b-row>
        </b-container>
      </b-form-checkbox-group>
      <hr />
      <b-button variant="info" @click.prevent="moreImage">Show more...</b-button>
    </b-form>
  </div>
</template>
<script lang="ts">
import Vue from "vue";
import axios from "axios";
import { ImageParam, NewDocParam } from "./types";

export default Vue.extend({
  mounted() {
    this.loadOwnerlessImage();
    this.loadDefaultTags();
  },
  computed: {
    rows(): Array<Array<ImageParam>> {
      let ret = Array<Array<ImageParam>>();
      let start = 0;
      while (start < this.ownerlessImages.length) {
        let slice = this.ownerlessImages.slice(start, start + 4);
        ret.push(slice);
        start += 4;
      }
      return ret;
    }
  },
  data() {
    let form: NewDocParam = {
      _id: "",
      mergeImageId: [],
      tags: Array<string>()
    };
    let selectedImageParam: ImageParam = {
      _id: "",
      fileName: "",
      tags: []
    };

    return {
      form,
      ownerlessImages: Array<ImageParam>(),
      tags: Array<string>(),
      selectedImageParam
    };
  },
  watch: {
    "form.mergeImageId": function(newValue, oldValue) {
      let set = new Set();

      if (newValue.length > oldValue.length) {
        for (let tag of this.form.tags) {
          set.add(tag);
        }
        let newTags = this.getTags(newValue[newValue.length - 1]);
        for (let tag of newTags) {
          if (!set.has(tag)) this.form.tags.push(tag);
        }
      } else {
        for (let _id of newValue) {
          let newTags = this.getTags(_id);
          for (let tag of newTags) set.add(tag);
        }
        let newTags = this.form.tags.filter(x => set.has(x));
        this.form.tags.splice(0, this.form.tags.length);
        for (let tag of newTags) {
          this.form.tags.push(tag);
        }
      }
    }
  },
  methods: {
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
    validateForm(form: NewDocParam) {
      if (form.mergeImageId.length === 0) {
        this.$bvModal
          .msgBoxOk("No image is selected!")
          .then(value => {})
          .catch(err => alert(err));
        return false;
      }

      if (form.tags.length === 0) {
        this.$bvModal
          .msgBoxOk("No tags!")
          .then(value => {})
          .catch(err => alert(err));
        return false;
      }

      return true;
    },
    onSubmit(evt: Event) {
      evt.preventDefault();
      if (!this.validateForm(this.form)) return;

      axios
        .post("/newDoc", this.form)
        .then(res => {
          const ret = res.data;
          if (ret.ok) {
            alert("Success");
            this.onReset(evt);
          }
        })
        .catch(err => alert(err));
    },
    onReset(evt: Event) {
      evt.preventDefault();
      this.loadOwnerlessImage();
    },
    loadOwnerlessImage() {
      axios
        .get(`/ownerless-image?skip=${0}`)
        .then(res => {
          const ret = res.data;
          this.ownerlessImages.splice(0, this.ownerlessImages.length);
          for (let id of ret) {
            this.ownerlessImages.push(id);
          }
        })
        .catch(err => alert(err));
    },
    moreImage() {
      axios
        .get(`/ownerless-image?skip=${this.ownerlessImages.length}`)
        .then(res => {
          const ret = res.data;
          for (let id of ret) {
            this.ownerlessImages.push(id);
          }
        })
        .catch(err => alert(err));
    },
    loadDefaultTags() {
      axios
        .get("/tags")
        .then(res => {
          const ret: string[] = res.data;
          this.tags.splice(0, this.tags.length);
          for (let id of ret) {
            this.tags.push(id);
          }
        })
        .catch(err => alert(err));
    },
    getTags(_id: string): string[] {
      for (let param of this.ownerlessImages) {
        if (param._id === _id) {
          return param.tags;
        }
      }
      return [];
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
    excelUrl(id: string): string {
      return `https://view.officeapps.live.com/op/embed.aspx?src=${this.fileUrl(
        id
      )}`;
    },
    showDetail(param: ImageParam) {
      this.selectedImageParam._id = param._id;
      this.selectedImageParam.fileName = param.fileName;
      this.selectedImageParam.tags.splice(
        0,
        this.selectedImageParam.tags.length
      );
      for (let tag of param.tags) {
        this.selectedImageParam.tags.push(tag);
      }
      this.$bvModal.show("detailModal");
    }
  }
});
</script>