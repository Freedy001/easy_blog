<template>
	<el-table
			class="article-table"
			:data="tableDate"
			@selection-change="handleSelectionChange"
			style="width: 100%">
		<el-table-column
				type="selection"
				width="55">
		</el-table-column>
		<el-table-column
				prop="name"
				label="昵称"
				width="150px"
		>
		</el-table-column>
		<el-table-column
				label="类容"
				prop="address"
				width="350px"
		>
		</el-table-column>
		<el-table-column label="评论文章">
			<template #default="scope">
				<a href="#" style="color: #0b9aff;text-decoration: none">你知道吗？</a>
			</template>
		</el-table-column>
		<el-table-column
				label="发表日期"
				prop="date"
		>
		</el-table-column>
		<el-table-column
				label="操作"
		>
			<template #default="scope">
				<i class="el-icon-light-rain"></i>
				<i class="el-icon-lightning"></i>
				<i class="el-icon-heavy-rain"></i>
			</template>
		</el-table-column>
	</el-table>
</template>

<script setup lang="ts">
import {ref, reactive} from "vue";

let tableDate = reactive([{
	date: '2016',
	name: '王小虎asdasdasdasdasdasfgsdfsdfsdfsd',
	address: '上海市普陀区金沙江路 1518 弄'
}, {
	date: '2016',
	name: '王小虎',
	address: '上海市普陀区金沙江路 1517 弄'
}, {
	date: '2016',
	name: '王小虎',
	address: '上海市普陀区金沙江路 1519 弄'
}, {
	date: '2016',
	name: '王小虎',
	address: '上海市普陀区金沙江路 1516 弄'
}, {
	date: '2016',
	name: '王小虎',
	address: '上海市普陀区金沙江路 1519 弄'
}, {
	date: '2016',
	name: '王小虎',
	address: '上海市普陀区金沙江路 1519 弄'
}, {
	date: '2016',
	name: '王小虎',
	address: '上海市普陀区金沙江路 1519 弄'
}, {
	date: '2016',
	name: '王小虎',
	address: '上海市普陀区金沙江路 1519 弄'
}, {
	date: '2016',
	name: '王小虎',
	address: '上海市普陀区金沙江路 1519 弄'
},
	{
		date: '2016',
		name: '王小虎',
		address: '上海市普陀区金沙江路 1519 弄'
	}, {
		date: '2016',
		name: '王小虎',
		address: '上海市普陀区金沙江路 1519 弄'
	}, {
		date: '2016',
		name: '王小虎',
		address: '上海市普陀区金沙江路 1519 弄'
	},
])
let multipleSelection = reactive([])


function handleEdit(index, row) {
	console.log(index, row);
}

function handleDelete(index, row) {
	console.log(index, row);
}

function handleSelectionChange() {

}
</script>

<style scoped lang="scss">

</style>
