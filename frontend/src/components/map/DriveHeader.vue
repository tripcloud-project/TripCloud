<template>
  <div class="bg-white border-b border-gray-200 p-4 flex items-center justify-between">
    <div class="flex items-center">
      <h2 class="text-lg font-medium text-gray-800">{{ currentFolderName }}</h2>
      <span class="text-gray-500 ml-2">({{ filteredItems.length }} items)</span>
    </div>
  </div>
</template>

<script setup>
import { useMapStore } from '@/stores/map.js'
import { storeToRefs } from 'pinia'
const mapStore = useMapStore()
const { currentFolderName, filteredItems } = storeToRefs(mapStore)
</script>

<style scoped></style>
