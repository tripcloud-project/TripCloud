<template>
  <div
    v-if="selectedItems.length > 0"
    class="bg-white border-b border-gray-200 p-3 flex items-center"
  >
    <span class="text-sm font-medium mr-4">{{ selectedItems.length }} items selected</span>
    <div class="flex space-x-2">
      <button
        class="px-3 py-1.5 bg-blue-50 text-blue-600 rounded-lg text-sm font-medium hover:bg-blue-100 cursor-pointer !rounded-button whitespace-nowrap"
        @click="restoreSelectedFiles"
      >
        <i class="fas fa-download mr-1"></i> 복원
      </button>
      <button
        class="px-3 py-1.5 bg-red-50 text-red-600 rounded-lg text-sm font-medium hover:bg-red-100 cursor-pointer !rounded-button whitespace-nowrap"
        @click="deleteSelectedFiles"
      >
        <i class="fas fa-trash-alt mr-1"></i> 영구삭제
      </button>
    </div>
    <button
      @click="clearSelection"
      class="ml-auto text-sm text-gray-500 hover:text-gray-700 cursor-pointer"
    >
      Clear selection
    </button>
  </div>
</template>

<script setup>
import { useTrashStore } from '@/stores/trash.js'
import { storeToRefs } from 'pinia'
const trashStore = useTrashStore()
const { selectedItems } = storeToRefs(trashStore)

const { restoreSelectedFiles, deleteSelectedFiles, clearSelection } = trashStore
</script>

<style scoped></style>
