<template>
  <div class="bg-white border-b border-gray-200 p-4 flex items-center justify-between">
    <div class="flex items-center space-x-2">
      <div class="flex space-x-4">
        <div class="flex items-center">
          <h2 class="text-lg font-medium text-gray-800">{{ currentFolderName }}</h2>
          <span class="text-gray-500 ml-2">({{ filteredItems.length }} items)</span>
        </div>

        <button
          class="px-4 py-2 bg-blue-600 text-white rounded-lg text-sm font-medium hover:bg-blue-700 cursor-pointer whitespace-nowrap"
          @click="triggerFileSelect"
        >
          <i class="fas fa-file-upload mr-1"></i> 파일 업로드
        </button>

        <!-- 폴더 업로드 버튼 -->
        <button
          class="px-4 py-2 bg-green-600 text-white rounded-lg text-sm font-medium hover:bg-green-700 cursor-pointer whitespace-nowrap"
          @click="triggerFolderSelect"
        >
          <i class="fas fa-folder-open mr-1"></i> 폴더 업로드
        </button>
        <!-- 숨겨진 input들 -->
        <input type="file" ref="fileInput" multiple class="hidden" @change="handleFileUpload" />
        <input
          type="file"
          ref="folderInput"
          webkitdirectory
          directory
          multiple
          class="hidden"
          @change="handleFolderUpload"
        />
      </div>
    </div>

    <div class="flex items-center space-x-2">
      <!-- 검색 타입 선택과 검색창 -->
      <div class="relative flex items-center space-x-2">
        <!-- 검색 타입 드롭다운 -->
        <select
          v-model="searchType"
          class="text-sm border-none bg-gray-100 rounded-lg py-1 px-2 focus:outline-none focus:ring-2 focus:ring-blue-500"
        >
          <option value="file">파일/디렉토리</option>
          <option value="hashtag">해시태그</option>
          <option value="description">설명</option>
        </select>

        <!-- 검색 입력창 -->
        <div class="relative">
          <input
            v-model="searchQuery"
            type="text"
            placeholder="검색어 입력..."
            class="text-sm border-none bg-gray-100 rounded-lg py-1 pl-2 pr-6 focus:outline-none focus:ring-2 focus:ring-blue-500"
            @keyup.enter="handleSearch"
          />
          <i class="fas fa-search absolute right-2 top-2 text-gray-500"></i>
        </div>
      </div>

      <!-- 정렬창 -->
      <span class="text-sm text-gray-500">정렬: </span>
      <select
        v-model="sortBy"
        class="text-sm border-none bg-gray-100 rounded-lg py-1 px-2 focus:outline-none focus:ring-2 focus:ring-blue-500"
      >
        <option value="name">이름</option>
        <option value="taken">촬영일</option>
        <option value="size">크기</option>
      </select>
      <button
        @click="toggleSortDirection"
        class="p-1 rounded-full hover:bg-gray-100 cursor-pointer"
      >
        <i
          :class="['fas', sortDirection === 'asc' ? 'fa-sort-up' : 'fa-sort-down', 'text-gray-500']"
        ></i>
      </button>

      <div class="ml-4 border-l border-gray-300 pl-4">
        <button class="p-2 rounded-lg hover:bg-gray-100 cursor-pointer">
          <i class="fas fa-th text-gray-500"></i>
        </button>
        <button class="p-2 rounded-lg hover:bg-gray-100 cursor-pointer">
          <i class="fas fa-list text-gray-500"></i>
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { useDriveStore } from '@/stores/drive.js'
import { storeToRefs } from 'pinia'
import { uploadToServer } from '@/utils/drive/upload'
const driveStore = useDriveStore()
const { prefix, fileInput, folderInput, sortBy, sortDirection, currentFolderName, filteredItems, items, selectedFolder } =
  storeToRefs(driveStore)

const { fetchItems, loadDirectoryTree } = driveStore

const toggleSortDirection = () => {
  sortDirection.value = sortDirection.value === 'asc' ? 'desc' : 'asc'
}

// 파일 선택창 열기
const triggerFileSelect = () => {
  fileInput.value.click()
}

// 폴더 선택창 열기
const triggerFolderSelect = () => {
  folderInput.value.click()
}
// [파일 업로드]
const handleFileUpload = async () => {
  const files = fileInput.value?.files
  if (!files || files.length === 0) return

  const formData = new FormData()
  for (let i = 0; i < files.length; i++) {
    formData.append('files', files[i])
  }
  formData.append('prefix', prefix.value)

  const result = await uploadToServer(formData)
  if (result.status === 'success') {
    alert('업로드 성공!')
    await fetchItems()
    await loadDirectoryTree()
  } else {
    alert('업로드 실패')
  }

  fileInput.value.value = null
}

// [폴더 업로드]
const handleFolderUpload = async () => {
  const files = folderInput.value?.files
  if (!files || files.length === 0) return

  const formData = new FormData()
  for (let i = 0; i < files.length; i++) {
    formData.append('files', files[i])
  }
  formData.append('prefix', prefix.value)

  const result = await uploadToServer(formData)
  if (result.status === 'success') {
    alert('업로드 성공!')
    await fetchItems()
    loadDirectoryTree()
  } else {
    alert('업로드 실패')
  }

  folderInput.value.value = null
}

import { ref } from 'vue'
import {searchFiles} from '@/utils/drive/search.js'
import mapApiResponseToItems from '@/utils/drive/mapApiResponseToItems'

// 검색 관련 상태
const searchType = ref('file') // 기본값은 파일/디렉토리
const searchQuery = ref('')

// 검색 실행 함수
const handleSearch = async () => {
  if (!searchQuery.value.trim()) {
    return
  }

    try {
    const data = await searchFiles(searchType.value, searchQuery.value)

    if (data.status === 'success') {
      selectedFolder.value = '/'
      prefix.value = '/'
      items.value = mapApiResponseToItems(data.result, '/')
      searchQuery.value = ''
    } else {
      console.error('검색 실패:', error)
    }
  } catch (error) {
    console.error('검색 오류:', error)
  }
}
</script>

<style scoped></style>
