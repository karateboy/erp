<template>
  <b-form>
    <b-form-group
      label-cols="4"
      label-cols-lg="2"
      label-size="lg"
      label="User to be deleted:"
      label-for="delete-users"
    >
      <b-form-radio-group
        id="delete-users"
        v-model="form.user"
        :options="userList"
        :state="userState"
      >
        <b-form-invalid-feedback :state="userState">Must select one user</b-form-invalid-feedback>
      </b-form-radio-group>
    </b-form-group>
    <hr />
    <b-button
      type="submit"
      variant="primary"
      :disabled="!userState"
      @click.prevent="deleteUser"
    >Delete User</b-button>
  </b-form>
</template>
<script lang="ts">
import Vue from "vue";
import axios from "axios";
interface Entry {
  text: string;
  value: string;
}

export default Vue.extend({
  mounted(){
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
    deleteUser() {
      axios.delete(`/user/${this.form.user}`).then(res => {
        const ret = res.data;
        if (ret.ok)
          this.$bvModal.msgBoxOk("User is deleted!").catch(err => alert(err));
        else
          this.$bvModal
            .msgBoxOk("Delete user failed!")
            .catch(err => alert(err));

        this.form.user = "";
        this.getUserList();
      });
    },
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