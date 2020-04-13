<template>
  <div>
    <b-form-group label="Tagged input using dropdown">
      <b-form-tags v-model="value" no-outer-focus class="mb-2">
        <template v-slot="{ tags, disabled, addTag, removeTag }">
          <ul v-if="tags.length > 0" class="list-inline d-inline-block mb-2">
            <li v-for="tag in tags" :key="tag" class="list-inline-item">
              <b-form-tag
                @remove="removeTag(tag)"
                :title="tag"
                :disabled="disabled"
                variant="info"
              >{{ tag }}</b-form-tag>
            </li>
          </ul>

          <b-dropdown size="sm" boundary variant="outline-secondary" block menu-class="w-100">
            <template v-slot:button-content>
              <b-icon icon="tag-fill"></b-icon>Choose tags
            </template>
            <b-dropdown-form @submit.stop.prevent="() => {}">
              <b-form-group
                label-for="tag-search-input"
                label="Search tags"
                label-cols-md="auto"
                class="mb-0"
                label-size="sm"
                :description="searchDesc"
                :disabled="disabled"
              >
                <b-form-input
                  v-model="search"
                  id="tag-search-input"
                  type="search"
                  size="sm"
                  autocomplete="off"
                ></b-form-input>
              </b-form-group>
            </b-dropdown-form>
            <b-dropdown-divider></b-dropdown-divider>
            <div v-if="search.length >1">
              <b-dropdown-item-button
                v-for="option in availableOptions"
                :key="option"
                @click="onOptionClick({ option, addTag })"
              >{{ option }}</b-dropdown-item-button>
            </div>
            <b-dropdown-text
              v-if="availableOptions.length === 0"
            >There are no tags available to select</b-dropdown-text>
          </b-dropdown>
        </template>
      </b-form-tags>
    </b-form-group>
  </div>
</template>

<script>
import { mapActions, mapGetters } from "vuex";

export default {
  props: {
    codes: { types: Array }
  },
  data() {
    return {
      //   options: [],
      search: "",
      value: []
    };
  },
  mounted() {
    this.$store.dispatch(
      "customer/FETCH_PRODUCT_CODES",
      "/customer/list_codes"
    );
  },
  computed: {
    ...mapGetters("customer", ["product_codes"]),

    options() {
      const options = [];
      for (let code of this.product_codes) {
        options.push(code.customer_code);
        // console.log(code.customer_code);
      }
      return options;
    },
    criteria() {
      // Compute the search criteria
      return this.search.trim().toLowerCase();
    },
    availableOptions() {
      const criteria = this.criteria;
      // Filter out already selected options
      const options = this.options.filter(
        opt => this.value.indexOf(opt) === -1
      );
      if (criteria) {
        // Show only options that match criteria
        return options.filter(opt => opt.toLowerCase().indexOf(criteria) > -1);
      }
      // Show all options available
      return options;
    },
    searchDesc() {
      if (this.criteria && this.availableOptions.length === 0) {
        return "There are no tags matching your search criteria";
      }
      return "";
    }
  },
  methods: {
    ...mapActions("customer", ["/FETCH_PRODUCT_CODES"]),

    onOptionClick({ option, addTag }) {
      addTag(option);
      this.search = "";
    }
  }
};
</script>