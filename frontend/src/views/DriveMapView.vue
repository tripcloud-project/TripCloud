<template>
  <div class="min-h-screen bg-gray-50 flex">
    <DriveSidebar />
    <div class="flex-1 flex flex-col h-screen overflow-hidden">
      <p>지도입니다</p>
      <DriveHeader />
      <DriveToolbar />
      <ContentGrid />
    </div>
    <ContextMenu />
  </div>
</template>

<script setup>
import { onMounted, watch } from 'vue'

import { useMapStore } from '@/stores/map.js'
import { storeToRefs } from 'pinia'

import DriveSidebar from '@/components/map/DriveSidebar.vue'
import ContextMenu from '@/components/map/ContextMenu.vue'
import DriveToolbar from '@/components/map/DriveToolbar.vue'
import DriveHeader from '@/components/map/DriveHeader.vue'
import ContentGrid from '@/components/map/ContentGrid.vue'

const mapStore = useMapStore()

// ref 꺼내 쓰기
const { folders, selectedFolder, expandedFolders, contextMenu } = storeToRefs(mapStore)

// 함수 꺼내 쓰기
const { fetchItems, loadDirectoryTree, closeContextMenu } = mapStore

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
