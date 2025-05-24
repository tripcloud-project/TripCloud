<!-- The exported code uses Tailwind CSS. Install Tailwind CSS in your dev environment to ensure all styles work. -->
<template>
  <div class="min-h-screen bg-gray-50 flex">
    <Sidebar/>
    <!-- Main Content Area -->
    <div class="flex-1 flex flex-col h-screen overflow-hidden">
      <!-- Header -->
      <div class="bg-white border-b border-gray-200 p-4 flex items-center justify-between">
        <div class="flex items-center space-x-2">
          <div class="flex space-x-4">
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
        <div class="flex items-center">
          <h2 class="text-lg font-medium text-gray-800">{{ currentFolderName }}</h2>
          <span class="text-gray-500 ml-2">({{ filteredItems.length }} items)</span>
        </div>
        <!-- Sort Options -->
        <div class="flex items-center space-x-2">
          <span class="text-sm text-gray-500">정렬: </span>
          <select
            v-model="sortBy"
            class="text-sm border-none bg-gray-100 rounded-lg py-1 px-2 focus:outline-none focus:ring-2 focus:ring-blue-500"
          >
            <option value="name">이름</option>
            <option value="modified">수정일</option>
            <option value="size">크기</option>
          </select>
          <button
            @click="toggleSortDirection"
            class="p-1 rounded-full hover:bg-gray-100 cursor-pointer"
          >
            <i
              :class="[
                'fas',
                sortDirection === 'asc' ? 'fa-sort-up' : 'fa-sort-down',
                'text-gray-500',
              ]"
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

      <!-- Bulk Actions Toolbar (shows when items selected) -->
      <div
        v-if="selectedItems.length > 0"
        class="bg-white border-b border-gray-200 p-3 flex items-center"
      >
        <span class="text-sm font-medium mr-4">{{ selectedItems.length }} items selected</span>
        <div class="flex space-x-2">
          <button
            class="px-3 py-1.5 bg-blue-50 text-blue-600 rounded-lg text-sm font-medium hover:bg-blue-100 cursor-pointer !rounded-button whitespace-nowrap"
            @click="downloadSelectedFiles"
          >
            <i class="fas fa-download mr-1"></i> Download
          </button>
          <!-- <button
            class="px-3 py-1.5 bg-blue-50 text-blue-600 rounded-lg text-sm font-medium hover:bg-blue-100 cursor-pointer !rounded-button whitespace-nowrap"
          >
            <i class="fas fa-share-alt mr-1"></i> Share
          </button> -->
          <button
            class="px-3 py-1.5 bg-red-50 text-red-600 rounded-lg text-sm font-medium hover:bg-red-100 cursor-pointer !rounded-button whitespace-nowrap"
            @click="deleteSelectedFiles"
          >
            <i class="fas fa-trash-alt mr-1"></i> Delete
          </button>
        </div>
        <button
          @click="clearSelection"
          class="ml-auto text-sm text-gray-500 hover:text-gray-700 cursor-pointer"
        >
          Clear selection
        </button>
      </div>

      <!-- Content Grid -->
      <div class="flex-grow overflow-y-auto p-6 bg-white">
        <div
          v-if="filteredItems.length === 0"
          class="flex flex-col items-center justify-center h-full text-center"
        >
          <img :src="emptyFolderImage" alt="Empty folder" class="w-64 h-64 object-contain mb-4" />
          <h3 class="text-lg font-medium text-gray-700 mb-1">This folder is empty</h3>
          <p class="text-sm text-gray-500 mb-4">Drag and drop files here to upload</p>
        </div>

        <div v-else class="grid grid-cols-1 sm:grid-cols-3 md:grid-cols-4 lg:grid-cols-5 gap-4">
          <div
            v-for="item in filteredItems"
            :key="item.id"
            :class="[
              'relative group border border-gray-200 rounded-lg p-3 hover:border-blue-300 hover:shadow-sm transition-all cursor-pointer',
              selectedItems.includes(item.id) ? 'bg-blue-50 border-blue-300' : 'bg-white',
            ]"
            @click="selectItem(item.id)"
            @contextmenu.prevent="showContextMenu($event, item)"
          >
            <!-- Checkbox -->
            <div
              :class="[
                'absolute top-2 left-2 w-5 h-5 rounded border flex items-center justify-center transition-all',
                selectedItems.includes(item.id)
                  ? 'bg-blue-500 border-blue-500'
                  : 'border-gray-300 bg-white opacity-0 group-hover:opacity-100',
              ]"
              @click.stop="toggleItemSelection(item.id)"
            >
              <i v-if="selectedItems.includes(item.id)" class="fas fa-check text-white text-xs"></i>
            </div>

            <!-- Icon / Image Preview -->
            <div class="flex flex-col items-center pt-6 pb-2">
              <div class="w-20 h-20 flex items-center justify-center mb-2">
                <!-- 크기 증가 -->
                <i v-if="item.type === 'folder'" class="fas fa-folder text-5xl text-yellow-400"></i>
                <img
                  v-else-if="item.type === 'image'"
                  :src="item.preview"
                  alt="Preview"
                  class="w-20 h-20 object-cover rounded-lg"
                />
                <i
                  v-else-if="item.type === 'document'"
                  class="fas fa-file-alt text-5xl text-green-400"
                ></i>
                <i
                  v-else-if="item.type === 'video'"
                  class="fas fa-file-video text-5xl text-purple-400"
                ></i>
                <i v-else class="fas fa-file text-5xl text-gray-400"></i>
              </div>
              <span
                v-if="item.type !== 'folder'"
                class="text-sm font-medium text-center truncate w-full"
                >{{ item.name }}</span
              >
              <span v-else class="text-sm font-medium text-center truncate w-full">{{
                item.name.slice(0, -1)
              }}</span>
              <span v-if="item.type !== 'folder'" class="text-xs text-gray-500 mt-1">{{
                formatDate(item.modified)
              }}</span>
              <span class="text-xs text-gray-500">{{ formatSize(item.size) }}</span>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- Right Metadata Panel -->
    <div
      v-if="selectedFile"
      class="w-[300px] bg-white border-l border-gray-200 flex flex-col h-screen"
    >
      <div class="p-4 border-b border-gray-200 flex justify-between items-center">
        <h3 class="text-lg font-medium text-gray-800">파일 상세정보</h3>
        <button
          @click="clearFileSelection"
          class="text-gray-500 hover:text-gray-700 cursor-pointer"
        >
          <i class="fas fa-times"></i>
        </button>
      </div>

      <!-- File Preview -->
      <div class="p-4 border-b border-gray-200">
        <div class="bg-gray-100 rounded-lg h-40 flex items-center justify-center overflow-hidden">
          <img
            v-if="selectedFile.type === 'image'"
            :src="selectedFile.preview"
            alt="File preview"
            class="max-w-full max-h-full object-contain"
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

          <div v-if="selectedFile.sido">
            <h4 class="text-sm font-medium text-gray-500 mb-1">위치</h4>
            <p class="text-sm text-gray-800">
              {{ selectedFile.sido }} {{ selectedFile.sigungu }} {{ selectedFile.eupmyeondong }}
            </p>
          </div>

          <div v-if="selectedFile.latitude">
            <h4 class="text-sm font-medium text-gray-500 mb-1">위도</h4>
            <p class="text-sm text-gray-800">{{ selectedFile.latitude }}</p>
          </div>

          <div v-if="selectedFile.longitude">
            <h4 class="text-sm font-medium text-gray-500 mb-1">경도</h4>
            <p class="text-sm text-gray-800">{{ selectedFile.longitude }}</p>
          </div>

          <div>
            <h4 class="text-sm font-medium text-gray-500 mb-1">크기</h4>
            <p class="text-sm text-gray-800">{{ formatSize(selectedFile.size) }}</p>
          </div>

          <div>
            <h4 class="text-sm font-medium text-gray-500 mb-1">생성일</h4>
            <p class="text-sm text-gray-800">{{ formatDateTime(selectedFile.created) }}</p>
          </div>

          <div>
            <h4 class="text-sm font-medium text-gray-500 mb-1">수정일</h4>
            <p class="text-sm text-gray-800">{{ formatDateTime(selectedFile.modified) }}</p>
          </div>

          <div v-if="selectedFile.taken">
            <h4 class="text-sm font-medium text-gray-500 mb-1">촬영일</h4>
            <p class="text-sm text-gray-800">{{ formatDateTime(selectedFile.taken) }}</p>
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
            <h4 class="text-sm font-medium text-gray-500 mb-1">Description</h4>
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
            <i class="fas fa-share-alt text-gray-700 mb-1"></i>
            <span class="text-xs">이름 변경</span>
          </button>
          <button
            class="flex flex-col items-center p-2 rounded-lg hover:bg-gray-100 cursor-pointer !rounded-button whitespace-nowrap"
          >
            <i class="fas fa-share-alt text-gray-700 mb-1"></i>
            <span class="text-xs">설명 추가</span>
          </button>
          <button
            class="flex flex-col items-center p-2 rounded-lg hover:bg-gray-100 cursor-pointer !rounded-button whitespace-nowrap"
          >
            <i class="fas fa-share-alt text-gray-700 mb-1"></i>
            <span class="text-xs">해시태그 추가</span>
          </button>
        </div>
      </div>
    </div>

    <!-- Context Menu -->
    <div
      v-if="contextMenu.show"
      class="fixed bg-white shadow-lg rounded-lg border border-gray-200 w-48 z-50"
      :style="{ top: `${contextMenu.y}px`, left: `${contextMenu.x}px` }"
    >
      <div class="py-1">
        <button
          v-for="(item, index) in contextMenuItems"
          :key="index"
          :class="[
            'w-full text-left px-4 py-2 text-sm hover:bg-gray-100 cursor-pointer flex items-center',
            item.type === 'divider' ? 'border-t border-gray-200 my-1' : '',
            item.danger ? 'text-red-600' : 'text-gray-700',
          ]"
          @click="handleContextMenuAction(item.action)"
        >
          <i v-if="item.icon" :class="['fas', item.icon, 'mr-2 w-4']"></i>
          <span>{{ item.label }}</span>
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { onMounted, watch } from 'vue'

// Empty folder image
const emptyFolderImage =
  'https://readdy.ai/api/search-image?query=A%20minimalist%20illustration%20of%20an%20empty%20folder%20with%20a%20slight%20shadow%2C%20clean%20lines%2C%20simple%20design%2C%20light%20background%2C%20professional%20look%2C%20subtle%20colors%2C%20business%20context%2C%20cloud%20storage%20concept&width=300&height=300&seq=1&orientation=squarish'

// import api from '@/lib/api'
// import flattenDirectoryTree from '@/utils/drive/flattenDirectoryTree'
import { useDriveStore } from '@/stores/drive.js'
// import mapApiResponseToItems from '@/utils/drive/mapApiResponseToItems.js'
import { storeToRefs } from 'pinia'
import { formatDate, formatDateTime, formatSize } from '@/utils/format'
import { deleteFiles } from '@/utils/drive/delete'
import { renameFile, renameDirectory, renameSingleFile } from '@/utils/drive/rename'
import { uploadToServer } from '@/utils/drive/upload'
import { downloadFiles } from '@/utils/drive/download.js'
import Sidebar from '@/components/drive/Sidebar.vue'
const driveStore = useDriveStore()

// ref 꺼내 쓰기
const {
  prefix,
  items,
  folders,
  selectedFolder,
  expandedFolders,
  selectedItems,
  selectedFile,
  sortBy,
  sortDirection,
  contextMenu,
  currentFolderName,
  filteredItems,
  contextMenuItems,
  fileInput,
  folderInput,
} = storeToRefs(driveStore)

// 함수 꺼내 쓰기
const { clearSelection, selectFolder, fetchItems, loadDirectoryTree } = driveStore


onMounted(() => {
  loadDirectoryTree()
  fetchItems()
  document.addEventListener('click', () => {
    if (contextMenu.value.show) {
      closeContextMenu()
    }
  })
})

const selectItem = (itemId) => {
  const item = items.value.find((i) => i.id === itemId)
  if (item && item.type !== 'folder') {
    selectedFile.value = item
  } else if (item && item.type === 'folder') {
    selectFolder(itemId)
  }
}

const toggleItemSelection = (itemId) => {
  const index = selectedItems.value.indexOf(itemId)
  if (index === -1) {
    selectedItems.value.push(itemId)
  } else {
    selectedItems.value.splice(index, 1)
  }
}


const clearFileSelection = () => {
  selectedFile.value = null
}

const toggleSortDirection = () => {
  sortDirection.value = sortDirection.value === 'asc' ? 'desc' : 'asc'
}

const showContextMenu = (event, item) => {
  event.preventDefault()
  contextMenu.value = {
    show: true,
    x: event.clientX,
    y: event.clientY,
    item,
  }

  // Add event listener to close context menu when clicking elsewhere
  setTimeout(() => {
    window.addEventListener('click', closeContextMenu)
  }, 0)
}

const closeContextMenu = () => {
  contextMenu.value.show = false
  window.removeEventListener('click', closeContextMenu)
}

const handleContextMenuAction = async (action) => {
  const item = contextMenu.value.item

  switch (action) {
    case 'open':
      if (item.type === 'folder') {
        selectFolder(item.id)
      } else {
        selectItem(item.id)
      }
      break
    case 'rename': {
      if (item.type !== 'folder') {
        const currentName = item.name
        const dotIndex = currentName.lastIndexOf('.')
        const nameOnly = currentName.slice(0, dotIndex)
        const extension = currentName.slice(dotIndex)
        const newNameOnly = prompt('새 이름을 입력하세요', nameOnly)

        if (newNameOnly && newNameOnly !== nameOnly) {
          const newFullName = newNameOnly + extension
          await handleRenameFile(item.id, newFullName)
        }
      } else {
        const oldPrefix = prefix.value + item.name
        const newName = prompt('새 이름을 입력하세요', item.name.slice(0, -1))
        const newPrefix = prefix.value + newName + '/'
        if (oldPrefix && newPrefix) {
          await handleRenameDirectory(oldPrefix, newPrefix)
        }
      }
      break
    }
    case 'copy':
      // Implement copy functionality
      console.log('Copy', item.name)
      break
    case 'move':
      // Implement move functionality
      console.log('Move', item.name)
      break
    case 'download':
      // Implement download functionality
      if (!selectedItems.value.includes(item.id)) {
        toggleItemSelection(item.id)
      }
      downloadSelectedFiles()
      break
    case 'delete':
      // Implement delete functionality
      if (!selectedItems.value.includes(item.id)) {
        toggleItemSelection(item.id)
      }
      deleteSelectedFiles()
      break
  }

  closeContextMenu()
}



// Watch for changes in selected folder to update expanded folders
watch(selectedFolder, (newFolderId) => {
  // Find the folder
  const folder = folders.value.find((f) => f.id === newFolderId)
  if (folder) {
    // Make sure all parent folders are expanded
    let parentId = folder.parent
    console.log('parentId: ', parentId)
    while (parentId) {
      if (!expandedFolders.value.includes(parentId)) {
        expandedFolders.value.push(parentId)
        console.log('expandedFolders.value: ', expandedFolders.value)
      }
      const parentFolder = folders.value.find((f) => f.id === parentId)
      parentId = parentFolder ? parentFolder.parent : null
    }
  }
})

// [다중 다운로드]
const downloadSelectedFiles = async () => {
  const prefixList = selectedItems.value
    .map((id) => filteredItems.value.find((i) => i.id === id && i.type === 'folder')?.id)
    .filter(Boolean)

  const fileIdList = selectedItems.value
    .map((id) => filteredItems.value.find((i) => i.id === id && i.type !== 'folder')?.id)
    .filter(Boolean)

  await downloadFiles({
    prefixList,
    fileIdList,
    currentPrefix: prefix.value,
  })
}

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

// [다중 삭제]
const deleteSelectedFiles = async () => {
  const prefixList = selectedItems.value
    .map((id) => filteredItems.value.find((i) => i.id === id && i.type === 'folder')?.id)
    .filter(Boolean)

  const fileIdList = selectedItems.value
    .map((id) => filteredItems.value.find((i) => i.id === id && i.type !== 'folder')?.id)
    .filter(Boolean)

  const result = await deleteFiles({ prefixList, fileIdList })

  if (result.status === 'success') {
    await fetchItems()
    await loadDirectoryTree()
    selectedItems.value = []
  }
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

// [이름 변경]
const handleRenameFile = async (fileId, newFilename) => {
  const result = await renameFile(fileId, newFilename)
  if (result.status === 'success') {
    await fetchItems()
    await loadDirectoryTree()
  } else {
    console.warn('이름 변경 실패:', result)
  }
}

// [디렉토리 이름 변경]
const handleRenameDirectory = async (oldPrefix, newPrefix) => {
  const result = await renameDirectory(oldPrefix, newPrefix)
  if (result.status === 'success') {
    await fetchItems()
    await loadDirectoryTree()
  } else {
    console.warn('디렉토리 이름 변경 실패:', result)
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
    loadDirectoryTree()
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
</script>

<style scoped>
/* Custom scrollbar */
::-webkit-scrollbar {
  width: 8px;
  height: 8px;
}

::-webkit-scrollbar-track {
  background: #f1f1f1;
  border-radius: 4px;
}

::-webkit-scrollbar-thumb {
  background: #c1c1c1;
  border-radius: 4px;
}

::-webkit-scrollbar-thumb:hover {
  background: #a1a1a1;
}

/* Remove default number input arrows */
input[type='number']::-webkit-inner-spin-button,
input[type='number']::-webkit-outer-spin-button {
  -webkit-appearance: none;
  margin: 0;
}

input[type='number'] {
  -moz-appearance: textfield;
}
</style>
