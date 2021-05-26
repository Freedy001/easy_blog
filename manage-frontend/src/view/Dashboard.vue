<template>
	<div class="root">
		<div class="card-row first">
			<el-card class="box-card" shadow="hover">
				<div class="title">
					<h1>文章</h1>
					<img :src="plus" alt="" @click="$router.push('/index/article')">
				</div>
				<span>{{ analyzeData.articleNum }}</span>
			</el-card>
			<el-card class="box-card" shadow="hover">
				<div class="title">
				<h1>评论</h1>
					<img :src="answerLogo" alt="" @click="$router.push('/index/comment')">
				</div>
				<span>{{ analyzeData.commentNum }}</span>
			</el-card>
			<el-card class="box-card" shadow="hover">
				<h1>阅读量</h1>
				<span>{{ analyzeData.visitNum }}</span>
			</el-card>
			<el-card class="box-card" shadow="hover">
				<h1>建立天数</h1>
				<span>{{ analyzeData.establishmentTime }}</span>
			</el-card>
		</div>
		<div class="card-row second">
			<el-card class="box-card textarea" shadow="hover">
				<h1>速记</h1>
				<el-input
						maxlength="300"
						type="textarea"
						:rows="10"
						:show-word-limit="true"
						v-model="shotHand"
						placeholder="随便记一些什么吧！">
				</el-input>
				<el-button type="primary" @click="publish" :loading="isLoading" round>发布</el-button>
			</el-card>
			<el-card class="box-card" shadow="hover">
				<h1>操作记录</h1>
				<div class="operation-log">
					<transition-group name="el-fade-in-linear">
						<div class="operation-item" v-for="log in operationLog" :key="log.id">
							<div class="operation-name">
								<div class="info"><span>{{ log.operationName }}</span></div>
								<span class="time">{{ log.creatTime }}</span>
							</div>
							<div class="operator">
								<div class="info">
									<div class="dot" :style="{'background-color':log.operationStatus=='成功'?'#4fc719':'#ff3232'}"></div>
									<span :style="{'color':log.operationStatus==='成功'?'#4fc719':'#ff3232'}">{{ log.operationStatus }}</span>
								</div>
								<span>{{ log.operator }}</span>
							</div>
						</div>
					</transition-group>
				</div>
			</el-card>
		</div>
		<div id="articleDistribute" class="chart-item" style="height: 700px"></div>
		<div id="articlePopular" class="chart-item"></div>
		<div id="visitor" class="chart-item"></div>
	</div>
</template>

<script setup lang="ts">
import * as echarts from 'echarts'
import answerLogo from '../assets/answer.svg'
import plus from '../assets/plus.svg'
import {getCurrentInstance, onMounted, reactive, ref} from "vue";
import {get, post} from "../http";
import {ElMessage} from "element-plus";
import {onBeforeRouteLeave} from "vue-router";
const {proxy}:any = getCurrentInstance();
let shotHand = ref('')
let isLoading = ref(false)
async function publish() {
	if (shotHand.value==''){
		proxy.$notify({
			title: '额',
			message: '你好像什么都没填😥!',
			type: 'error'
		});
		return;
	}
	isLoading.value=true;
	const response =await post('/shorthand/publish',{
		content:shotHand.value
	});
	if (response.code==200){
		setTimeout(()=>{
			proxy.$notify({
				title: '成功',
				message: '发布成功!',
				type: 'success'
			});
			isLoading.value=false;
			shotHand.value=''
		},500)
	}

}


let operationLog =ref([]);

//获取操作日志
async function getOperation() {
	const response = await get('/operationLog/getOperationLog?page=1&limit=20&sidx=creat_time&order=desc');
	if (response.code == 200) {
		operationLog.value=response.data.list;
	}
}

let analyzeData = reactive<any>({})

async function getAnalyzeData() {
	const response = await get('/sys/getDashboardData');
	if (response.code == 200) {
		const data = response.data;
		Object.keys(data).forEach(key => analyzeData[key] = data[key])
	}
}

let interval: number | undefined;
onMounted(() => {
	getAnalyzeData().then(() => {
		articleDistribute();
		articlePopular();
		visitor();
	});
	getOperation().then();
	interval=setInterval(()=>{
		getOperation().then();
	},5000)
})
onBeforeRouteLeave(()=>{
	clearInterval(interval)
})


let flag=true

function articleDistribute() {
	let chartDom: any = document.getElementById('articleDistribute');
	let myChart = echarts.init(chartDom);
	let data = analyzeData.articleDistribution
	let option = {
		title: {
			text: "文章分布",
			textStyle: {
				fontSize: 30,
				color: '#4b4b4b'
			}
		},
		borderRadius: "20px",
		backgroundColor: "#ffffff",
		color: ['#FFAE57', '#FF7853', '#EA5151', '#CC3F57', '#9A2555'],
		series: {
			type: 'sunburst',
			emphasis: {
				focus: 'ancestor'
			},
			data: data,
			radius: [0, '90%'],
			levels: [{}, {
				r0: 30,
				r: 130,
			},  {
				r0: 160,
				r: 200,
				itemStyle: {
					shadowBlur: 80,
				},
				label: {
					position: 'outside',
					textShadowBlur: 5,

				},
			}],
			itemStyle: {
				shadowBlur: 20,

				borderWidth: 2
			}
		}
	};
	myChart.on('click', function (params) {
		if (params.treePathInfo.length==3&&flag){
			ElMessage({
				dangerouslyUseHTMLString: true,
				showClose:true,
				duration:2000,
				message: `
<div>
	<strong>去 ${params.treePathInfo[2].name} 看看？</strong>
	<a href="/#/article?articleName=${params.treePathInfo[2].name}" style="color: #3a9ff5;text-decoration: none">go</a>
</div>`
			});
			console.log(params.treePathInfo[2])
		}
	});
	myChart.setOption(option);
}

function articlePopular() {
	let chartDom:any = document.getElementById('articlePopular');
	let myChart = echarts.init(chartDom);
	let xData: any[]=[];
	let like: any[]=[];
	let comment: any[]=[];
	let visit: any[]=[];
	analyzeData.articlePopular.forEach((value: any[])=>{
		xData.push(value[0])
		like.push(value[1])
		comment.push(value[2])
		visit.push(value[3])
	})

	let option = {
		title: {
			text: "欢迎度",
			textStyle: {
				fontSize: 30,
				color: '#4b4b4b'
			}
		},
		tooltip: {
			trigger: 'axis'
		},
		legend: {
			data: ['点赞数', '评论数', '访问量']
		},
		dataZoom: [
			{
				type: 'inside'
			}
		],
		toolbox: {
			show: true,
			feature: {
				dataView: {show: true, readOnly: false},
				magicType: {show: true, type: ['line', 'bar']},
				restore: {show: true},
				saveAsImage: {show: true}
			}
		},
		calculable: true,
		xAxis: [
			{
				type: 'category',
				data: xData
			}
		],
		yAxis: [
			{
				type: 'value'
			}
		],
		series: [
			{
				name: '点赞数',
				type: 'bar',
				data: like,
				markPoint: {
					data: [
						{type: 'max', name: '最大值'},
						{type: 'min', name: '最小值'}
					]
				},
				markLine: {
					data: [
						{type: 'average', name: '平均值'}
					]
				}
			},
			{
				name: '评论数',
				type: 'bar',
				data: comment,
				markPoint: {
					data: [
						{type: 'max', name: '最大值'},
						{type: 'min', name: '最小值'}
					]
				},
				markLine: {
					data: [
						{type: 'average', name: '平均值'}
					]
				}
			},
			{
				name: '访问量',
				type: 'bar',
				data: visit,
				markPoint: {
					data: [
						{type: 'max', name: '最大值'},
						{type: 'min', name: '最小值'}
					]
				},
				markLine: {
					data: [
						{type: 'average', name: '平均值'}
					]
				}
			}
		]
	};

	option && myChart.setOption(option);
}

function visitor() {
	let chartDom: any = document.getElementById('visitor');
	let myChart = echarts.init(chartDom);
	let option;
	let week = ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun']
	let axisData = [];
	let day = new Date().getDay();
	for (let i = 6; i >= 0; i--) {
		axisData[i] = week[(day + i) % 7]
	}
	option = {
		title: {
			text: "过去七天的访客",
			textStyle: {
				fontSize: 30,
				color: '#4b4b4b'
			}
		},
		tooltip: {
			trigger: 'axis',
			formatter: '访客数:{c0}个'
		},
		toolbox: {
			show: true,
			feature: {
				dataView: {show: true, readOnly: false},
				magicType: {show: true, type: ['line', 'bar']},
				restore: {show: true},
				saveAsImage: {show: true}
			}
		},
		xAxis: {
			type: 'category',
			data: axisData
		},
		yAxis: {
			type: 'value'
		},
		series: [{
			data: analyzeData.visitorNum.reverse(),
			type: 'line',
			smooth: true
		}]
	};

	option && myChart.setOption(option);

}

</script>

<style scoped lang="scss">
.card-row {
	margin: 20px;
	display: flex;
	justify-content: space-around;
	flex-wrap: wrap;
}

.card-row.first {
	.box-card {
		width: 22%;
		h1 {
			font-weight: lighter;
			font-size: 20px;
			margin-bottom: 20px;
		}
		.title{
			display: flex;
			justify-content: space-between;

			img {
				width: 20px;
				height: 20px;
				cursor: pointer;
			}
		}
		span {
			font-weight: bold;
			font-size: 25px;
		}
	}
}

.card-row.second {
	.box-card {
		overflow: auto;
		width: 48%;
		max-height: 500px;
	}

	.textarea {
		height: 415px;
		resize:none;
		:deep(.el-textarea){
			margin-top: 15px;
		}
		:deep(.el-textarea__inner){
			resize: none;
		}

		div {
			font-size: 18px;
		}

		.el-button {
			height: 30px;
			margin: 10px;
			float: right;
		}
	}

	.operation-log {
		margin: 5px;

		.operation-item {
			width: 100%;
			height: 50px;
			margin: 15px 0;
			display: flex;
			justify-content: space-between;
			border-bottom: 1px solid rgba(212, 212, 212, 0.5);
			align-items: center;

			.operation-name {
				display: flex;
				flex-direction: column;
				height: 100%;

				.info {
					font-size: 15px;
					position: relative;
				}

				.time {
					margin-top: 5px;
					font-size: 13px;
					font-weight: 300;
					color: #84847f;
				}
			}

			.operator {
				font-weight: 300;
				font-size: 15px;
				color: grey;

				.info {
					display: flex;
					align-items: center;

					.dot {
						top: -10px;
						right: 0;
						width: 6px;
						height: 6px;
						border-radius: 100%;
						flex-shrink: 0;
						flex-grow: 0;
						display: inline-block;
					}

					span {
						margin-left: 5px;
						font-size: 13px;
						font-weight: bold;
					}
				}
			}
		}
	}
}

.chart-item {
	margin-top: 50px;
	width: 100%;
	height: 500px;
}
</style>