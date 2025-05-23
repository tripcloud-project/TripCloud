<template>
  <div>
    <h3>총 용량: {{ formattedSize }}</h3>
    <ul id="fileList">
      <template v-for="dir in directories" :key="dir.name">
        <li>
          <input type="checkbox" class="entryCheckbox" :data-key="prefix + dir.name" data-type="directory" />
          <a href="#" @click.prevent="goToDirectory(prefix + dir.name)">
            {{ dir.name.slice(0, -1) }} ({{ formatBytes(dir.size) }})
          </a>
          <button @click="renameDirectory(dir.name)">✏️ 이름 변경</button>
        </li>
      </template>
      <template v-for="file in files" :key="file.fileId">
        <li>
          <input
            type="checkbox"
            class="entryCheckbox"
            :data-key="prefix + file.name"
            data-type="file"
            :data-file-id="file.fileId"
          />
          <a href="#" @click.prevent="fetchMetadata(file.fileId)">
            {{ file.name }} ({{ formatBytes(file.size) }})
          </a>
          <button @click="renameFile(file)">✏️ 이름 변경</button>
        </li>
      </template>
    </ul>

    <div id="fileGrid">
      <div v-for="dir in directories" :key="dir.name" class="item" @click="goToDirectory(prefix + dir.name)">
        <img src="https://cdn-icons-png.flaticon.com/512/716/716784.png" alt="folder" />
        <div class="name">{{ dir.name.slice(0, -1) }}</div>
      </div>

      <div v-for="file in files" :key="file.fileId" class="item" @click="fetchMetadata(file.fileId)">
        <img :src="file.presignedUrl" alt="image" />
        <div class="name">{{ file.name }}</div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import api from '@/lib/api'
import { useDriveStore } from '@/stores/drive'

const driveStore = useDriveStore()
const prefix = computed(() => driveStore.prefix)
const directories = ref([])
const files = ref([])
const totalSize = ref(0)

const formatBytes = (bytes) => {
  if (bytes === 0) return '0 Bytes'
  const k = 1024
  const sizes = ['Bytes', 'KB', 'MB', 'GB']
  const i = Math.floor(Math.log(bytes) / Math.log(k))
  return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i]
}

const formattedSize = computed(() => formatBytes(totalSize.value))

const loadDirectory = async () => {
  try {
    const { data } = await api.get('/gallery/list', {
      params: { prefix: prefix.value },
    })
    console.log(data.result)

    directories.value = data.result.directories
    files.value = data.result.files
    totalSize.value = data.result.totalSize
  } catch (err) {
    console.error('디렉토리 불러오기 실패:', err)
  }
}

const goToDirectory = (newPrefix) => {
  driveStore.setPrefix(newPrefix)
  loadDirectory()
}

const renameDirectory = async (dirName) => {
  const original = dirName.replace(/\/$/, '')
  const newName = prompt('새 이름을 입력하세요', original)
  if (!newName || newName === original) return

  const oldPrefix = prefix.value + dirName
  const newPrefix = prefix.value + newName + '/'

  try {
    await api.put('/gallery/rename', {
      oldPrefix,
      newPrefix,
    })
    alert('이름 변경 성공!')
    loadDirectory()
  } catch {
    alert('이름 변경 실패')
  }
}

const renameFile = async (file) => {
  const original = file.name
  const dotIndex = original.lastIndexOf('.')
  const base = original.slice(0, dotIndex)
  const ext = original.slice(dotIndex)

  const newBase = prompt('새 이름을 입력하세요', base)
  if (!newBase || newBase === base) return

  try {
    await api.put(`/gallery/rename/${file.fileId}`, {
      filename: newBase + ext,
    })
    alert('이름 변경 성공!')
    loadDirectory()
  } catch {
    alert('이름 변경 실패')
  }
}

const fetchMetadata = (fileId) => {
  // 원하는 메타데이터 요청 함수
  console.log('파일 메타데이터 조회:', fileId)
}

onMounted(() => {
  loadDirectory()
})
</script>

<style scoped>
.item {
  display: inline-block;
  width: 120px;
  text-align: center;
  margin: 10px;
}
</style>
