<template>
  <div>
    <b-form class="border">
      <label for="start">Start:</label>
      <b-form-datepicker id="start" v-model="form.start" class="mb-2" value-as-date></b-form-datepicker>
      <label for="end">End:</label>
      <b-form-datepicker id="end" v-model="form.end" class="mb-2" value-as-date></b-form-datepicker>

      <b-form-group label="Document tags:" label-for="tags" description="Please select tag.">
        <b-form-checkbox-group id="tags" v-model="form.tags" :options="tags"></b-form-checkbox-group>
      </b-form-group>
      <b-button variant="primary" @click.prevent="onSubmit">Query</b-button>&nbsp;
      <b-button type="reset" variant="danger" @click.prevent="onReset">Reset</b-button>
    </b-form>
    <b-card v-if="searched" class="mt-3" header="Search Result">
      <strong v-if="searchResult.length === 0">No match!</strong>
      <strong v-else>Please click the document row for detail</strong>
      <b-table
        selectable
        hover
        select-mode="single"
        :fields="fields"
        :items="searchResult"
        @row-selected="onRowSelected"
      >
        <template v-slot:cell(selected)="{ rowSelected }">
          <template v-if="rowSelected">
            <span aria-hidden="true">&check;</span>
            <span class="sr-only">Selected</span>
          </template>
          <template v-else>
            <span aria-hidden="true">&nbsp;</span>
            <span class="sr-only">Not selected</span>
          </template>
        </template>
      </b-table>
    </b-card>
    <b-card class="mt-3" header="Document Detail" v-if="docID">
      <image-doc :_id="docID" :key="docID"></image-doc>
    </b-card>
  </div>
</template>
<script lang="ts">
import Vue from "vue";
import axios from "axios";
import moment from "moment";
import ImageDoc from "./ImageDoc.vue";
interface SearchForm {
  tags: string[];
  start?: Date;
  end?: Date;
  skip: number;
  limit: number;
}

interface ShortDocJson {
  _id: string;
  tags: string[];
  dateTime: number;
}

interface DisplayDocEntry {
  _id: string;
  tags: string[];
  date: string;
}

export default Vue.extend({
  components: {
    ImageDoc
  },
  mounted() {
    this.loadDefaultTags();
  },
  data() {
    let form: SearchForm = {
      tags: [],
      start: undefined,
      end: undefined,
      skip: 0,
      limit: 10
    };

    let selected = Array<ShortDocJson>();
    let searchResult = Array<DisplayDocEntry>();
    return {
      form,
      tags: Array<string>(),
      searchResult,
      searched: false,
      fields: ["selected", "tags", "date"],
      selected,
      docTitle: "doc title",
      docID: ""
    };
  },
  methods: {
    onRowSelected(items: ShortDocJson[]) {
      this.selected = items;
      this.docID = items[0]._id;
    },
    validateForm(form: SearchForm) {
      if (form.tags.length === 0) {
        this.$bvModal
          .msgBoxOk("No tags!")
          .then(value => {})
          .catch(err => alert(err));
        return false;
      }

      return true;
    },
    onSubmit() {
      this.searched = true;
      this.docID = "";
      let start = undefined;
      if (this.form.start) start = this.form.start.getTime();
      let end = undefined;
      if (this.form.end) end = this.form.end.getTime();

      let params = {
        tags: this.form.tags.join(","),
        start,
        end,
        skip: this.form.skip,
        limit: this.form.limit
      };
      axios
        .get("/doc", {
          params
        })
        .then(res => {
          const ret = res.data;

          this.searchResult.splice(0, this.searchResult.length);
          for (let doc of ret) {
            let entry: DisplayDocEntry = {
              _id: doc._id,
              tags: doc.tags,
              date: moment(doc.dateTime).format("lll")
            };
            this.searchResult.push(entry);
          }
        })
        .catch(err => alert(err));
    },
    onReset() {
      this.form.tags.splice(0, this.form.tags.length);
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