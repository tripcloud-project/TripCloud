import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import api from '@/lib/api'
import mapApiResponseToItems from '@/utils/drive/mapApiResponseToItems'
import flattenDirectoryTree from '@/utils/drive/flattenDirectoryTree'
import { downloadFiles } from '@/utils/drive/download.js'
import { deleteFiles } from '@/utils/drive/delete.js'
import { useRouter } from 'vue-router'
import { getStorage } from '@/utils/drive/storage.js'

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
      { id: 'drive', name: '드라이브', icon: 'fas fa-clock', color: 'text-blue-500' },
      { id: 'trash', name: '휴지통', icon: 'fas fa-trash-alt', color: 'text-red-500' },
      { id: 'map', name: '지도', icon: 'fas fa-map', color: 'text-green-500' },
    ])

    const router = useRouter()
    const handleQuickAccessClick = (id) => {
      if (id === 'trash') {
        router.push({ name: 'trash' })
      } else if (id === 'map') {
        router.push({ name: 'map' })
      } else if (id === 'drive') {
        router.push({ name: 'drive' })
      }
    }

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
    const visibleFolders = computed(() => {
      // 검색 중일 땐 모두 보여줌
      if (searchQuery.value) {
        return filteredFolders.value
      }

      // 검색이 아닐 땐 트리 접힘 고려
      const result = []
      const visibleParents = new Set([]) // 루트는 항상 표시

      for (const folder of filteredFolders.value) {
        if (folder.level === 0 || visibleParents.has(folder.parent)) {
          result.push(folder)

          if (folder.hasChildren && expandedFolders.value.includes(folder.id)) {
            visibleParents.add(folder.id)
          }
        }
      }

      return result
    })

    const currentFolderName = computed(() => {
      const folder = folders.value.find((f) => f.id === selectedFolder.value)
      return folder ? folder.name : 'My Files'
    })

    const filteredItems = computed(() => {
      let result = items.value.filter((item) => item.parent === selectedFolder.value)

      // if (searchQuery.value) {
      //   const query = searchQuery.value.toLowerCase()
      //   result = result.filter((item) => item.name.toLowerCase().includes(query))
      // }

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
        } else if (sortBy.value === 'taken') {
          valueA = new Date(a.taken).getTime()
          valueB = new Date(b.taken).getTime()
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
          label: isFolder ? '열기' : '미리보기',
          icon: isFolder ? 'fa-folder-open' : 'fa-eye',
          action: 'open',
        },
        { label: '이름 바꾸기', icon: 'fa-edit', action: 'rename' },
      ]

      // 폴더인 경우 '설명 추가' 항목 삽입
      if (!isFolder) {
        baseItems.push({ label: '설명 추가', icon: 'fa-comment-dots', action: 'addDescription' })
      }

      baseItems.push(
        { type: 'divider' },
        { label: '다운로드', icon: 'fa-download', action: 'download' },
        { type: 'divider' },
        { label: '삭제', icon: 'fa-trash-alt', action: 'delete', danger: true },
      )

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
        }
      } catch (err) {
        console.error('[fetchItems] 오류:', err)
      }
    }
    const loadDirectoryTree = async () => {
      fetchStorage()
      const { data } = await api.get('/gallery') // 백엔드 API
      folders.value = flattenDirectoryTree(data.result)
    }
    const clearFileSelection = () => {
      selectedFile.value = null
    }

    const selectItem = (itemId) => {
      const item = items.value.find((i) => i.id === itemId)
      if (item && item.type !== 'folder') {
        selectedFile.value = item
      } else if (item && item.type === 'folder') {
        selectFolder(itemId)
      }
    }

    const toggleItemSelection = (itemId) => {
      const index = selectedItems.value.indexOf(itemId)
      if (index === -1) {
        selectedItems.value.push(itemId)
      } else {
        selectedItems.value.splice(index, 1)
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

    // [다중 다운로드]
    const downloadSelectedFiles = async () => {
      const prefixList = selectedItems.value
        .map((id) => filteredItems.value.find((i) => i.id === id && i.type === 'folder')?.id)
        .filter(Boolean)

      const fileIdList = selectedItems.value
        .map((id) => filteredItems.value.find((i) => i.id === id && i.type !== 'folder')?.id)
        .filter(Boolean)

      await downloadFiles({
        prefixList,
        fileIdList,
        currentPrefix: prefix.value,
      })
    }

    const deleteSelectedFiles = async () => {
      const prefixList = selectedItems.value
        .map((id) => filteredItems.value.find((i) => i.id === id && i.type === 'folder')?.id)
        .filter(Boolean)

      const fileIdList = selectedItems.value
        .map((id) => filteredItems.value.find((i) => i.id === id && i.type !== 'folder')?.id)
        .filter(Boolean)

      const result = await deleteFiles({ prefixList, fileIdList })

      if (result.status === 'success') {
        await fetchItems()
        await loadDirectoryTree()
        selectedItems.value = []
      }
    }
    const usedStorage = ref(0)
    const maxStorage = ref(0)
    const usagePercent = computed(() => {
      if (!maxStorage.value) return 0
      return (usedStorage.value / maxStorage.value) * 100
    })
    async function fetchStorage() {
      const data = await getStorage()
      if (data.status === 'success') {
        usedStorage.value = Number(data.result.usedStorage)
        maxStorage.value = Number(data.result.maxStorage)
      }
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
      clearFileSelection,
      selectItem,
      toggleItemSelection,
      showContextMenu,
      closeContextMenu,
      downloadSelectedFiles,
      deleteSelectedFiles,
      handleQuickAccessClick,
      visibleFolders,
      usedStorage,
      maxStorage,
      fetchStorage,
      usagePercent,
    }
  },
  {
    persist: { storage: sessionStorage }, // 새로고침해도 유지
  },
)
