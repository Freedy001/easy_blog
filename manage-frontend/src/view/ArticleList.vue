<template>
	<el-table
			class="article-table"
			:data="tableDate"
			style="width: 100%">
		<el-table-column
				label="标题"
				min-width="150px">
			<template #default="scope">
				<i class="el-icon-time"></i>
				<span style="margin-left: 10px">{{ scope.row.name }}</span>
			</template>
		</el-table-column>
		<el-table-column
				label="状态">
			<template #default="scope">
				<i class="el-icon-time"></i>
				<span style="margin-left: 10px">{{ scope.row.date }}</span>
			</template>
		</el-table-column>
		<el-table-column label="分类">
			<template #default="scope">
				<el-tag>标签一</el-tag>
			</template>
		</el-table-column>
		<el-table-column
				label="标签"
				min-width="180px">
			<template #default="scope">
				<el-tag type="success">标签二</el-tag>
				<el-tag type="success">标签二</el-tag>
				<el-tag type="success">标签二</el-tag>
				<el-tag type="success">标签二</el-tag>
			</template>
		</el-table-column>
		<el-table-column label="评论" width="60px">
			<template #default="scope">
				<div style="background-color: #ff6e6e;width: 10px;height: 10px;border-radius: 50%"></div>
			</template>
		</el-table-column>
		<el-table-column label="访问"  width="60px">
			<template #default="scope">
				<div style="background-color: #0df9fc;width: 10px;height: 10px;border-radius: 50%"></div>
			</template>
		</el-table-column>
		<el-table-column
				min-width="150px"
				label="发布时间">
			<template #default="scope">
				2021-04-30 12:03
			</template>
		</el-table-column>
		<el-table-column
				label="操作"
				min-width="250px"
		>
			<template #default="scope">
				<el-button type="primary" size="mini" round>编辑</el-button>
				<el-button type="success" size="mini" round>设置</el-button>
				<el-button type="danger"  size="mini" round>删除</el-button>
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

function handleEdit(index, row) {
	console.log(index, row);
}

function handleDelete(index, row) {
	console.log(index, row);
}
</script>

<style scoped lang="scss">

</style>
