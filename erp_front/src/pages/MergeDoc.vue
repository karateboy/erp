<template>
  <div>
    <b-form @submit="onSubmit" @reset="onReset">
      <b-form-checkbox-group id="imageIdGroup" v-model="form.mergeImageId">
        <b-container>
          <b-row>
            <b-col v-for="(id) in row1" :key="id">
              <b-form-checkbox :value="id" />
              <b-img :src="imageUrl(id)" fluid thumbnail />
            </b-col>
          </b-row>
          <b-row>
            <b-col v-for="(id) in row2" :key="id">
              <b-form-checkbox :value="id" />
              <b-img :src="imageUrl(id)" fluid thumbnail />
            </b-col>
          </b-row>
          <b-row>
            <b-col v-for="(id) in row3" :key="id">
              <b-form-checkbox :value="id" />
              <b-img :src="imageUrl(id)" fluid thumbnail />
            </b-col>
          </b-row>
        </b-container>
      </b-form-checkbox-group>
      <b-form-group label="Existing tags:">
        <b-form-checkbox-group id="existing-tags" v-model="form.tags" :options="tags"></b-form-checkbox-group>
        <b-form-tags v-model="form.tags" class="mb-2"></b-form-tags>
      </b-form-group>

      <b-button type="submit" variant="primary">Submit</b-button>
      <b-button type="reset" variant="danger">Reset</b-button>
    </b-form>
  </div>
</template>
<script lang="ts">
import Vue from "vue";
import axios from "axios";
export default Vue.extend({
  mounted() {
    this.loadOwneressImage();
    this.loadDefaultTags();
  },
  computed: {
    row1() {
      return this.ownerlessImages.slice(0, 4);
    },
    row2() {
      return this.ownerlessImages.slice(4, 8);
    },
    row3() {
      return this.ownerlessImages.slice(8, 12);
    }
  },
  data() {
    return {
      form: {
        _id: "",
        mergeImageId: [],
        tags: []
      },
      ownerlessImages: [],
      tags: []
    };
  },
  methods: {
    imageUrl(id) {
      return process.env.NODE_ENV === "development"
        ? `http://localhost:9000/image/${id}`
        : `/image/${id}`;
    },
    validateForm(form) {
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
    onSubmit(evt) {
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
    onReset(evt) {
      evt.preventDefault();
      this.form.tags.splice(0, this.form.tags.length);
      this.form.mergeImageId.splice(0, this.form.mergeImageId.length);
      this.loadOwneressImage();
      this.loadDefaultTags();
    },
    loadOwneressImage() {
      axios
        .get("/ownerless-image")
        .then(res => {
          const ret = res.data;
          this.ownerlessImages.splice(0, this.ownerlessImages.length);
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
          const ret = res.data;
          this.tags.splice(0, this.tags.length);
          for (let id of ret) {
            this.tags.push(id);
          }
        })
        .catch(err => alert(err));
    }
  }
});
</script>