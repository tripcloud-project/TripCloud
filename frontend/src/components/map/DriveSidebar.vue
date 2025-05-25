<!-- Left Sidebar -->
<template>
  <div class="w-[250px] bg-white border-r border-gray-200 flex flex-col h-screen">
    <div class="p-4 border-b border-gray-200">
      <h1 class="text-xl font-semibold text-gray-800">CloudStorage</h1>
    </div>

    <!-- Search Filter -->
    <div class="p-3 border-b border-gray-200">
      <div class="relative">
        <input
          type="text"
          v-model="searchQuery"
          placeholder="Search files and folders"
          class="w-full pl-9 pr-3 py-2 text-sm border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent"
        />
        <i class="fas fa-search absolute left-3 top-2.5 text-gray-400 text-sm"></i>
      </div>
    </div>

    <!-- Directory Tree -->
    <div class="overflow-y-auto flex-grow p-2">
      <div class="space-y-1">
        <!-- <div v-for="folder in filteredFolders" :key="folder.id" class="cursor-pointer"> -->
        <div v-for="folder in visibleFolders" :key="folder.id" class="cursor-pointer">
          <div
            :class="[
              'flex items-center p-2 rounded-lg',
              selectedFolder === folder.id ? 'bg-blue-50 text-blue-600' : 'hover:bg-gray-100',
            ]"
            @click="selectFolder(folder.id)"
          >
            <div class="flex items-center" :style="{ marginLeft: `${folder.level * 12}px` }">
              <i
                v-if="folder.hasChildren"
                :class="[
                  'mr-1 text-gray-500 w-4',
                  expandedFolders.includes(folder.id)
                    ? 'fas fa-chevron-down'
                    : 'fas fa-chevron-right',
                ]"
                @click.stop="toggleFolder(folder.id)"
              ></i>
              <span v-else class="w-4"></span>
              <i class="fas fa-folder text-yellow-400 mr-2"></i>
              <span class="text-sm font-medium truncate">{{ folder.name }}</span>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- Quick Access -->
    <div class="p-3 border-t border-gray-200">
      <h3 class="text-xs font-semibold text-gray-500 uppercase tracking-wider mb-2">
        Quick Access
      </h3>
      <div class="space-y-1">
        <div
          v-for="item in quickAccess"
          :key="item.id"
          class="flex items-center p-2 rounded-lg hover:bg-gray-100 cursor-pointer"
          @click="handleQuickAccessClick(item.id)"
        >
          <i :class="[item.icon, 'mr-2', item.color]"></i>
          <span class="text-sm">{{ item.name }}</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { useMapStore } from '@/stores/map.js'
import { storeToRefs } from 'pinia'
const mapStore = useMapStore()
const { selectedFolder, expandedFolders, searchQuery, visibleFolders, quickAccess } =
  storeToRefs(mapStore)

const { toggleFolder, selectFolder, handleQuickAccessClick } = mapStore
</script>

<style scoped></style>
