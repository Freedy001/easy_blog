<template>
	<teleport to="body">
		<div class="body">
			<div class="background">
				<div class="ground"></div>
			</div>
			<div class="container">
				<div class="left-section">
					<div class="inner-content">
						<h1 class="heading">404</h1>
						<p class="subheading">很抱歉，您访问的页面找不到了</p>
						<p class="index-btn"><router-link to="/" tag="a">返回首页</router-link></p>
					</div>
				</div>
				<div class="right-section">
					<svg class="svgimg" xmlns="http://www.w3.org/2000/svg" viewBox="51.5 -15.288 385 505.565">
						<g class="bench-legs">
							<path d="M202.778,391.666h11.111v98.611h-11.111V391.666z M370.833,390.277h11.111v100h-11.111V390.277z M183.333,456.944h11.111
	  v33.333h-11.111V456.944z M393.056,456.944h11.111v33.333h-11.111V456.944z" />
						</g>
						<g class="top-bench">
							<path d="M396.527,397.917c0,1.534-1.243,2.777-2.777,2.777H190.972c-1.534,0-2.778-1.243-2.778-2.777v-8.333
	  c0-1.535,1.244-2.778,2.778-2.778H393.75c1.534,0,2.777,1.243,2.777,2.778V397.917z M400.694,414.583
	  c0,1.534-1.243,2.778-2.777,2.778H188.194c-1.534,0-2.778-1.244-2.778-2.778v-8.333c0-1.534,1.244-2.777,2.778-2.777h209.723
	  c1.534,0,2.777,1.243,2.777,2.777V414.583z M403.473,431.25c0,1.534-1.244,2.777-2.778,2.777H184.028
	  c-1.534,0-2.778-1.243-2.778-2.777v-8.333c0-1.534,1.244-2.778,2.778-2.778h216.667c1.534,0,2.778,1.244,2.778,2.778V431.25z" />
						</g>
						<g class="bottom-bench">
							<path d="M417.361,459.027c0,0.769-1.244,1.39-2.778,1.39H170.139c-1.533,0-2.777-0.621-2.777-1.39v-4.86
	  c0-0.769,1.244-0.694,2.777-0.694h244.444c1.534,0,2.778-0.074,2.778,0.694V459.027z" />
							<path d="M185.417,443.75H400c0,0,18.143,9.721,17.361,10.417l-250-0.696C167.303,451.65,185.417,443.75,185.417,443.75z" />
						</g>
						<g id="lamp">
							<path class="lamp-details" d="M125.694,421.997c0,1.257-0.73,3.697-1.633,3.697H113.44c-0.903,0-1.633-2.44-1.633-3.697V84.917
	  c0-1.257,0.73-2.278,1.633-2.278h10.621c0.903,0,1.633,1.02,1.633,2.278V421.997z" />
							<path class="lamp-accent" d="M128.472,93.75c0,1.534-1.244,2.778-2.778,2.778h-13.889c-1.534,0-2.778-1.244-2.778-2.778V79.861
	  c0-1.534,1.244-2.778,2.778-2.778h13.889c1.534,0,2.778,1.244,2.778,2.778V93.75z" />

							<circle class="lamp-light" cx="119.676" cy="44.22" r="40.51" />
							<path class="lamp-details" d="M149.306,71.528c0,3.242-13.37,13.889-29.861,13.889S89.583,75.232,89.583,71.528c0-4.166,13.369-13.889,29.861-13.889
	  S149.306,67.362,149.306,71.528z" />
							<radialGradient class="light-gradient" id="SVGID_1_" cx="119.676" cy="44.22" r="65" gradientUnits="userSpaceOnUse">
								<stop offset="0%" style="stop-color:#FFFFFF; stop-opacity: 1" />
								<stop offset="50%" style="stop-color:#EDEDED; stop-opacity: 0.5">
									<animate attributeName="stop-opacity" values="0.0; 0.5; 0.0" dur="5000ms" repeatCount="indefinite"></animate>
								</stop>
								<stop offset="100%" style="stop-color:#EDEDED; stop-opacity: 0" />
							</radialGradient>
							<circle class="lamp-light__glow" fill="url(#SVGID_1_)" cx="119.676" cy="44.22" r="65" />
							<path class="lamp-bottom" d="M135.417,487.781c0,1.378-1.244,2.496-2.778,2.496H106.25c-1.534,0-2.778-1.118-2.778-2.496v-74.869
	  c0-1.378,1.244-2.495,2.778-2.495h26.389c1.534,0,2.778,1.117,2.778,2.495V487.781z" />
						</g>
					</svg>
				</div>
			</div>
		</div>
	</teleport>
</template>

<script setup lang="ts">

</script>

<style scoped lang="scss">
.left-section .inner-content {
	position: absolute;
	top: 50%;
	transform: translateY(-50%);
}

* {
	box-sizing: border-box;
}


.body {
	position: absolute;
	top: 0;
	z-index: 99999;
	margin: 0;
	padding: 0;
	width: 100%;
}

.body {
	font-family: 'Fira Sans', sans-serif;
	color: #f5f6fa;
}

.background {
	position: absolute;
	top: 0;
	left: 0;
	width: 100%;
	height: 100%;
	background: linear-gradient(#0c0e10, #446182);
}

.background .ground {
	position: absolute;
	bottom: 0;
	width: 100%;
	height: 25vh;
	background: #0c0e10;
}

@media (max-width: 770px) {
	.background .ground {
		height: 0vh;
	}
}

.container {
	position: relative;
	margin: 0 auto;
	width: 85%;
	height: 100vh;
	padding-bottom: 25vh;
	display: flex;
	flex-direction: row;
	justify-content: space-around;
}

@media (max-width: 770px) {
	.container {
		flex-direction: column;
		padding-bottom: 0vh;
	}
}

.left-section,
.right-section {
	position: relative;
}

.left-section {
	width: 40%;
}

@media (max-width: 770px) {
	.left-section {
		width: 100%;
		height: 40%;
		position: absolute;
		top: 0;
	}
}

@media (max-width: 770px) {
	.left-section .inner-content {
		position: relative;
		padding: 1rem 0;
	}
}

.heading {
	text-align: center;
	font-size: 9em;
	line-height: 1.3em;
	margin: 2rem 0 0.5rem 0;
	padding: 0;
	text-shadow: 0 0 1rem #fefefe;
}

@media (max-width: 770px) {
	.heading {
		font-size: 7em;
		line-height: 1.15;
		margin: 0;
	}
}

.subheading {
	text-align: center;
	max-width: 480px;
	font-size: 1.5em;
	line-height: 1.15em;
	padding: 0 1rem;
	margin: 0 auto;
}

.index-btn {
	margin-top: 50px;
	text-align: center;
}

.index-btn a {
	padding: 10px 20px;
	border: 1px solid #fff;
	border-radius: 4px;
	color: #fff;
	text-decoration: none;
}

.index-btn a:hover {
	background-color: #000;
}

@media (max-width: 770px) {
	.subheading {
		font-size: 1.3em;
		line-height: 1.15;
		max-width: 100%;
	}
}

.right-section {
	width: 50%;
}

@media (max-width: 770px) {
	.right-section {
		width: 100%;
		height: 60%;
		position: absolute;
		bottom: 0;
	}
}

.svgimg {
	position: absolute;
	bottom: 0;
	padding-top: 10vh;
	padding-left: 1vh;
	max-width: 100%;
	max-height: 100%;
}

@media (max-width: 770px) {
	.svgimg {
		padding: 0;
	}
}

.svgimg .bench-legs {
	fill: #0c0e10;
}

.svgimg .top-bench,
.svgimg .bottom-bench {
	stroke: #0c0e10;
	stroke-width: 1px;
	fill: #5b3e2b;
}

.svgimg .bottom-bench path:nth-child(1) {
	fill: #432d20;
}

.svgimg .lamp-details {
	fill: #202425;
}

.svgimg .lamp-accent {
	fill: #2c3133;
}

.svgimg .lamp-bottom {
	fill: linear-gradient(#202425, #0c0e10);
}

.svgimg .lamp-light {
	fill: #efefef;
}

@keyframes glow {
	0% {
		text-shadow: 0 0 1rem #fefefe;
	}

	50% {
		text-shadow: 0 0 1.85rem #ededed;
	}

	100% {
		text-shadow: 0 0 1rem #fefefe;
	}
}

//********************
@font-face {
	font-family: "Varela";
	font-style: normal;
	font-weight: 400;
	src: local("Varela"), url(https://fonts.gstatic.com/s/varela/v9/DPEtYwqExx0AWHX5DR4ETvw.woff2) format("woff2");
	unicode-range: U+0100-024F, U+0259, U+1E00-1EFF, U+2020, U+20A0-20AB, U+20AD-20CF, U+2113, U+2C60-2C7F, U+A720-A7FF;
}

/* latin */
@font-face {
	font-family: "Varela";
	font-style: normal;
	font-weight: 400;
	src: local("Varela"), url(https://fonts.gstatic.com/s/varela/v9/DPEtYwqExx0AWHX5Ax4E.woff2) format("woff2");
	unicode-range: U+0000-00FF, U+0131, U+0152-0153, U+02BB-02BC, U+02C6, U+02DA, U+02DC, U+2000-206F, U+2074, U+20AC, U+2122, U+2191, U+2193, U+2212, U+2215, U+FEFF, U+FFFD;
}


.glitch {
	color: rgb(57, 135, 224);
	font-size: 100px;
	position: relative;
	margin: auto;
}

@keyframes noise-anim {
	0% {
		clip: rect(73px, 9999px, 15px, 0);
	}

	5% {
		clip: rect(3px, 9999px, 100px, 0);
	}

	10% {
		clip: rect(93px, 9999px, 80px, 0);
	}

	15% {
		clip: rect(99px, 9999px, 19px, 0);
	}

	20% {
		clip: rect(41px, 9999px, 34px, 0);
	}

	25% {
		clip: rect(82px, 9999px, 54px, 0);
	}

	30% {
		clip: rect(8px, 9999px, 62px, 0);
	}

	35% {
		clip: rect(19px, 9999px, 26px, 0);
	}

	40% {
		clip: rect(82px, 9999px, 50px, 0);
	}

	45% {
		clip: rect(97px, 9999px, 40px, 0);
	}

	50% {
		clip: rect(31px, 9999px, 47px, 0);
	}

	55% {
		clip: rect(34px, 9999px, 34px, 0);
	}

	60% {
		clip: rect(33px, 9999px, 74px, 0);
	}

	65% {
		clip: rect(27px, 9999px, 92px, 0);
	}

	70% {
		clip: rect(59px, 9999px, 100px, 0);
	}

	75% {
		clip: rect(8px, 9999px, 93px, 0);
	}

	80% {
		clip: rect(93px, 9999px, 73px, 0);
	}

	85% {
		clip: rect(100px, 9999px, 13px, 0);
	}

	90% {
		clip: rect(20px, 9999px, 24px, 0);
	}

	95% {
		clip: rect(51px, 9999px, 100px, 0);
	}

	100% {
		clip: rect(57px, 9999px, 7px, 0);
	}
}

.glitch:after {
	content: attr(data-text);
	position: absolute;
	left: 2px;
	text-shadow: -1px 0 red;
	top: 0;
	color: rgb(173, 156, 156);
	background: #e5e5e5;
	overflow: hidden;
	clip: rect(0, 900px, 0, 0);
	animation: noise-anim 2s infinite linear alternate-reverse;

}

@keyframes noise-anim-2 {
	0% {
		clip: rect(99px, 9999px, 98px, 0);
	}

	5% {
		clip: rect(87px, 9999px, 41px, 0);
	}

	10% {
		clip: rect(33px, 9999px, 40px, 0);
	}

	15% {
		clip: rect(33px, 9999px, 43px, 0);
	}

	20% {
		clip: rect(89px, 9999px, 22px, 0);
	}

	25% {
		clip: rect(12px, 9999px, 73px, 0);
	}

	30% {
		clip: rect(47px, 9999px, 18px, 0);
	}

	35% {
		clip: rect(62px, 9999px, 98px, 0);
	}

	40% {
		clip: rect(30px, 9999px, 94px, 0);
	}

	45% {
		clip: rect(69px, 9999px, 65px, 0);
	}

	50% {
		clip: rect(87px, 9999px, 47px, 0);
	}

	55% {
		clip: rect(12px, 9999px, 3px, 0);
	}

	60% {
		clip: rect(63px, 9999px, 14px, 0);
	}

	65% {
		clip: rect(88px, 9999px, 97px, 0);
	}

	70% {
		clip: rect(15px, 9999px, 23px, 0);
	}

	75% {
		clip: rect(88px, 9999px, 20px, 0);
	}

	80% {
		clip: rect(68px, 9999px, 54px, 0);
	}

	85% {
		clip: rect(58px, 9999px, 66px, 0);
	}

	90% {
		clip: rect(98px, 9999px, 97px, 0);
	}

	95% {
		clip: rect(86px, 9999px, 16px, 0);
	}

	100% {
		clip: rect(79px, 9999px, 77px, 0);
	}
}

.glitch:before {
	content: attr(data-text);
	position: absolute;
	left: -2px;
	text-shadow: 1px 0 rgb(255, 252, 63);
	top: 0;
	color: rgb(227, 226, 227);
	background: #e89595;
	overflow: hidden;
	clip: rect(0, 900px, 0, 0);
	animation: noise-anim-2 3s infinite linear alternate-reverse;

}
</style>