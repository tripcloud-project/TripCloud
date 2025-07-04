<template>
  <div class="bg-gray-50 flex">
    <DriveSidebar />
    <div class="flex-1 flex flex-col h-full overflow-hidden">
      <DriveHeader />
      <DriveToolbar />
      <ContentGrid />
    </div>
    <MetadataPanel />
    <ContextMenu />
    <InputModal
      v-if="showInputModal"
      :title="inputModalTitle"
      :model-value="inputModalInitialValue"
      @submit="handleInputSubmit"
      @cancel="() => (showInputModal = false)"
    />
  </div>
</template>

<script setup>
import { onMounted, watch } from 'vue'

import { useDriveStore } from '@/stores/drive.js'
import { storeToRefs } from 'pinia'

import DriveSidebar from '@/components/drive/DriveSidebar.vue'
import MetadataPanel from '@/components/drive/MetadataPanel.vue'
import ContextMenu from '@/components/drive/ContextMenu.vue'
import DriveToolbar from '@/components/drive/DriveToolbar.vue'
import DriveHeader from '@/components/drive/DriveHeader.vue'
import ContentGrid from '@/components/drive/ContentGrid.vue'
import InputModal from '@/components/InputModal.vue'

const driveStore = useDriveStore()

// ref 꺼내 쓰기
const {
  folders,
  selectedFolder,
  expandedFolders,
  contextMenu,
  showInputModal,
  inputModalTitle,
  inputModalInitialValue,
  onInputSubmit,
} = storeToRefs(driveStore)

// 함수 꺼내 쓰기
const { fetchItems, loadDirectoryTree, closeContextMenu } = driveStore

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
    while (parentId) {
      if (!expandedFolders.value.includes(parentId)) {
        expandedFolders.value.push(parentId)
      }
      const parentFolder = folders.value.find((f) => f.id === parentId)
      parentId = parentFolder ? parentFolder.parent : null
    }
  }
})

const handleInputSubmit = async (value) => {
  if (typeof onInputSubmit.value === 'function') {
    await onInputSubmit.value(value)
    onInputSubmit.value = null
  }
  showInputModal.value = false
}
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
