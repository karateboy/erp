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
    <b-form-group
      label-cols="4"
      label-cols-lg="2"
      label-size="lg"
      label="password"
      label-for="password"
    >
      <b-form-input id="password" type="password" size="lg" v-model="form.password" />
    </b-form-group>
    <b-form-group
      label-cols="4"
      label-cols-lg="2"
      label-size="lg"
      label="retype password"
      label-for="password2"
    >
      <b-form-input id="password2" type="password" size="lg" v-model="form.password2" />
    </b-form-group>

    <b-form-group label-cols="4" label-cols-lg="2" label-size="lg" label="Group" label-for="group">
      <b-button-group id="group">
        <b-button
          v-for="(group) in groupList"
          :key="group"
          @click="form.group=group"
          :class="{ active: group==form.group}"
          variant="outline-info"
        >{{group}}</b-button>
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
        user: "",
        password:"",
        password2:"",
        group:""
      },
      userList: Array<string>(),
      groupList: ["admin", "RD", "Factory", "QC"]
    };
  },
  methods: {
    onSubmit(evt: Event) {
      evt.preventDefault();
    }
  }
});
</script>