<template>
  <div>
    <Row :gutter="20">
      <i-col :xs="12" :md="8" :lg="4" key="selector" style="height: 120px;padding-bottom: 10px;">
        <infor-card shadow :color="summary.color" :icon="summary.icon" :icon-size="36">
          <Card :bordered="false" :dis-hover="true" :title="summary.title" :key="summary._id">
            <p>Under construction...</p>
          </Card>
        </infor-card>
      </i-col>
    </Row>
  </div>
</template>

<script>
import InforCard from '_c/info-card';

export default {
  name: 'home',
  components: {
    InforCard
  },
  data() {
    return {
      summary: {
        _id: 'erp',
        icon: 'ios-speedometer',
        color: '#ff0000',
        title: 'ERP Summary'
      },
      timer: undefined
    };
  },
  mounted() {
    this.reloadData();
  },
  methods: {
    reloadData() {
      this.timer = setTimeout(this.reloadData, 30000);
    }
  },
  destroyed() {
    clearTimeout(this.timer);
  }
};
</script>

<style lang="less">
.count-style {
  font-size: 50px;
}
</style>
