<template>
  <b-container fluid v-if="newOrders.length > 0" class="border border-danger rounded bg-warning">
    <b-form @submit="onSubmit" @reset="onReset">
      <b-row>
        <b-input-group v-for="order in newOrders" :key="order._id">
          <b-col>
            <b-form-group :id="order._id" :description="`${order.ext_ref}`">
              <b-input-group
                size="sm"
                variant="info"
                :prepend="`${order.product_code} - ${order.product_number} ${order.product_spec} `"
              >
                <b-input-group-append>
                  <b-button variant="danger">Del</b-button>
                </b-input-group-append>
              </b-input-group>
            </b-form-group>
          </b-col>
          <b-col>
            <b-form-group
              :id="`${order._id}-qty-group`"
              :description="`จำนวน 訂單數量: ${order.qty}`"
              label-for="input-1"
              :invalid-feedback="invalidFeedback"
              :valid-feedback="validFeedback"
              :state="state"
            >
              <b-form-input
                size="sm"
                :id="`${order._id}-qty`"
                small
                v-model="order.qty"
                :state="state"
                trim
              ></b-form-input>
            </b-form-group>
          </b-col>
          <b-col>
            <b-form-group
              :id="`${order._id}-due-group`"
              :description="`กำหนดส่ง出貨日期 ${order.due_date}`"
              label-for="input-1"
              :invalid-feedback="invalidFeedback"
              :valid-feedback="validFeedback"
              :state="state"
            >
              <b-form-datepicker
                size="sm"
                :id="`${order._id}-due`"
                :min="min_date"
                today-button
                v-model="order.due_date"
                locale="th"
              ></b-form-datepicker>
            </b-form-group>
          </b-col>
          <b-col>
            <b-form-group
              :id="`${order._id}-type-group`"
              :description="`Work Type ${order.type}`"
              label-for="input-1"
              :invalid-feedback="invalidFeedback"
              :valid-feedback="validFeedback"
              :state="state"
            >
              <b-form-radio-group
                size="sm"
                :id="`${order._id}-type`"
                v-model="order.type"
                :options="options"
                buttons
                :button-variant="options.variant"
                :name="`${order._id}-radio`"
              ></b-form-radio-group>
            </b-form-group>
          </b-col>
        </b-input-group>
      </b-row>
      <b-button type="submit" variant="primary">Submit</b-button>
      <b-button type="reset" variant="danger">Reset</b-button>
    </b-form>
    <!-- {{orders}} -->
  </b-container>
</template>

<script>
import { mapGetters } from "vuex";

export default {
  props: {
    product: String
  },
  data() {
    const now = new Date();
    const today = new Date(now.getFullYear(), now.getMonth(), now.getDate());
    const min_date = new Date(today);

    return {
      selectMode: "multi",
      selected: [],
      list: [],
      min_date: min_date,
      new_order: {},
      new_orders: [],
      options: [
        { value: "#FFF", text: "Normal" },
        { value: "#F00", text: "Urgent" },
        { value: "#00F", text: "Sample" },
        { value: "#0FF", text: "Sample Order" },
        { value: "#FF0", text: "Repair" },
        { value: "#0F0", text: "Block" }
      ]
    };
  },
  mounted() {},
  methods: {
    onSubmit(evt) {
      evt.preventDefault();
      alert(JSON.stringify(this.new_order));
      this.new_orders.push(this.new_order);
      this.new_order = {};
    },
    onReset(evt) {
      evt.preventDefault();
      this.new_order = {};
      this.$nextTick(() => {
        this.show = true;
      });
    }
  },
  computed: {
    ...mapGetters("order", ["newOrders", "appDocument"])
  }
};
</script>

<style scoped>
form {
  padding: 1em 0;
}
.container-fluid{
  margin: 1em 0;
}
</style>