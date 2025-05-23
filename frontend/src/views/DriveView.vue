<template>
  <div class="drive-container">
    <h2>📁 내 파일 관리</h2>

    <!-- 업로드 컴포넌트 -->
    <FileUpload @uploaded="refresh" />

    <hr />

    <!-- 파일/폴더 리스트 컴포넌트 -->
    <FileList ref="fileList" />
  </div>
</template>

<script setup>
import FileUpload from '@/components/FileUpload.vue'
import FileList from '@/components/FileList.vue'
import {ref} from 'vue'

// 업로드 후 디렉토리 새로고침
const fileList = ref(null)
const refresh = () => {
  fileList.value?.loadDirectory()
}
</script>

<style scoped>
.drive-container {
  max-width: 900px;
  margin: 0 auto;
  padding: 1rem;
}
</style>
