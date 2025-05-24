import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import api from '@/lib/api'
import mapApiResponseToItems from '@/utils/drive/mapApiResponseToItems'
import flattenDirectoryTree from '@/utils/drive/flattenDirectoryTree'
export const useDriveStore = defineStore(
  'drive',
  () => {
    // ref properties
    const prefix = ref('/') // 기본 경로
    const selectedFolder = ref('/')
    const expandedFolders = ref(['/'])
    const selectedItems = ref([])
    const selectedFile = ref(null)
    const searchQuery = ref('')
    const sortBy = ref('name')
    const sortDirection = ref('asc')
    const folders = ref([])
    const items = ref([])
    const contextMenu = ref({
      show: false,
      x: 0,
      y: 0,
      item: null,
    })
    const quickAccess = ref([
      { id: 'recent', name: 'Recent', icon: 'fas fa-clock', color: 'text-blue-500' },
      { id: 'starred', name: 'Starred', icon: 'fas fa-star', color: 'text-yellow-500' },
      { id: 'trash', name: 'Trash', icon: 'fas fa-trash-alt', color: 'text-red-500' },
    ])
    const fileInput = ref(null)
    const folderInput = ref(null)
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
        // { label: 'Share', icon: 'fa-share-alt', action: 'share' },
        { type: 'divider' },
        { label: 'Delete', icon: 'fa-trash-alt', action: 'delete', danger: true },
      ]

      return baseItems
    })

    // Function
    const setPrefix = (newPrefix) => {
      prefix.value = newPrefix
    }
    const clearSelection = () => {
      selectedItems.value = []
      selectedFile.value = null
    }
    const toggleFolder = (folderId) => {
      const index = expandedFolders.value.indexOf(folderId)
      if (index === -1) {
        expandedFolders.value.push(folderId)
      } else {
        expandedFolders.value.splice(index, 1)
      }
    }
    const selectFolder = (folderId) => {
      selectedFolder.value = folderId
      const newPrefix = folderId
      setPrefix(newPrefix)
      fetchItems()
    }
    const fetchItems = async () => {
      try {
        const res = await api.get(`/gallery/list`, {
          params: { prefix: prefix.value },
        })
        if (res.data.status === 'success') {
          items.value = mapApiResponseToItems(res.data.result, prefix.value)
          console.log(items.value)
        }
      } catch (err) {
        console.error('[fetchItems] 오류:', err)
      }
    }
    const loadDirectoryTree = async () => {
      const { data } = await api.get('/gallery') // 백엔드 API
      folders.value = flattenDirectoryTree(data.result)

    }
    return {
      prefix,
      setPrefix,
      selectedFolder,
      expandedFolders,
      selectedItems,
      selectedFile,
      searchQuery,
      sortBy,
      sortDirection,
      clearSelection,
      folders,
      filteredFolders,
      toggleFolder,
      contextMenu,
      items,
      quickAccess,
      currentFolderName,
      filteredItems,
      contextMenuItems,
      fileInput,
      folderInput,
      selectFolder,
      fetchItems,
      loadDirectoryTree,
    }
  },
  {
    persist: { storage: sessionStorage }, // 새로고침해도 유지
  }
)
