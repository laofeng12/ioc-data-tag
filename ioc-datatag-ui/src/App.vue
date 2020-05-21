<template>
  <div id="app" class="datatagweb-postcss">
    <url-keep-alive v-if="singleSpaNavigate"><router-view :key="key"></router-view></url-keep-alive>
    <router-view v-else></router-view>
  </div>
</template>

<script>
export default {
  name: 'App',
  computed: {
    singleSpaNavigate () {
      return window.singleSpaNavigate
    },
    key () {
      return this.$route.fullPath
    }
  }
}
</script>
