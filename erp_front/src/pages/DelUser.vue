<template>
  <b-form>
    <b-form-group label-cols="4" label-cols-lg="2" label-size="lg" label="User:" label-for="user">
      <b-button-group id="user">
        <b-button
          v-for="user in userList"
          :key="user"
          @click="form.user=user"
          :class="{ active: user==form.user}"
          variant="outline-info"
        >{{user}}</b-button>
      </b-button-group>
    </b-form-group>
    <hr />
    <b-button type="submit" variant="primary">Delete User</b-button>
  </b-form>
</template>
<script lang="ts">
import Vue from "vue";
import axios from "axios";
export default Vue.extend({
  mounted() {
    axios
      .get("/user")
      .then(res => {
        const ret = res.data;
        this.userList.splice(0, this.userList.length);
        for (let usr of ret) {
          this.userList.push(usr);
        }
      })
      .catch(err => alert(err));
  },
  data() {
    return {
      form: {
        user: ""
      },
      userList: Array<string>()
    };
  },
  methods: {
    onSubmit(evt: Event) {
      evt.preventDefault();
    }
  }
});
</script>