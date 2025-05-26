<template>
  <div class="bg-white border-b border-gray-200 p-4 flex items-center justify-between">
    <div class="flex items-center">
      <h2 class="text-lg font-medium text-gray-800">{{ currentFolderName }}</h2>
      <span class="text-gray-500 ml-2">({{ filteredItems.length }} items)</span>
    </div>
    <div class="flex items-center space-x-2">
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
import { useMapStore } from '@/stores/map.js'
import { storeToRefs } from 'pinia'
const mapStore = useMapStore()
const { sortBy, sortDirection, currentFolderName, filteredItems } = storeToRefs(mapStore)
const toggleSortDirection = () => {
  sortDirection.value = sortDirection.value === 'asc' ? 'desc' : 'asc'
}
</script>

<style scoped></style>
