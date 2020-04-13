<template>
  <div>
    <b-form-group
      :id="`${name}-field`"
      label-cols-sm="4"
      label-cols-lg="3"
      :description="name"
      :label="name"
      :label-for="name"
    >
      <b-form-input :id="`${name}-input`" v-model="appDocument[name]" trim></b-form-input>
    </b-form-group>
  </div>
</template>

<script>
export default {
  props: {
      value,
      name,
      index
  },
  data() {
    return {};
  }
};
</script>

<style lang="scss" scoped>
</style>