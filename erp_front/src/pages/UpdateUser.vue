<template>
  <b-form>
    <b-form-group
      label-cols="4"
      label-cols-lg="2"
      label-size="lg"
      label="User to be deleted:"
      label-for="update-user"
    >
      <b-form-radio-group
        id="update-user"
        v-model="form.user"
        :options="userList"
        :state="userState"
      >
        <b-form-invalid-feedback :state="userState">Must select one user</b-form-invalid-feedback>
      </b-form-radio-group>
    </b-form-group>
    <hr />    
    <user v-if="userState" :isNew="false" :_id="form.user" :key="form.user"></user>
  </b-form>
</template>
<script lang="ts">
import Vue from "vue";
import axios from "axios";
import User from "./User.vue"
interface Entry {
  text: string;
  value: string;
}

export default Vue.extend({
  components:{
    User
  },
  mounted() {
    this.getUserList();
  },
  data() {
    return {
      form: {
        user: ""
      },
      userList: Array<Entry>()
    };
  },
  computed: {
    userState() {
      if (!this.form.user) return false;
      else return true;
    }
  },
  methods: { 
    getUserList() {
      axios
        .get("/user")
        .then(res => {
          const ret = res.data;
          this.userList.splice(0, this.userList.length);
          for (let usr of ret) {
            let text: string;
            if (usr.name) text = usr.name;
            else text = usr._id;

            let entry = {
              text,
              value: usr._id
            };
            this.userList.push(entry);
          }
        })
        .catch(err => alert(err));
    }
  }
});
</script>