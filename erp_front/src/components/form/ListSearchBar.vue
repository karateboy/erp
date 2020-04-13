<template>
  <div>
    <b-form-group
      label="Filter"
      label-cols="2"
      label-align="right"
      label-size="lg"
      label-for="filterInput"
      class="mb-0"
    >
      <b-input-group size="lg">
        <b-form-input v-model="filter" type="search" id="filterInput" placeholder="Type to Search"></b-form-input>
        <b-input-group-append>
          <b-button :disabled="!filter" @click="filter = ''">Clear</b-button>
        </b-input-group-append>
      </b-input-group>
    </b-form-group>
  </div>
</template>

<script>
export default {
    data(){
        return{
            search: {},
        }
    }
};
</script>

<style lang="scss" scoped>
</style>