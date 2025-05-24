<!-- The exported code uses Tailwind CSS. Install Tailwind CSS in your dev environment to ensure all styles work. -->
<template>
  <div class="min-h-screen bg-gray-50 flex">
    <DriveSidebar/>
    <div class="flex-1 flex flex-col h-screen overflow-hidden">
      <DriveHeader/>
      <DriveToolbar/>
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
    <MetadataPanel/>

    <!-- Context Menu -->
    <ContextMenu/>

  </div>
</template>

<script setup>
import { onMounted, watch } from 'vue'

// Empty folder image
const emptyFolderImage =
  'https://readdy.ai/api/search-image?query=A%20minimalist%20illustration%20of%20an%20empty%20folder%20with%20a%20slight%20shadow%2C%20clean%20lines%2C%20simple%20design%2C%20light%20background%2C%20professional%20look%2C%20subtle%20colors%2C%20business%20context%2C%20cloud%20storage%20concept&width=300&height=300&seq=1&orientation=squarish'

import { useDriveStore } from '@/stores/drive.js'
import { storeToRefs } from 'pinia'
import { formatDate, formatSize } from '@/utils/format'
// import { uploadToServer } from '@/utils/drive/upload'
import DriveSidebar from '@/components/drive/DriveSidebar.vue'
import MetadataPanel from '@/components/drive/MetadataPanel.vue'
import ContextMenu from '@/components/drive/ContextMenu.vue'
import DriveToolbar from '@/components/drive/DriveToolbar.vue'
import DriveHeader from '@/components/drive/DriveHeader.vue'
const driveStore = useDriveStore()

// ref 꺼내 쓰기
const {
  folders,
  selectedFolder,
  expandedFolders,
  selectedItems,
  contextMenu,
  filteredItems,
} = storeToRefs(driveStore)

// 함수 꺼내 쓰기
const { selectItem, fetchItems, loadDirectoryTree, toggleItemSelection, closeContextMenu, showContextMenu } = driveStore

onMounted(() => {
  loadDirectoryTree()
  fetchItems()
  document.addEventListener('click', () => {
    if (contextMenu.value.show) {
      closeContextMenu()
    }
  })
})



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

</script>

<style scoped>
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
