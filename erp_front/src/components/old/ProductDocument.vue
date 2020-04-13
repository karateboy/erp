<template>
	<div class="common-list">
		<div>{{ MyAppUrl.mongodb }}</div>
		<table>
			<thead>
			</thead>
			<tbody>
				<tr v-for="(p, index) in jsonData">
					<td>{{ index }} </td>
					<td><a v-bind:href="'/product/'+p.ptn">{{ p.product_code }}-{{ p.product_number }} {{ p.product_spec }}</a></td>
					<td>{{ p }}</td>
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
	import axios from 'axios';
	export default {
		props:{
		 MyAppUrl:{
				type: Object
			},
		 restapi:{
				type: String
			}
		},
		data () {
			return {
				id: this.$route.params.id,
				jsonData:[],
				search:"",
				list: {
					fields: ["รหัสสินค้า","2","รายการสินค้า","4", "ชนิดกระดาษ", "สินค้าคงเหลือ", "ตระกร้า", "รูปแบบการพิมพ์"],
				}
			}
		},
		computed: {

		},
		created() {
			console.log(url);
			const url = this.restapi+this.id;//'http://localhost:5000/product';
			// const url = 'http://localhost:5000/product/'+this.id;
			console.log(this.id);
			// const url = 'http://localhost:8080/json/5IN1.json';
			// const url = 'https://jsonplaceholder.typicode.com/posts';
			this.$http.get(url).then(data => {
				this.jsonData = data.body,
				console.log(url),
				console.log(url,data.body)
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
