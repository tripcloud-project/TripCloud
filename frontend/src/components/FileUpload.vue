<template>
  <form @submit.prevent="handleUpload" enctype="multipart/form-data">
    <label>
      파일 선택:
      <input type="file" ref="fileInput" multiple />
    </label>
    <br />
    <label>
      폴더 선택:
      <input type="file" ref="folderInput" webkitdirectory directory multiple />
    </label>
    <br />
    <button type="submit">업로드</button>
  </form>
</template>

<script setup>
import { ref } from 'vue'
import api from '@/lib/api'
import { useDriveStore } from '@/stores/drive'

const fileInput = ref(null)
const folderInput = ref(null)
const driveStore = useDriveStore()

const handleUpload = async () => {
  const formData = new FormData()

  // 파일 추가
  const files = fileInput.value.files
  for (let i = 0; i < files.length; i++) {
    formData.append('files', files[i])
  }

  // 폴더 선택에서의 파일도 추가
  const folderFiles = folderInput.value.files
  for (let i = 0; i < folderFiles.length; i++) {
    formData.append('files', folderFiles[i])
  }

  // prefix 설정
  formData.append('prefix', driveStore.prefix)

  try {
    await api.post('/gallery/upload', formData, {
      headers: {
        'Content-Type': 'multipart/form-data',
      },
    })

    alert('업로드 성공!')
    fileInput.value.value = null
    folderInput.value.value = null
    // 필요 시 목록 다시 로드
    // loadDirectory(driveStore.prefix)
  } catch (err) {
    console.error('업로드 실패:', err)
    alert('업로드 실패')
  }
}
</script>

<style scoped></style>
