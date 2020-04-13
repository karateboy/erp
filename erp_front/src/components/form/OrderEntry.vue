<template>
  <div>
    <b-form @input="onChange" @reset="onReset">
      <b-form-group :label="`${product}-`" label-cols="2" label-align="right" class="mb-0">
        <b-input-group>
          <b-form-input
            v-model="filter"
            type="search"
            id="filterInput"
            placeholder="Type to Search"
          ></b-form-input>
        </b-input-group>
      </b-form-group>
    </b-form>

    <div v-if="showList">
      <!-- <div>
        <b-card-group deck>
          <div v-for="item in selected" :key="item._id">
            <b-card class="text-center" size="sm">
              <b-card-text>
                <h6>{{item.product_code}}-{{item.product_number}} {{item.product_spec}}</h6>
                <p>
                  <small>{{item.ext_ref}}</small>
                </p>
                <p>
                  <small>{{item.print_type}}</small>
                </p>
              </b-card-text>
            </b-card>
          </div>
        </b-card-group>
      </div>-->

      <b-table
        striped
        hover
        small
        show-empty
        ref="selectableTable"
        selectable
        :select-mode="selectMode"
        :items="list"
        :fields="fields"
        :filter="filter"
        :filterIncludedFields="filterOn"
        @row-selected="onRowSelected"
      >
        <!-- A virtual column -->
        <template v-slot:cell(ticker)="{ rowSelected }">
          <template v-if="rowSelected">
            <span aria-hidden="true">&check;</span>
            <span class="sr-only">Selected</span>
          </template>
          <template v-else>
            <span aria-hidden="true">&nbsp;</span>
            <span class="sr-only">Not selected</span>
          </template>
        </template>
        <template v-slot:cell(name)="data">
          <span>
            <a
              :href="`/product/${data.item.ptn}?${data.item._id}`"
              target="_blank"
            >{{data.item.product_code }}-{{ data.item.product_number }}</a>
          </span>
        </template>

        <template v-slot:cell(index)="data">{{ data.index+1 }}</template>
      </b-table>
    </div>
    <!-- {{new_orders}} -->
  </div>
</template>

<script>
import { ApiService } from "@/api/apiServices";

const moduleApi = {
  list(collection, params) {
    return ApiService.query(collection, {
      params: params
    });
  }
};
export default {
  props: {
    product: String
  },
  data() {
    return {
      selectMode: "multi",
      selected: [],
      list: [],
      showList: false,
      filter: "",
      filterOn: ["product_code", "product_number", "ext_ref"],

      fields: [
        { key: "ticker", label: "T" },
        { key: "index", label: "Seq" },
        { key: "name", label: "รหัสสินค้า" },
        { key: "product_spec" },
        { key: "ext_ref" },
        { key: "product_name" },
        { key: "paper_code" },
        { key: "stock" },
        { key: "print_type" }
      ]
    };
  },
  mounted() {},
  methods: {
    removeTags() {
      console.log("!@#@!$!@$!@");
      this.$store.commit("order" + "/SET_NEW_ORDERS", this.selected);
    },

    onRowSelected(items) {
      var itemSelected = [];
      items.forEach(function(item) {
        // console.log(item);
        var order = {
          _id: item._id,
          ptn: item.ptn,
          number: item.product_number,
          spec: item.product_spec
        };
        itemSelected.push(order);
      });
      // this.selected = this.selected.concat(itemSelected);
      // this.selected = Array.from(new Set(this.selected));
      this.selected = items;
      this.$store.commit("order" + "/SET_NEW_ORDERS", this.selected);

      console.log(this.selected);
    },
    selectAllRows() {
      this.$refs.selectableTable.selectAllRows();
    },
    clearSelected() {
      this.$refs.selectableTable.clearSelected();
    },
    onSubmit(evt) {
      evt.preventDefault();
      console.log(this.selected);
      alert(JSON.stringify(this.selected));
      this.$store.commit("order" + "/SET_NEW_ORDERS", this.selected);

      // this.new_orders.push(this.new_order);
      this.selected = [];
    },
    onReset(evt) {
      evt.preventDefault();
      this.new_order = {};
      this.$nextTick(() => {
        this.show = true;
      });
    },
    onChange() {
      var re = /\d{3,}/;
      re.test(this.filter);
      if (this.filter.length > 5 || re.test(this.filter)) {
        this.showList = true;
        moduleApi
          .list("/product/customer/" + this.product, "")
          .then(response => {
            this.list = response.data;
          });
      } else {
        this.showList = false;
      }
    }
  },
  computed: {}
};
</script>

<style lang="scss" scoped>
</style>