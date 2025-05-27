<template>
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
</template>

<script setup>
import { useDriveStore } from '@/stores/drive.js'
import { storeToRefs } from 'pinia'
import { renameFile, renameDirectory } from '@/utils/drive/rename'
import { descriptionSingleFile } from '@/utils/drive/description.js'
const driveStore = useDriveStore()
const {
  contextMenu,
  contextMenuItems,
  prefix,
  selectedItems,
  showInputModal,
  inputModalTitle,
  inputModalInitialValue,
  onInputSubmit,
} = storeToRefs(driveStore)

const {
  selectItem,
  selectFolder,
  closeContextMenu,
  toggleItemSelection,
  downloadSelectedFiles,
  deleteSelectedFiles,
  fetchItems,
  loadDirectoryTree,
} = driveStore

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

        inputModalTitle.value = '파일 이름 변경'
        inputModalInitialValue.value = nameOnly
        onInputSubmit.value = async (newNameOnly) => {
          if (!newNameOnly || newNameOnly === nameOnly) return
          const newFullName = newNameOnly + extension
          await handleRenameFile(item.id, newFullName)
          showInputModal.value = false
        }
        showInputModal.value = true
      } else {
        // 폴더 이름인 경우
        const oldPrefix = prefix.value + item.name
        const nameOnly = item.name.slice(0, -1)

        inputModalTitle.value = '폴더 이름 변경'
        inputModalInitialValue.value = nameOnly
        onInputSubmit.value = async (newNameOnly) => {
          if (!newNameOnly || newNameOnly === nameOnly) return
          const newPrefix = prefix.value + newNameOnly + '/'
          await handleRenameDirectory(oldPrefix, newPrefix)
          showInputModal.value = false
        }
        showInputModal.value = true
      }
      break
    }
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
    case 'addDescription': {
      const item = contextMenu.value.item

      inputModalTitle.value = '설명 추가'
      inputModalInitialValue.value = item.description || ''
      onInputSubmit.value = async (desc) => {
        if (!desc) return
        const result = await descriptionSingleFile({ fileId: item.id, description: desc })
        if (result.status === 'success') {
          item.description = desc
          await fetchItems()
        }
        showInputModal.value = false
      }
      showInputModal.value = true
      break
    }
  }

  closeContextMenu()
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
</script>

<style scoped></style>
