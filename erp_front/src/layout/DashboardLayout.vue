<template>
  <div class="wrapper">
    <side-bar>
      <mobile-menu slot="content"></mobile-menu>
      <sidebar-link :to="{ name: 'MergeDoc'}">
        <i class="nc-icon nc-settings-gear-64"></i>
        <p>Merge Document</p>
      </sidebar-link>
      <sidebar-link :to="{ name: 'SearchDoc'}">
        <i class="nc-icon nc-zoom-split"></i>
        <p>Search Document</p>
      </sidebar-link>
      <sidebar-link :to="{ name: 'UserManagement'}" v-if="isAdmin">
        <i class="nc-icon nc-circle-09"></i>
        <p>User Management</p>
      </sidebar-link>
      <!--       <sidebar-link to="/admin/icons">
        <i class="nc-icon nc-atom"></i>
        <p>Icons</p>
      </sidebar-link>
      <sidebar-link to="/admin/notifications">
        <i class="nc-icon nc-bell-55"></i>
        <p>Notifications</p>
      </sidebar-link>-->
    </side-bar>
    <div class="main-panel">
      <top-navbar></top-navbar>

      <dashboard-content @click="toggleSidebar"></dashboard-content>

      <!-- <content-footer></content-footer> -->
    </div>
  </div>
</template>
<style lang="scss">
</style>
<script>
import TopNavbar from "./TopNavbar.vue";
import ContentFooter from "./ContentFooter.vue";
import DashboardContent from "./Content.vue";
import MobileMenu from "./MobileMenu.vue";
import { mapState } from "vuex";
export default {
  components: {
    TopNavbar,
    DashboardContent,
    MobileMenu
  },
  computed: {
    ...mapState(["userInfo"]),
    isAdmin() {
      return this.userInfo.groups.indexOf("admin") !== -1;
    }
  },
  methods: {
    toggleSidebar() {
      if (this.$sidebar.showSidebar) {
        this.$sidebar.displaySidebar(false);
      }
    }
  }
};
</script>
