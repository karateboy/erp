<template>
	<div class="common-list">
		<table>
			<thead>
				<tr>
					<th>Action</th>
					<th v-for="f in fields">{{ f }}</th>
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
				<tr v-for="(row, index) in filteredList">
					<td>{{ index }} </td>
					<td>{{ row.entry_datetime }} </td>
					<td><a v-bind:href="'/work/'+row.jwn">{{ row.jwn }}</a></td>
					<td><a v-bind:href="'/product/'+row.ptn">{{ row.pcns }}</a>{{ row.ext_ref }}</td>
					<td>{{ row.print_type }}</td>
					<td>{{ row.order_number }}</td>
					<td>{{ row.order_qty }}</td>
					<td>{{ row.work_qty }}</td>
					<td>{{ row.order_due }}</td>
					<td>{{ row.work_progress }}</td>
				</tr>		
			</tbody>
			<tfoot>
				<tr>
					<th v-bind:colspan="fields.length+1">Welcome</th>
				</tr>
			</tfoot>
		</table>
	</div>
</template>

<script>
	export default {
	   	data(){
			return{
				search:"",
				fields: ["Date","Work#","Product","Print Type", "Order#", "Qty", "Work Qty", "Due Date", "Status"],
				apiData:[]
			}
		},
		computed: {
			filteredList() {
				return this.apiData.filter((project) => {
					return project.product_code.toLowerCase().includes(this.search.toLowerCase());
				});
			},
		},
		created() {
			const url = 'http://localhost:5000/work';
			this.$http.get(url).then(data => {
				this.apiData = data.body,
				console.log(data.body[1])
			})
		}
	}
</script>


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
