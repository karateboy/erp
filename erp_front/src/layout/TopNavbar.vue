<template>
  <nav class="navbar navbar-expand-lg">
    <b-container fluid>
      <b-row>
        <b-col>
          <b-button
            :class="{toggled: $sidebar.showSidebar}"
            aria-controls="navigation-index"
            aria-expanded="false"
            aria-label="Toggle navigation"
            @click="toggleSidebar"
          >
            <span class="navbar-toggler-bar burger-lines"></span>
            <span class="navbar-toggler-bar burger-lines"></span>
            <span class="navbar-toggler-bar burger-lines"></span>
          </b-button>
          <b-collapse id="nav-collapse" is-nav>
            <b-navbar-nav>
              <b-nav-item href="/customer">Customer</b-nav-item>
              <b-nav-item href="/product">Product</b-nav-item>
              <b-nav-item href="/order">Order</b-nav-item>
              <b-nav-item href="/work">Work</b-nav-item>
              <b-nav-item-dropdown text="Order">
                <b-dropdown-item href="/order">Order</b-dropdown-item>
                <b-dropdown-item href="/work">Work</b-dropdown-item>
                <b-dropdown-item href="/planning">Planning</b-dropdown-item>
                <b-dropdown-item href="/printing">Printing</b-dropdown-item>
              </b-nav-item-dropdown>
              <b-nav-item-dropdown text="Store">
                <b-dropdown-item href="/store">Store</b-dropdown-item>
                <b-dropdown-item href="/material">Material</b-dropdown-item>
                <b-dropdown-item href="/product/store">Product Store</b-dropdown-item>
              </b-nav-item-dropdown>

              <b-nav-item-dropdown text="Production">
                <b-dropdown-item href="/dicut">Dicut</b-dropdown-item>
                <b-dropdown-item href="/rubber">Rubber</b-dropdown-item>
                <b-dropdown-item href="/oven">Oven</b-dropdown-item>
              </b-nav-item-dropdown>
              <b-nav-item-dropdown text="ISO">
                <b-dropdown-item href="/iso">ISO</b-dropdown-item>
                <b-dropdown-item href="/qc">QC</b-dropdown-item>
                <b-dropdown-item href="/qa">QA</b-dropdown-item>
                <b-dropdown-item href="/logistics">Logistics</b-dropdown-item>
              </b-nav-item-dropdown>
            </b-navbar-nav>

            <!-- Right aligned nav items -->
            <b-navbar-nav class="ml-auto">
              <b-nav-form>
                <b-form-input size="sm" class="mr-sm-2" placeholder="Search"></b-form-input>
                <b-button size="sm" class="my-2 my-sm-0" type="submit">Search</b-button>
              </b-nav-form>

              <b-nav-item-dropdown text="Lang" right>
                <b-dropdown-item href="#">EN</b-dropdown-item>
                <b-dropdown-item href="#">ES</b-dropdown-item>
                <b-dropdown-item href="#">RU</b-dropdown-item>
                <b-dropdown-item href="#">FA</b-dropdown-item>
              </b-nav-item-dropdown>

              <b-nav-item-dropdown right>
                <!-- Using 'button-content' slot -->
                <template v-slot:button-content>
                  <em>User</em>
                </template>
                <b-dropdown-item href="#">Profile</b-dropdown-item>
                <b-dropdown-item href="/logout">Sign Out</b-dropdown-item>
              </b-nav-item-dropdown>
            </b-navbar-nav>
          </b-collapse>
        </b-col>
      </b-row>
    </b-container>
  </nav>
</template>
<script>
import { mapState } from "vuex";
export default {
  computed: {
  },
  data() {
    return {
      activeNotifications: false
    };
  },
  computed: {
    ...mapState(["userInfo"])
  },
  methods: {
    toggleNotificationDropDown() {
      this.activeNotifications = !this.activeNotifications;
    },
    closeDropDown() {
      this.activeNotifications = false;
    },
    toggleSidebar() {
      this.$sidebar.displaySidebar(!this.$sidebar.showSidebar);
    },
    hideSidebar() {
      this.$sidebar.displaySidebar(false);
    }
  }
};
</script>
<style>
</style>
