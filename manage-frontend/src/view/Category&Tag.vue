<!--suppress ALL -->
<template>
<div class="index-container">
	<CategoryOrTagCard name="分类" :hasDel="true"></CategoryOrTagCard>
	<CategoryOrTagCard name="标签" :hasDel="true"></CategoryOrTagCard>
</div>
</template>

<script setup lang="ts">
import {defineComponent, onMounted, reactive, ref} from "vue";
import CategoryOrTagCard from '../components/CategoryOrTagCard.vue'
defineComponent({
	CategoryOrTagCard
})


</script>

<style scoped lang="scss">
.index-container{
	height: 100%;
	display: flex;
	flex-direction: column;
	justify-content: space-around;
}
</style>