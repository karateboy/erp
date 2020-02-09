<template>
  <div>
    <b-form class="border">
      <b-form-group label="Document tags:" label-for="tags" description="Please select tag.">
        <b-form-checkbox-group id="tags" v-model="form.tags" :options="tags"></b-form-checkbox-group>
      </b-form-group>
      <b-button variant="primary" @click.prevent="onSubmit">Query</b-button>
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
    <b-modal id="docModal" :title="docTitle" size="xl" header-bg-variant="info" scrollable>
      <image-doc :_id="docID"></image-doc>
    </b-modal>
  </div>
</template>
<script lang="ts">
import Vue from "vue";
import axios from "axios";
import moment from "moment";
import moment_tz from "moment-timezone";
import ImageDoc from "./ImageDoc.vue";
interface SearchForm {
  tags: string[];
  skip: number;
  limit: number;
}

interface ShortDocJson {
  _id: string, 
  tags: string[], 
  dateTime: number  
}

interface DisplayDocEntry {
  _id: string, 
  tags: string[], 
  date: string  
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
      selected ,
      docTitle: "doc title",
      docID: ""
    };
  },
  methods: {
    onRowSelected(items : ShortDocJson[]) {
      this.selected = items;
      this.docID = items[0]._id;
      this.$bvModal.show("docModal");
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
      let workaround = {
        tags: this.form.tags.join(","),
        skip: this.form.skip,
        limit: this.form.limit
      };
      axios
        .get("/doc", {
          params: workaround
        })
        .then(res => {
          const ret = res.data;

          this.searchResult.splice(0, this.searchResult.length);
          for (let doc of ret) {
            let entry : DisplayDocEntry = {
              _id: doc._id,
              tags: doc.tags,
              date: moment(doc.dateTime)
                .tz("Asia/Bangkok")
                .format("lll")
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