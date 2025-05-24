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
import { useTrashStore } from '@/stores/trash.js'
import { storeToRefs } from 'pinia'
const trashStore = useTrashStore()
const {
  contextMenu,
  contextMenuItems,
  selectedItems,

} = storeToRefs(trashStore)

const { selectItem, selectFolder, closeContextMenu, toggleItemSelection, deleteSelectedFiles } = trashStore

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
    case 'copy':
      // Implement copy functionality
      console.log('Copy', item.name)
      break
    case 'move':
      // Implement move functionality
      console.log('Move', item.name)
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




</script>

<style scoped></style>