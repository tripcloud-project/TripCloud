<!-- The exported code uses Tailwind CSS. Install Tailwind CSS in your dev environment to ensure all styles work. -->

<template>
  <div class="min-h-screen bg-gray-50 flex">
    <!-- Left Sidebar -->
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
          <div v-for="folder in filteredFolders" :key="folder.id" class="cursor-pointer">
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
          >
            <i :class="[item.icon, 'mr-2', item.color]"></i>
            <span class="text-sm">{{ item.name }}</span>
          </div>
        </div>
      </div>
    </div>

    <!-- Main Content Area -->
    <div class="flex-1 flex flex-col h-screen overflow-hidden">
      <!-- Header -->
      <div class="bg-white border-b border-gray-200 p-4 flex items-center justify-between">
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
          >
            <i class="fas fa-download mr-1"></i> Download
          </button>
          <button
            class="px-3 py-1.5 bg-blue-50 text-blue-600 rounded-lg text-sm font-medium hover:bg-blue-100 cursor-pointer !rounded-button whitespace-nowrap"
          >
            <i class="fas fa-share-alt mr-1"></i> Share
          </button>
          <button
            class="px-3 py-1.5 bg-red-50 text-red-600 rounded-lg text-sm font-medium hover:bg-red-100 cursor-pointer !rounded-button whitespace-nowrap"
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
          <button
            class="px-4 py-2 bg-blue-600 text-white rounded-lg text-sm font-medium hover:bg-blue-700 cursor-pointer !rounded-button whitespace-nowrap"
          >
            <i class="fas fa-upload mr-1"></i> Upload Files
          </button>
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
            <p class="text-sm text-gray-800">{{ getFileTypeLabel(selectedFile.type) }}</p>
          </div>

          <div>
            <h4 class="text-sm font-medium text-gray-500 mb-1">크기</h4>
            <p class="text-sm text-gray-800">{{ formatSize(selectedFile.size) }}</p>
          </div>

          <div>
            <h4 class="text-sm font-medium text-gray-500 mb-1">생성일</h4>
            <p class="text-sm text-gray-800">{{ formatDate(selectedFile.created) }}</p>
          </div>

          <div>
            <h4 class="text-sm font-medium text-gray-500 mb-1">수정일</h4>
            <p class="text-sm text-gray-800">{{ formatDate(selectedFile.modified) }}</p>
          </div>

          <div v-if="selectedFile.type === 'image'">
            <h4 class="text-sm font-medium text-gray-500 mb-1">촬영일</h4>
            <p class="text-sm text-gray-800">{{ formatDate(selectedFile.taken) }}</p>
          </div>
          <!-- 
          <div>
            <h4 class="text-sm font-medium text-gray-500 mb-1">Location</h4>
            <p class="text-sm text-gray-800">{{ selectedFile.location }}</p>
          </div> -->

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
          >
            <i class="fas fa-download text-gray-700 mb-1"></i>
            <span class="text-xs">Download</span>
          </button>
          <button
            class="flex flex-col items-center p-2 rounded-lg hover:bg-gray-100 cursor-pointer !rounded-button whitespace-nowrap"
          >
            <i class="fas fa-share-alt text-gray-700 mb-1"></i>
            <span class="text-xs">Share</span>
          </button>
          <button
            class="flex flex-col items-center p-2 rounded-lg hover:bg-gray-100 cursor-pointer !rounded-button whitespace-nowrap"
          >
            <i class="fas fa-trash-alt text-gray-700 mb-1"></i>
            <span class="text-xs">Delete</span>
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
import { ref, computed, onMounted, watch } from 'vue'

// Empty folder image
const emptyFolderImage =
  'https://readdy.ai/api/search-image?query=A%20minimalist%20illustration%20of%20an%20empty%20folder%20with%20a%20slight%20shadow%2C%20clean%20lines%2C%20simple%20design%2C%20light%20background%2C%20professional%20look%2C%20subtle%20colors%2C%20business%20context%2C%20cloud%20storage%20concept&width=300&height=300&seq=1&orientation=squarish'

import api from '@/lib/api'
import flattenDirectoryTree from '@/utils/flattenDirectoryTree'

const folders = ref([])

const loadDirectoryTree = async () => {
  const { data } = await api.get('/gallery') // 백엔드 API
  folders.value = flattenDirectoryTree(data.result)
  console.log('data.result: ', data)
}

onMounted(() => {
  loadDirectoryTree()
  fetchItems()
})

const items = ref([])
import { useDriveStore } from '@/stores/drive.js'
import mapApiResponseToItems from '@/utils/mapApiResponseToItems.js'
const driveStore = useDriveStore()

const fetchItems = async () => {
  try {
    const res = await api.get(`/gallery/list`, {
      params: { prefix: driveStore.prefix },
    })
    if (res.data.status === 'success') {
      items.value = mapApiResponseToItems(res.data.result, driveStore.prefix)
      console.log('items.value: ', items.value)
    }
  } catch (err) {
    console.error('[fetchItems] 오류:', err)
  }
}

const selectItem = (itemId) => {
  const item = items.value.find((i) => i.id === itemId)
  if (item && item.type !== 'folder') {
    selectedFile.value = item
  } else if (item && item.type === 'folder') {
    selectFolder(itemId)
  }
}

// Quick access items
const quickAccess = ref([
  { id: 'recent', name: 'Recent', icon: 'fas fa-clock', color: 'text-blue-500' },
  { id: 'starred', name: 'Starred', icon: 'fas fa-star', color: 'text-yellow-500' },
  { id: 'shared', name: 'Shared with me', icon: 'fas fa-user-friends', color: 'text-green-500' },
  { id: 'trash', name: 'Trash', icon: 'fas fa-trash-alt', color: 'text-red-500' },
])

// State variables
const selectedFolder = ref('/')
const expandedFolders = ref(['/'])
const selectedItems = ref([])
const selectedFile = ref(null)
const searchQuery = ref('')
const sortBy = ref('name')
const sortDirection = ref('asc')

// Context menu
const contextMenu = ref({
  show: false,
  x: 0,
  y: 0,
  item: null,
})

// Computed properties
const filteredFolders = computed(() => {
  let result = [...folders.value]

  if (searchQuery.value) {
    const query = searchQuery.value.toLowerCase()
    result = result.filter((folder) => folder.name.toLowerCase().includes(query))
  }

  return result
})

const currentFolderName = computed(() => {
  const folder = folders.value.find((f) => f.id === selectedFolder.value)
  return folder ? folder.name : 'My Files'
})

const filteredItems = computed(() => {
  let result = items.value.filter((item) => item.parent === selectedFolder.value)

  if (searchQuery.value) {
    const query = searchQuery.value.toLowerCase()
    result = result.filter((item) => item.name.toLowerCase().includes(query))
  }

  // Sort items
  result.sort((a, b) => {
    // Always put folders first
    if (a.type === 'folder' && b.type !== 'folder') return -1
    if (a.type !== 'folder' && b.type === 'folder') return 1

    // Then sort by selected criteria
    let valueA, valueB

    if (sortBy.value === 'name') {
      valueA = a.name.toLowerCase()
      valueB = b.name.toLowerCase()
    } else if (sortBy.value === 'modified') {
      valueA = new Date(a.modified).getTime()
      valueB = new Date(b.modified).getTime()
    } else if (sortBy.value === 'size') {
      valueA = a.size || 0
      valueB = b.size || 0
    }

    if (sortDirection.value === 'asc') {
      return valueA < valueB ? -1 : 1
    } else {
      return valueA > valueB ? -1 : 1
    }
  })

  return result
})

const contextMenuItems = computed(() => {
  const item = contextMenu.value.item
  if (!item) return []

  const isFolder = item.type === 'folder'

  const baseItems = [
    {
      label: isFolder ? 'Open' : 'Preview',
      icon: isFolder ? 'fa-folder-open' : 'fa-eye',
      action: 'open',
    },
    { label: 'Rename', icon: 'fa-edit', action: 'rename' },
    { label: 'Copy to', icon: 'fa-copy', action: 'copy' },
    { label: 'Move to', icon: 'fa-cut', action: 'move' },
    { type: 'divider' },
    { label: 'Download', icon: 'fa-download', action: 'download' },
    { label: 'Share', icon: 'fa-share-alt', action: 'share' },
    { type: 'divider' },
    { label: 'Delete', icon: 'fa-trash-alt', action: 'delete', danger: true },
  ]

  return baseItems
})

// Methods
// const selectFolder = (folderId) => {
//   selectedFolder.value = folderId;
//   clearSelection();
//   clearFileSelection();
// };
const selectFolder = (folderId) => {
  console.log('selectFolder\nfolderId:', folderId)
  // const folder = items.value.find(i => i.id === folderId)
  // console.log("folder: ", folder)
  // if (folder) {
  selectedFolder.value = folderId
  // const newPrefix = `${driveStore.prefix}${folder.name}`
  const newPrefix = folderId
  // const newPrefix = driveStore.prefix==='/'?`${folder.name}`:`${driveStore.prefix}${folder.name}`
  driveStore.setPrefix(newPrefix)
  fetchItems()

  // }
}

const toggleFolder = (folderId) => {
  const index = expandedFolders.value.indexOf(folderId)
  if (index === -1) {
    expandedFolders.value.push(folderId)
  } else {
    expandedFolders.value.splice(index, 1)
  }
}

// const selectItem = (itemId) => {
//   const item = items.value.find(i => i.id === itemId);
//   if (item && item.type !== 'folder') {
//     selectedFile.value = item;
//   } else {
//     // If it's a folder, navigate into it
//     if (item && item.type === 'folder') {
//       selectFolder(itemId);
//     }
//   }
// };

const toggleItemSelection = (itemId) => {
  const index = selectedItems.value.indexOf(itemId)
  if (index === -1) {
    selectedItems.value.push(itemId)
  } else {
    selectedItems.value.splice(index, 1)
  }
}

const clearSelection = () => {
  selectedItems.value = []
}

const clearFileSelection = () => {
  selectedFile.value = null
}

const toggleSortDirection = () => {
  sortDirection.value = sortDirection.value === 'asc' ? 'desc' : 'asc'
}

const formatDate = (dateString) => {
  const date = new Date(dateString)
  return date.toLocaleDateString('en-US', {
    year: 'numeric',
    month: 'short',
    day: 'numeric',
  })
}

const formatSize = (bytes) => {
  if (!bytes) return '—'

  const units = ['B', 'KB', 'MB', 'GB', 'TB']
  let size = bytes
  let unitIndex = 0

  while (size >= 1024 && unitIndex < units.length - 1) {
    size /= 1024
    unitIndex++
  }

  return `${size.toFixed(1)} ${units[unitIndex]}`
}

const getFileTypeLabel = (type) => {
  switch (type) {
    case 'document':
      return 'Document'
    case 'image':
      return 'Image'
    case 'video':
      return 'Video'
    case 'folder':
      return 'Folder'
    default:
      return 'File'
  }
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

const handleContextMenuAction = (action) => {
  const item = contextMenu.value.item

  switch (action) {
    case 'open':
      if (item.type === 'folder') {
        selectFolder(item.id)
      } else {
        selectItem(item.id)
      }
      break
    case 'rename':
      // Implement rename functionality
      console.log('Rename', item.name)
      break
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
      console.log('Download', item.name)
      break
    case 'share':
      // Implement share functionality
      console.log('Share', item.name)
      break
    case 'delete':
      // Implement delete functionality
      console.log('Delete', item.name)
      break
  }

  closeContextMenu()
}

// Close context menu when clicking outside
onMounted(() => {
  document.addEventListener('click', (event) => {
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

// Debug
// Watchers for each reactive reference
watch(selectedFolder, (newValue, oldValue) => {
  console.log('selectedFolder changed:', { oldValue, newValue })
})

watch(expandedFolders, (newValue, oldValue) => {
  console.log('expandedFolders changed:', { oldValue, newValue })
})

watch(selectedItems, (newValue, oldValue) => {
  console.log('selectedItems changed:', { oldValue, newValue })
})

watch(selectedFile, (newValue, oldValue) => {
  console.log('selectedFile changed:', { oldValue, newValue })
})

watch(searchQuery, (newValue, oldValue) => {
  console.log('searchQuery changed:', { oldValue, newValue })
})

watch(sortBy, (newValue, oldValue) => {
  console.log('sortBy changed:', { oldValue, newValue })
})

watch(sortDirection, (newValue, oldValue) => {
  console.log('sortDirection changed:', { oldValue, newValue })
})
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
