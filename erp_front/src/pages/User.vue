<template>
  <b-form @submit="newUser" @reset="reset">
    <b-form-group label-cols="4" label-cols-lg="2" label-size="lg" label="ID" label-for="ID">
      <b-form-input id="ID" size="lg" v-model="form._id" />
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

    <b-button type="submit" variant="primary">New User</b-button> &nbsp;
    <b-button type="reset" variant="danger">Reset</b-button>
  </b-form>
</template>
<script lang="ts">
import Vue from "vue";
export default Vue.extend({
  props:["isNew"],
  data() {
    return {
      form: {
        _id: "",
        password: "",
        password2: "",
        group: ""
      },
      groupList: ["admin", "RD", "Factory", "QC"]
    };
  },
  methods: {
    newUser(evt:Event) {
        evt.preventDefault();
    },
    reset(evt:Event){
        evt.preventDefault();
    }
  }
});
</script>