<template>
  <div class="back-top show" :class="{'bounce-out-top':click}" v-if="show" @click="backTop">
    <svg class="icon" aria-hidden="true">
      <use xlink:href="#icon-huidaodingbu_huaban"></use>
    </svg>
  </div>
</template>

<script setup lang="ts">
import {defineEmit, getCurrentInstance, onMounted, ref} from "vue";

defineEmit(['scroll'])
const {proxy}: any = getCurrentInstance();
let show = ref(false)
let click = ref(false)
let pre = new Date().getTime();
onMounted(() => {
    setTimeout(() => {
        document.body.onscroll = ({srcElement}: any) => {
            if (new Date().getTime() - pre > 5) {
                proxy.$emit('scroll', srcElement)
                const scroll: HTMLElement = srcElement.scrollingElement
                show.value = scroll.scrollTop > scroll.clientHeight;
            }
        }
    }, 500)
})

function backTop() {
    click.value = true
    window.scrollTo({
        top: 0,
        behavior: "smooth"
    });
    setTimeout(() => {
        show.value = false
        click.value = false
    }, 500)
}


</script>

<style lang="scss" scoped>
.back-top {
  position: fixed;
  bottom: 50px;
  right: 50px;
  cursor: pointer;
  opacity: 0;
  visibility: hidden;
  z-index: 1;

  &.show {
    opacity: 1;
    visibility: visible;
  }

  .icon {
    font-size: 40px;
    color: #73aada;

    &:hover {
      color: var(--color-active)
    }
  }
}

.bounce-out-top {
  animation: bounce-out-top 1s ease both;
}

@keyframes bounce-out-top {
  0% {
    transform: translateY(0);
  }
  100% {
    transform: translateY(-2000px);
  }
}

@media screen and (max-width: 600px) {
  .back-top {
    right: 20px;
    bottom: 10px;

    span {
      font-size: 30px;
    }
  }
}
</style>