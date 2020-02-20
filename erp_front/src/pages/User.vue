<template>
  <b-form @submit="newUser" @reset="reset">
    <b-form-group
      label-cols="4"
      label-cols-lg="2"
      label-size="lg"
      label="ID"
      :label-for="`${prefix}id`"
    >
      <b-form-input
        :id="`${prefix}id`"
        size="lg"
        v-model="form._id"
        :readonly="!isNew"
        :state="idState"
        :aria-describedby="`${prefix}id-feedback`"
      />
      <b-form-invalid-feedback :id="`${prefix}id-feedback`">ID cannot be empty.</b-form-invalid-feedback>
    </b-form-group>
    <b-form-group
      label-cols="4"
      label-cols-lg="2"
      label-size="lg"
      label="Name"
      :label-for="`${prefix}name`"
    >
      <b-form-input :id="`${prefix}name`" size="lg" v-model="form.name" />
    </b-form-group>
    <b-form-group
      label-cols="4"
      label-cols-lg="2"
      label-size="lg"
      label="password"
      :label-for="`${prefix}password`"
    >
      <b-form-input
        :id="`${prefix}password`"
        type="password"
        size="lg"
        placeholder="password"
        v-model="form.password"
        autocomplete="true"
      />
    </b-form-group>
    <b-form-group
      label-cols="4"
      label-cols-lg="2"
      label-size="lg"
      label="retype password"
      :label-for="`${prefix}password2`"
    >
      <b-form-input
        :id="`${prefix}password2`"
        type="password"
        size="lg"
        placeholder="password"
        :state="password2State"
        :aria-describedby="`${prefix}password2-feedback`"
        v-model="form.password2"
        autocomplete="true"
      />
      <b-form-invalid-feedback :id="`${prefix}password2-feedback`">retype password is not the same.</b-form-invalid-feedback>
    </b-form-group>

    <b-form-group
      label-cols="4"
      label-cols-lg="2"
      label-size="lg"
      label="Group"
      :label-for="`${prefix}group`"
    >
      <b-form-checkbox-group
        :id="`${prefix}group`"
        v-model="form.groups"
        :options="groupList"
        :state="groupState"
      >
        <b-form-invalid-feedback :state="groupState">Must select one group</b-form-invalid-feedback>
      </b-form-checkbox-group>
    </b-form-group>

    <b-button type="submit" variant="primary" :disabled="!canSubmit">{{btnTitle}}</b-button>&nbsp;
    <b-button type="reset" variant="danger">Reset</b-button>
  </b-form>
</template>
<script lang="ts">
import Vue from "vue";
import axios from "axios";

export default Vue.extend({
  props: ["isNew", "_id"],
  data() {
    return {
      form: {
        _id: "",
        name: "",
        password: "",
        password2: "",
        groups: Array<string>()
      },
      groupList: ["admin", "RD", "Factory", "QC"]
    };
  },
  mounted() {
    if (!this.isNew) this.getUserInfo(this._id);
  },
  computed: {
    prefix() {
      if (this.isNew) return "new-";
      else return "update-";
    },
    idState() {
      if (!this.form._id) return false;
      else return true;
    },
    password2State() {
      if (this.form.password === this.form.password2) return true;
      else return false;
    },
    groupState() {
      if (this.form.groups.length === 0) return false;
      else return true;
    },
    canSubmit() {
      if (this.idState && this.password2State && this.groupState) return true;
      else return false;
    },
    btnTitle() {
      if (this.isNew) return "New User";
      else return "Update";
    }
  },
  methods: {
    newUser(evt: Event) {
      evt.preventDefault();
      axios.post("/user", this.form).then(res => {
        const ret = res.data;
        if (ret.ok) this.$bvModal.msgBoxOk("Success!").catch(err => alert(err));
        else this.$bvModal.msgBoxOk("Failed!").catch(err => alert(err));
      });
    },
    reset(evt: Event) {
      evt.preventDefault();
    },
    getUserInfo(id: string) {
      axios.get(`/user/${id}`).then(res => {
        const ret = res.data;
        this.form._id = ret._id;
        this.form.name = ret.name;
        this.form.password = ret.password;
        this.form.password2 = ret.password2;
        this.form.groups.splice(0, this.form.groups.length);
        for (let group of ret.groups) {
          this.form.groups.push(group);
        }
      });
    }
  }
});
</script>