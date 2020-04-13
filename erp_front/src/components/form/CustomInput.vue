<template>
  <b-form-group
    :id="`field-${label}`"
    label-cols-sm="4"
    label-cols-lg="3"
    :description="label"
    :label="label"
    :label-for="label"
  >
    <b-form-input
      :id="`input-${label}`"
      :type="type"
      v-on="$listeners"
      :value="value"
      @input="$emit('update', $event.target.value)"
    ></b-form-input>
  </b-form-group>
</template>

<script>
const TYPES = [
  "text",
  "password",
  "email",
  "number",
  "url",
  "tel",
  "search",
  "color"
];
const includes = types => type => types.includes(type);

export default {
  inheritAttrs: false,
  props: {
    label: {
      type: String,
      default: ""
    },
    value: {
      type: [String, Number],
      default: ""
    },
    type: {
      type: String,
      default: "text",
      validator(value) {
        const isValid = includes(TYPES)(value);
        if (!isValid) {
          // eslint-disable-next-line
          console.warn(`allowed types are ${TYPES}`);
        }
        return isValid;
      }
    }
  },
  model: {
    prop: "value",
    event: "update"
  }
};
</script>
