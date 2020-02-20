<template>
  <div>
    <b-card no-body>
      <b-tabs card>
        <b-tab title="New User" active>
          <user :isNew="true"></user>
        </b-tab>
        <b-tab title="Delete User">
          <del-user></del-user>
        </b-tab>
        <b-tab title="Update User">
          <udate-user />
        </b-tab>
      </b-tabs>
    </b-card>
  </div>
</template>
<script lang="ts">
import Vue from "vue";
import User from "./User.vue";
import DelUser from "./DelUser.vue";
import UdateUser from "./UpdateUser.vue";
export default Vue.extend({
  components: {
    User,
    DelUser,
    UdateUser
  }
});
</script>