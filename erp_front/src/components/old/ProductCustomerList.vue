<template>
	<div>
		{{ $route.path }}
    <div>
      Sorting By: <b>{{ sortBy }}</b>, Sort Direction:
      <b>{{ sortDesc ? 'Descending' : 'Ascending' }}</b>
    </div>

		<b-table striped hover 
			v-bind:fields="fields" 
			v-bind:items="filteredList"
			v-bind:sort-by.sync="sortBy"
	    v-bind:sort-desc.sync="sortDesc"
			v-bind:striped = true
			v-bind:borderless = true
			v-bind:small = true
			v-bind:hover = true
			v-bind:dark = true
			v-bind:footClone = true

		>
			<!-- A virtual column -->
			<template v-slot:cell(index)="filteredList">
				{{ filteredList.index+1 }}
			</template>
		</b-table>
	</div>
</template>



<!--
	***

	VUE scripts

	***
-->
<script>  
	import axios from 'axios'
	export default {
		data () {
			return {
				path: this.$route.path,
	      sortBy: 'product_number',
        sortDesc: false,
				id: this.$route.params.id,
				apiData:[],
				search:"",
				fields:[
					{key: "index", label:"INDEXXXXX"},
					{key:"name", label:"XXXXXXXXXXXXXXXX"},
					{key: "product_code", label:"รหัสสินค้า"},
					{key: "product_number", label:"Number", sortable: true, variant: "danger", isRowHeader: true},
					{key: "ext_ref"},
					{key: "product_name"},
					{key: "colors"},
					{key: "paper_code"},
					{key: "stock"},
					{key: "cell"},
					{key: "print_type"}
				],
				fieldsThai: ["รหัสสินค้า","2","รายการสินค้า","4", "ชนิดกระดาษ", "สินค้าคงเหลือ", "ตระกร้า", "รูปแบบการพิมพ์"],
			}
		},
		computed: {
			filteredList() {
				return this.apiData.filter((project) => {
					return project.product_code.toLowerCase().includes(this.search.toLowerCase());
				});
			},
		},
		created(){
		    axios
		      .get(this.path)
		      .then(response => {
		        this.apiData = response.data;
		        console.log(response.data[1])
		      })
		      .catch(error => {
		        console.log(response.data)
		        console.log(error)
		        this.errored = true
		      })
		      .finally(() => this.loading = false)
		},
	}
</script>



<!--
	***

	BELOW IS STYLING OF WEBPAGE SCOPED
	
	***
	!-->
	<style scoped>

</style>
