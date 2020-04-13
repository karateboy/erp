<template>
	<div class="common-list">
		<table>
			<thead>
				<tr>
					<th>Action</th>
					<th v-for="f in list.fields">{{ f }}</th>
				</tr>
				<tr>
					<th>X</th>
					<th><input type="text" v-model="search" onkeyup="" placeholder="Search title.."/></th>
					<th><input type="text" v-model="search" placeholder="Search title.."/></th>
					<th><input type="text" v-model="search" placeholder="Search title.."/></th>
					<th></th>
					<th><input type="text" v-model="search" placeholder="Search title.."/></th>
					<th></th>
					<th></th>
					<th><input type="text" v-model="search" placeholder="Search title.."/></th>
				</tr>
			</thead>
			<tbody>
				<tr v-for="(p, index) in filteredList">
					<td>{{ index }} </td>
					<td><a v-bind:href="'/product/'+p.ptn">{{ p.product_code }}-{{ p.product_number }} {{ p.product_spec }}</a></td>
					<td>{{ p.ext_ref }}</td>
					<td>{{ p.product_name }}</td>
					<td>{{ p.colors }}</td>
					<td>{{ p.paper_code }}</td>
					<td>{{ p.cell }}</td>
					<td>{{ p.stock }}</td>
					<td>{{ p.print_type }}</td>
				</tr>		
			</tbody>
			<tfoot>
				<tr>
					<th v-bind:colspan="list.fields.length+1">Welcome</th>
				</tr>
			</tfoot>
		</table>
	</div>
</template>



<!--
	***

	VUE scripts

	***
-->
<script>  
	export default {
		data () {
			return {
				projects:[],
				search:"",
				list: {
					fields: ["รหัสสินค้า","2","รายการสินค้า","4", "ชนิดกระดาษ", "สินค้าคงเหลือ", "ตระกร้า", "รูปแบบการพิมพ์"],
				}
			}
		},
		computed: {
			filteredList() {
				return this.projects.filter(project => {
					return project.product_code.toLowerCase().includes(this.search.toLowerCase());
				});
			},
			filteredList2(column, search) {
				return this.projects.filter(project => {
					return project.column.toLowerCase().includes(search.toLowerCase());
				});
			}

		},
		created() {
			const url = 'http://localhost:5000/product';
			// const url = 'http://localhost:8080/json/5IN1.json';
			// const url = 'https://jsonplaceholder.typicode.com/posts';
			this.$http.get(url).then(data => {
				this.projects = data.body,
				console.log(data.body[1])
			})
		}
	}
</script>



<!--
	***

	BELOW IS STYLING OF WEBPAGE SCOPED
	
	***
!-->
<style scoped>
	td, th{
		border: 1px solid black;
		margin:0px;
	}
	td{
		text-align: left;
	}
	table{
		margin:10px;
		padding:0px;
		width:100%;
	}
	thead th{
		background:#000;
		color:#fff;
		font-style:bold;
	}

	tbody tr:hover td{
		background: #ff0;
	}

	tbody tr:nth-child(even){
		background:#eee;
	}
	tbody tr:nth-child(odd){
		background:#fff;
	}
</style>
