<!-- Right Metadata Panel -->
<template>
  <div
    v-if="selectedFile"
    class="w-[300px] bg-white border-l border-gray-200 flex flex-col h-screen"
  >
    <div class="p-4 border-b border-gray-200 flex justify-between items-center">
      <h3 class="text-lg font-medium text-gray-800">파일 상세정보</h3>
      <button @click="clearFileSelection" class="text-gray-500 hover:text-gray-700 cursor-pointer">
        <i class="fas fa-times"></i>
      </button>
    </div>

    <!-- File Preview -->
    <div class="p-4 border-b border-gray-200">
      <div
        class="bg-gray-100 rounded-lg h-40 flex items-center justify-center overflow-hidden cursor-pointer"
      >
        <img
          v-if="selectedFile.type === 'image'"
          :src="selectedFile.preview"
          alt="File preview"
          class="max-w-full max-h-full object-contain"
          @click="showPreviewModal = true"
        />
        <i
          v-else-if="selectedFile.type === 'document'"
          class="fas fa-file-alt text-6xl text-green-400"
        ></i>
        <i
          v-else-if="selectedFile.type === 'video'"
          class="fas fa-file-video text-6xl text-purple-400"
        ></i>
        <i v-else class="fas fa-file text-6xl text-gray-400"></i>
      </div>
    </div>
    <!-- Modal -->
    <div
      v-if="showPreviewModal"
      class="fixed inset-0 bg-black bg-opacity-60 flex items-center justify-center z-50"
    >
      <div class="relative">
        <button
          class="absolute top-0 right-0 m-2 text-white text-xl font-bold"
          @click="showPreviewModal = false"
        >
          &times;
        </button>
        <img
          :src="selectedFile.preview"
          alt="Full preview"
          class="max-w-[90vw] max-h-[90vh] rounded shadow-lg"
        />
      </div>
    </div>

    <!-- File Information -->
    <div class="flex-grow overflow-y-auto p-4">
      <div class="space-y-4">
        <div>
          <h4 class="text-sm font-medium text-gray-500 mb-1">이름</h4>
          <p class="text-sm text-gray-800">{{ selectedFile.name }}</p>
        </div>

        <div>
          <h4 class="text-sm font-medium text-gray-500 mb-1">타입</h4>
          <p class="text-sm text-gray-800">{{ selectedFile.contentType }}</p>
        </div>

        <div>
          <h4 class="text-sm font-medium text-gray-500 mb-1">크기</h4>
          <p class="text-sm text-gray-800">{{ formatSize(selectedFile.size) }}</p>
        </div>

        <div v-if="selectedFile.taken">
          <h4 class="text-sm font-medium text-gray-500 mb-1">촬영일</h4>
          <p class="text-sm text-gray-800">{{ formatDateTime(selectedFile.taken) }}</p>
        </div>

        <div v-if="selectedFile.sido">
          <h4 class="text-sm font-medium text-gray-500 mb-1">위치</h4>
          <p class="text-sm text-gray-800">
            {{ selectedFile.sido }} {{ selectedFile.sigungu }} {{ selectedFile.eupmyeondong }}
          </p>
        </div>

        <div v-if="selectedFile.latitude && selectedFile.longitude">
          <h4 class="text-sm font-medium text-gray-500 mb-1">위경도</h4>
          <p class="text-sm text-gray-800">{{ selectedFile.latitude }}</p>
          <p class="text-sm text-gray-800">{{ selectedFile.longitude }}</p>
        </div>

        <div v-if="selectedFile.hashtags && selectedFile.hashtags.length > 0">
          <h4 class="text-sm font-medium text-gray-500 mb-1">해시태그</h4>
          <div class="flex flex-wrap gap-2">
            <span
              v-for="(tag, index) in selectedFile.hashtags"
              :key="index"
              class="inline-block bg-blue-100 text-blue-800 text-xs px-2.5 py-0.5 rounded-full"
            >
              #{{ tag }}
            </span>
          </div>
        </div>

        <div v-if="selectedFile.description">
          <h4 class="text-sm font-medium text-gray-500 mb-1">설명</h4>
          <p class="text-sm text-gray-800">{{ selectedFile.description }}</p>
        </div>
      </div>
    </div>

    <!-- Quick Actions -->
    <div class="p-4 border-t border-gray-200">
      <div class="grid grid-cols-3 gap-2">
        <button
          class="flex flex-col items-center p-2 rounded-lg hover:bg-gray-100 cursor-pointer !rounded-button whitespace-nowrap"
          @click="downloadSingleFile"
        >
          <i class="fas fa-download text-gray-700 mb-1"></i>
          <span class="text-xs">다운로드</span>
        </button>
        <button
          class="flex flex-col items-center p-2 rounded-lg hover:bg-gray-100 cursor-pointer !rounded-button whitespace-nowrap"
          @click="deleteSingleFile"
        >
          <i class="fas fa-trash-alt text-gray-700 mb-1"></i>
          <span class="text-xs">삭제</span>
        </button>
        <button
          class="flex flex-col items-center p-2 rounded-lg hover:bg-gray-100 cursor-pointer !rounded-button whitespace-nowrap"
          @click="handleRenameSingleFile"
        >
          <i class="fas fa-pen text-gray-700 mb-1"></i>
          <span class="text-xs">이름 변경</span>
        </button>

        <button
          class="flex flex-col items-center p-2 rounded-lg hover:bg-gray-100 cursor-pointer !rounded-button whitespace-nowrap"
          @click="handleDescriptionSingleFile"
        >
          <i class="fas fa-comment-alt text-gray-700 mb-1"></i>
          <span class="text-xs">설명 추가</span>
        </button>

        <button
          class="flex flex-col items-center p-2 rounded-lg hover:bg-gray-100 cursor-pointer !rounded-button whitespace-nowrap"
        >
          <i class="fas fa-hashtag text-gray-700 mb-1"></i>
          <span class="text-xs">해시태그 추가</span>
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { useMapStore } from '@/stores/map.js'
import { storeToRefs } from 'pinia'
import { downloadFiles } from '@/utils/map/download.js'
import { deleteFiles } from '@/utils/map/delete.js'
import { renameSingleFile } from '@/utils/map/rename.js'
import { formatDateTime, formatSize } from '@/utils/format'
import { descriptionSingleFile } from '@/utils/map/description.js'

const mapStore = useMapStore()
const { prefix, selectedFile, selectedItems } = storeToRefs(mapStore)

const { clearFileSelection, fetchItems, loadDirectoryTree } = mapStore

// [단건 다운로드]
const downloadSingleFile = async () => {
  const file = selectedFile.value
  if (!file || file.type === 'folder') return

  await downloadFiles({
    prefixList: [],
    fileIdList: [file.id],
    currentPrefix: prefix.value,
  })
}

// [단건 삭제]
const deleteSingleFile = async () => {
  const item = selectedFile.value
  if (!item) return

  const prefixList = item.type === 'folder' ? [item.id] : []
  const fileIdList = item.type !== 'folder' ? [item.id] : []

  const result = await deleteFiles({ prefixList, fileIdList })

  if (result.status === 'success') {
    await fetchItems()
    await loadDirectoryTree()
    selectedItems.value = []
    selectedFile.value = null
  }
}

// [선택 이름 변경]
const handleRenameSingleFile = async () => {
  await renameSingleFile({
    file: selectedFile.value,
    onRename: async () => {
      await fetchItems()
      await loadDirectoryTree()
    },
  })
}

// [선택 설명 추가]
const handleDescriptionSingleFile = async () => {
  const fileId = selectedFile.value.id // 또는 contextMenu.value.item.id 등
  const newDescription = prompt('설명을 입력하세요:', selectedFile.value.description || '')

  if (!newDescription) return

  const result = await descriptionSingleFile({
    fileId,
    description: newDescription
  })

  if (result.status === 'success') {
    alert('설명이 수정되었습니다.')
    selectedFile.value.description = newDescription
    await fetchItems()
  }
}
import { ref } from 'vue'

const showPreviewModal = ref(false)
</script>

<style scoped></style>
